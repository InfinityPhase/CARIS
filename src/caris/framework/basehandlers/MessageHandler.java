package caris.framework.basehandlers;

import java.util.ArrayList;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Constants;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;

public class MessageHandler extends Handler {

	public MessageReceivedEvent mrEvent;
	public String message;
	public boolean setupComplete;
	
	public String keyword = "";
	
	public MessageHandler() {
		this("", false);
	}
	
	public MessageHandler(String name) {
		this(name, false);
	}
	
	public MessageHandler(boolean allowBots) {
		this("", allowBots);
	}
	
	public MessageHandler(String name, boolean allowBots) {
		super(name, allowBots);
		mrEvent = null;
		message = "";
	}
	
	@Override
	public Reaction handle(Event event) {
		Logger.debug("Checking " + name, 0, true);
		setup(event);
		if( setupComplete ) {
			if( botFilter(event) ) {
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
		Logger.debug("Ignoring " + name, 1, true);
		return null;
	}
	
	private void setup(Event event) {
		if( event instanceof MessageReceivedEvent ) {
			mrEvent = (MessageReceivedEvent) event;
			message = mrEvent.getMessage().getContent();
			setupComplete = true;
		} else {
			setupComplete = false;
		}
	}
	
	protected boolean isElevated() {
		Logger.debug("Checking if elevated", 3);
		boolean check = isDeveloper() || isAdmin();
		return check;
	}
	
	protected boolean isDeveloper() {
		Logger.debug("Checking if developer", 3);
		boolean check = false;
		for( long id : Constants.ADMIN_IDS ) {
			if( mrEvent.getAuthor().getLongID() == id ) {
				check = true;
			}
		}
		if( check ) {
			Logger.debug("Developer confirmed", 4);
		} else {
			Logger.debug("Developer unconfirmed", 4);
		}
		return check;
	}
	
	protected boolean isAdmin() {
		Logger.debug("Checking if admin", 3);
		boolean check = mrEvent.getAuthor().getPermissionsForGuild(mrEvent.getGuild()).contains(Permissions.ADMINISTRATOR);
		if( check ) {
			Logger.debug("Admin confirmed", 4);
		} else {
			Logger.debug("Admin unconfirmed", 4);
		}
		return check;
	}
	
	protected boolean isMentioned() {
		Logger.debug("Checking mention", 3);
		boolean check = StringUtilities.containsIgnoreCase(message, Constants.NAME);
		if( check ) {
			Logger.debug("Name mentioned", 4);
		} else {
			Logger.debug("Name unmentioned", 4);
		}
		return check;
	}
	
	protected boolean keywordMatched() {
		Logger.debug("Checking keyword", 3);
		ArrayList<String> tokens = TokenUtilities.parseTokens(message, new char[] {});
		Logger.debug("Message tokens: " + tokens.toString(), 4);
		boolean check = false;
		if( tokens.size() >= 1 ) {
			check = tokens.get(1).equalsIgnoreCase(keyword);
		} else {
			Logger.debug("No keyword specified", 4);
		}
		if( check ) {
			Logger.debug("Keyword matched", 4);
		} else {
			Logger.debug("Keyword match failed", 4);
		}
		return check;
	}
	
	protected boolean isInvoked() {
		Logger.debug("Checking invocation", 3);
		ArrayList<String> tokens = TokenUtilities.parseTokens(message, new char[] {});
		Logger.debug("Message tokens: " + tokens.toString(), 4);
		boolean check = false;
		if( tokens.size() >= 2 ) {
			check = tokens.get(0).equalsIgnoreCase(Constants.INVOCATION_PREFIX) && tokens.get(1).equalsIgnoreCase(keyword);
		}
		if( check ) {
			Logger.debug("Handler invoked", 4);
		} else {
			Logger.debug("Handler uninvoked", 4);
		}
		return check;
	}
	
}
