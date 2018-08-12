package caris.framework.handlers;

import java.util.HashMap;

import caris.framework.reactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.basehandlers.Handler;
import caris.framework.library.GuildInfo;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.obj.IChannel;

public class GuildCreateHandler extends Handler {

	public GuildCreateHandler() {
		super("GuildCreate Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return event instanceof GuildCreateEvent;
	}
	
	@Override
	protected Reaction process(Event event) {
		GuildCreateEvent guildCreateEvent = (GuildCreateEvent) event;
		if( !Variables.guildIndex.containsKey( guildCreateEvent.getGuild() ) ) {
			GuildInfo guildInfo = new GuildInfo( guildCreateEvent.getGuild().getName(), guildCreateEvent.getGuild() );
			Variables.guildIndex.put( guildCreateEvent.getGuild(), guildInfo );			
			for( IChannel c : guildCreateEvent.getGuild().getChannels() ) {
				if( !Variables.guildIndex.get( guildCreateEvent.getGuild() ).settings.containsKey( c ) ) {
					Variables.guildIndex.get( guildCreateEvent.getGuild() ).settings.put( c, new HashMap<String, Object>() );
				}
			}
		}
		return new Reaction(); // TODO: Change to a logging reaction
	}
	
}
