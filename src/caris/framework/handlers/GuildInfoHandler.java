package caris.framework.handlers;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.embedbuilders.GuildInfoBuilder;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionEmbed;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.api.events.Event;

public class GuildInfoHandler extends MessageHandler {

	public GuildInfoHandler() {
		super("GuildInfo Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return isAdmin() && isMentioned() && StringUtilities.containsIgnoreCase(message, "guild") && StringUtilities.containsAnyOfIgnoreCase(message, "info", "data", "analysis", "stats", "statistics");
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.print("GuildInfo detected", 2);
		Logger.debug("Response produced from " + name, 1);
		return new ReactionEmbed(new GuildInfoBuilder(mrEvent.getGuild()), mrEvent.getChannel());
	}
}
