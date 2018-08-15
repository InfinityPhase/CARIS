package caris.framework.handlers;

import caris.framework.basehandlers.Handler;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionCreateGuild;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;

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
		Logger.debug("Guild creation detected", 2);
		GuildCreateEvent guildCreateEvent = (GuildCreateEvent) event;
		Logger.debug("Reaction produced from " + name, 1, true);
		return new ReactionCreateGuild(guildCreateEvent.getGuild(), -1);
	}
	
}
