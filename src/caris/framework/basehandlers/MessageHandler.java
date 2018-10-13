package caris.framework.basehandlers;

import java.util.ArrayList;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Constants;
import caris.framework.library.Variables;
import caris.framework.tokens.RedirectedMessage;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;

public class MessageHandler extends Handler {
		
	public enum Access {
		DEFAULT,
		ADMIN,
		DEVELOPER
	};
	
	public Access accessLevel;
	
	public MessageReceivedEvent mrEvent;
	public String message;
	public boolean setupComplete;
	
	public MessageHandler(String name, Access accessLevel, boolean allowBots) {
		super(name, allowBots);
		this.accessLevel = accessLevel;
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
			} if( isTriggered(event) && (accessLevel != Access.ADMIN || isElevated()) && (accessLevel != Access.DEVELOPER || isDeveloper()) ) {
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
			ArrayList<String> tokens = TokenUtilities.parseTokens(message);
			if( tokens.size() > 0 ) {
				ArrayList<String> captured = TokenUtilities.parseCaptured(tokens.get(0), "{", "}");
				if( captured.size() > 0 ) {
					Long channelID = 0L;
					try {
						channelID = Long.parseLong(captured.get(0));
					} catch (NumberFormatException e) {
						// do nothing
					}
					if( channelID != 0L ) {
						for( IGuild guild : Variables.guildIndex.keySet() ) {
							for( IChannel channel : Variables.guildIndex.get(guild).channelIndex.keySet() ) {
								if( channelID.equals(channel.getLongID()) ) {
									String newMessage = message.substring(captured.get(0).length()+1);
									mrEvent = new MessageReceivedEvent(new RedirectedMessage(mrEvent.getMessage(), channel, newMessage));
								}
							}
						}
					}
				}
			}
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
	
	protected boolean isInvoked() {
		Logger.debug("Checking invocation", 3);
		ArrayList<String> tokens = TokenUtilities.parseTokens(message, new char[] {});
		Logger.debug("Message tokens: " + tokens.toString(), 4);
		boolean check = false;
		if( tokens.size() >= 1 ) {
			check = tokens.get(0).equalsIgnoreCase(getKeyword());
		}
		if( check ) {
			Logger.debug("Handler invoked", 4);
		} else {
			Logger.debug("Handler uninvoked", 4);
		}
		return check;
	}
	
	protected String getKeyword() {
		return Constants.INVOCATION_PREFIX + name;
	}
	
}
