package caris.framework.handlers;

import caris.framework.basehandlers.Handler;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionLoggerHear;
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
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		return new ReactionLoggerHear(messageReceivedEvent.getMessage().getContent(), messageReceivedEvent.getAuthor(), messageReceivedEvent.getChannel());
	}
	
}
