package caris.framework.handlers;

import caris.framework.basehandlers.Handler;
import caris.framework.library.Variables;
import caris.framework.reactions.MultiReaction;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionLoggerHear;
import caris.framework.reactions.ReactionMessage;
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
		boolean overflow = Variables.guildIndex.get(messageReceivedEvent.getGuild()).channelIndex.get(messageReceivedEvent.getChannel()).messageStack.push(messageReceivedEvent.getMessage());
		if( overflow && Variables.guildIndex.get(messageReceivedEvent.getGuild()).channelIndex.get(messageReceivedEvent.getChannel()).messageStack.isBlackboxActive()) {
			logMessage.reactions.add(new ReactionMessage("Warning: Your blackbox queue is overflowing!", messageReceivedEvent.getChannel()));
		}
		Logger.debug("Reaction produced from " + name, 1, true);
		logMessage.reactions.add(new ReactionLoggerHear(messageReceivedEvent.getMessage().getContent(), messageReceivedEvent.getAuthor(), messageReceivedEvent.getChannel()));
		return logMessage;
	}
	
}
