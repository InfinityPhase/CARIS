package caris.framework.basehandlers;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;

public class GeneralHandler extends Handler {
	
	public GeneralHandler(String name, boolean allowBots) {
		super(name, allowBots);
	}
	
	@Override
	public Reaction handle(Event event) {
		Logger.debug("Checking " + name, 0, true);
		if( botFilter(event) ) {
			Logger.debug("Event from a bot, ignoring", 1, true);
			return null;
		} if( isTriggered(event) ) {
			Logger.debug("Processing " + name, 1);
			return process(event);
		} else {
			Logger.debug("Ignoring " + name, 1, true);
			return null;
		}
	}
	
	protected boolean isTriggered(Event event) {
		return false;
	}
	
	protected Reaction process(Event event) {
		return null;
	}
	
}
