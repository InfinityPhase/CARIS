package caris.framework.basehandlers;

import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.library.Constants;
import caris.framework.library.Variables;
import caris.framework.tokens.RedirectedMessage;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public abstract class MessageHandler extends Handler {
		
	public enum Access {
		DEFAULT,
		ADMIN,
		DEVELOPER
	};
	
	public String invocation;
	public Access accessLevel;
		
	public MessageHandler(String name, Access accessLevel, boolean allowBots) {
		super(name, allowBots);
		this.invocation = Constants.INVOCATION_PREFIX + name;
		this.accessLevel = accessLevel;
	}
	
	@Override
	public Reaction handle(Event event) {
		Logger.debug("Checking " + name, 0, true);
		if( event instanceof MessageReceivedEvent ) {
			MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
			if( !messageReceivedEvent.getChannel().isPrivate() ) {
				MessageEventWrapper messageEventWrapper = wrap(messageReceivedEvent);
				if( botFilter(event) ) {
					return null;
				} else if( isTriggered(messageEventWrapper) && messageEventWrapper.accessGranted(accessLevel) ) {
					return process(messageEventWrapper);
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	private MessageEventWrapper wrap(MessageReceivedEvent messageReceivedEvent) {
		MessageEventWrapper messageEventWrapper = new MessageEventWrapper(messageReceivedEvent);
		if( messageEventWrapper.tokens.size() > 0 ) {
			String token = messageEventWrapper.tokens.get(0);
			if( token.startsWith("{") && token.endsWith("}") && token.length() > 2 && messageEventWrapper.message.length() > token.length() + 1) {
				String tokenContent = token.substring(1, token.length()-1);
				try {
					Long channelID = Long.parseLong(tokenContent);
					for( IGuild guild : Variables.guildIndex.keySet() ) {
						for( IChannel channel : Variables.guildIndex.get(guild).channelIndex.keySet() ) {
							if( channel.getLongID() == channelID ) {
								messageEventWrapper = new MessageEventWrapper(
														new MessageReceivedEvent(
															new RedirectedMessage(messageReceivedEvent.getMessage(), channel, messageEventWrapper.message.substring(messageEventWrapper.message.indexOf("}"+2)))
														));
							}
						}
					}
				} catch (NumberFormatException e) {
					
				}
			}
		}
		return messageEventWrapper;
	}
	
	protected abstract boolean isTriggered(MessageEventWrapper messageEventWrapper);
	protected abstract Reaction process(MessageEventWrapper messageEventWrapper);
	
}
