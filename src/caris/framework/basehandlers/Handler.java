package caris.framework.basehandlers;

import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Constants;
import caris.framework.utilities.Logger;

public class Handler {
	
	public String name;
	public boolean allowBots;
	
	public Handler() {
		this("", false);
	}
	
	public Handler(String name) {
		this(name, false);
	}
	
	public Handler(boolean allowBots) {
		this("", allowBots);
	}
	
	public Handler(String name, boolean allowBots) {
		this.name = name;
		this.allowBots = allowBots;
		Logger.debug("Handler " + name + " initialized.", 1);
	}
	
	public Reaction handle(Event event) {
		Logger.debug("Checking " + name, 0, true);
		if( isBot(event) && !allowBots) {
			Logger.debug("Event from a bot, ignoring", 1, true);
			return null;
		} if( isTriggered(event) ) {
			Logger.debug("Processing " + name, 1, true);
			return process(event);
		} else {
			Logger.debug("Ignoring " + name, 1, true);
			return null;
		}
	}
	
	protected boolean isBot(Event event) {
		if( event instanceof MessageReceivedEvent ) {
			if( !Constants.RESPOND_TO_BOT && ((MessageReceivedEvent) event).getAuthor().isBot() ) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isTriggered(Event event) {
		return false;
	}
	
	protected Reaction process(Event event) {
		return new Reaction();
	}
	
}
