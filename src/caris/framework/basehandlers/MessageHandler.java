package caris.framework.basehandlers;

import java.util.ArrayList;
import java.util.Arrays;

import caris.framework.library.Constants;
import caris.framework.reactions.Reaction;
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
	
	public String invocation = "";
	
	public MessageHandler() {
		this("");
	}
	
	public MessageHandler(String name) {
		super(name);
		mrEvent = null;
		message = "";
	}
	
	@Override
	public Reaction handle(Event event) {
		Logger.debug("Checking " + name, 0, true);
		setup(event);
		if( setupComplete ) {
			if( isTriggered(event) ) {
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
	
	protected void setup(Event event) {
		if( event instanceof MessageReceivedEvent ) {
			mrEvent = (MessageReceivedEvent) event;
			message = mrEvent.getMessage().getContent();
			setupComplete = true;
		} else {
			setupComplete = false;
		}
	}
	
	@SuppressWarnings("unlikely-arg-type")
	protected boolean isAdmin() {
		Logger.debug("Checking if admin", 3);
		boolean check = mrEvent.getAuthor().getPermissionsForGuild(mrEvent.getGuild()).contains(Permissions.ADMINISTRATOR) || Arrays.asList(Constants.ADMIN_IDS).contains(mrEvent.getAuthor().getLongID());
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
	
	protected boolean isInvoked() {
		Logger.debug("Checking invocation", 3);
		ArrayList<String> tokens = TokenUtilities.parseTokens(message, new char[] {});
		Logger.debug("Message tokens: " + tokens.toString(), 4);
		boolean check = false;
		if( tokens.size() >= 1 ) {
			check = StringUtilities.equalsIgnoreCase(tokens.get(0), invocation);
		}
		if( check ) {
			Logger.debug("Handler invoked", 4);
		} else {
			Logger.debug("Handler uninvoked", 4);
		}
		return check;
	}
	
	protected boolean isAdminInvoked() {
		Logger.debug("Checking admin invocation", 3);
		ArrayList<String> tokens = TokenUtilities.parseTokens(message, new char[] {});
		Logger.debug("Message tokens: " + tokens.toString(), 4);
		boolean check = false;
		if( tokens.size() >= 2 ) {
			check = StringUtilities.equalsIgnoreCase(tokens.get(0), Constants.ADMIN_PREFIX) && StringUtilities.equalsIgnoreCase(tokens.get(1), invocation);
		}
		if( check ) {
			Logger.debug("Admin invoked", 4);
		} else {
			Logger.debug("Admin uninvoked", 4);
		}
		return check;
	}
	
}
