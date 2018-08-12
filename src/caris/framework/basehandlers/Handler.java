package caris.framework.basehandlers;

import sx.blah.discord.api.events.Event;

import caris.framework.reactions.Reaction;

public class Handler {
	
	public String name;
	
	public Handler() {
		this("");
	}
	
	public Handler(String name) {
		this.name = name;
	}
	
	public Reaction handle(Event event) {
		if( isTriggered(event) ) {
			System.out.println("Processing " + name);
			return process(event);
		} else {
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
