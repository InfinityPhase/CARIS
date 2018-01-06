package commands;

import java.util.HashMap;

import library.Variables;
import main.GuildInfo;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.obj.IChannel;
import utilities.Logger;

public class GuildCreate extends SuperEvent {
	private Logger log = new Logger().setBaseIndent(1).build();
	
	@Override
	@EventSubscriber
	public void onGuildCreate( GuildCreateEvent event ) {
		// When connected to a guild, add it to the guild list
		log.log("Checking guild...");
		if( !Variables.guildIndex.containsKey( event.getGuild() ) ) {
			Variables.guildIndex.put( event.getGuild(), new GuildInfo( event.getGuild().getName(), event.getGuild() ) );
			log.indent(1).log("Creating new Guild Object " + event.getGuild().getName() + ":" + event.getGuild().getStringID() );
			
			for( IChannel c : event.getGuild().getChannels() ) { // Init channelMap
				log.indent(1).log("Checking channel: " + c.getName() + " : " + c.getStringID());
				if( !Variables.channelMap.containsKey( c.getStringID() ) ) {
					log.indent(2).log("Added to channelMap");
					Variables.channelMap.put( c.getStringID(), c );
				}
				if( !Variables.guildIndex.get( event.getGuild() ).settings.containsKey( c ) ) {
					log.indent(2).log("Adding channel to settings list");
					Variables.guildIndex.get( event.getGuild() ).settings.put( c, new HashMap<String, Object>() );
				}
			}
		}
	}
}
