package caris.framework.handlers;

import caris.framework.reactions.Reaction;
import sx.blah.discord.api.events.Event;

public class Handler {
	
	public String name;
	
	public Handler() {
		name = "";
	}
	
	public Reaction handle(Event event) {
		if( isTriggered(event) ) {
			return process(event);
		} else {
			return null;
		}
	}
	
	private boolean isTriggered(Event event) {
		return false;
	}
	
	private Reaction process(Event event) {
		return new Reaction();
	}
	
}
