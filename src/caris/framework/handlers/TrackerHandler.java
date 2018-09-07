package caris.framework.handlers;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.api.events.Event;

public class TrackerHandler extends MessageHandler {

	public TrackerHandler() {
		super("Tracker Handler");
	}
	
	@Override
	protected boolean isTriggered() {
		return StringUtilities.containsAnyOfIgnoreCase(message, "track", "tracking");
	}
	
}
