package caris.framework.basehandlers;

import java.util.HashMap;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Constants;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Handler {
	
	public String name;
	public boolean allowBots;
	
	public String description;
	public HashMap<String, String> usage;
	
	public boolean enabled;
	
	public Handler(String name, boolean allowBots) {
		this.name = name;
		this.allowBots = allowBots;
		
		description = "";
		usage = new HashMap<String, String>();
		
		enabled = true;
		
		Logger.debug("Handler " + name + " initialized.", 1);
	}
	
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
	
	protected boolean botFilter(Event event) {
		return isBot(event) && !allowBots;
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
