package caris.framework.handlers;

import caris.framework.basehandlers.Handler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.reactions.ReactionHear;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class MessageLoggerHandler extends Handler {
	
	public MessageLoggerHandler() {
		super("MessageLogger Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return event instanceof MessageReceivedEvent;
	}
	
	@Override
	protected Reaction process(Event event) {
		MultiReaction logMessage = new MultiReaction(-1);
		Logger.debug("Message detected", 2);
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		logMessage.reactions.add(new ReactionHear(messageReceivedEvent.getMessage().getContent(), messageReceivedEvent.getAuthor(), messageReceivedEvent.getChannel()));
		Logger.debug("Response produced from " + name, 1, true);
		return logMessage;
	}
	
}
