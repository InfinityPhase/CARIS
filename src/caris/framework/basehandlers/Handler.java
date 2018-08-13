package caris.framework.basehandlers;

import sx.blah.discord.api.events.Event;

import caris.framework.reactions.Reaction;
import caris.framework.utilities.Logger;

public class Handler {
	
	public String name;
	
	public Handler() {
		this("");
	}
	
	public Handler(String name) {
		this.name = name;
		Logger.debug("Handler " + name + " initialized.", 1);
	}
	
	public Reaction handle(Event event) {
		Logger.debug("Checking " + name, 1);
		if( isTriggered(event) ) {
			Logger.debug("Processing " + name, 1);
			return process(event);
		} else {
			Logger.debug("Ignoring " + name, 1);
			return null;
		}
	}
	
	protected boolean isTriggered(Event event) {
		return false;
	}
	
	protected Reaction process(Event event) {
		return new Reaction();
	}
	
}
