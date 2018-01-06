package commands;

import library.Variables;
import main.Brain;
import main.GuildInfo;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
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
			log.indent(1).log("Creating new Guild Object \"" + event.getGuild().getName() + "\".");
			
			if( Variables.guildIndex.containsKey( Brain.cli.getGuildByID( Variables.guildID ))) { // Remove after build season
				log.log("I SET it TO FUCKING TRUE, DAMMNIT");
				Brain.roboGuild = true;
			}
		}
	}
}
