package caris.framework.handlers;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.reactions.ReactionHear;
import caris.framework.reactions.ReactionMessageLog;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class MessageLoggerHandler extends MessageHandler {
	
	public MessageLoggerHandler() {
		super("MessageLogger Handler", true);
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return event instanceof MessageReceivedEvent;
	}
	
	@Override
	protected Reaction process(Event event) {
		MultiReaction logMessage = new MultiReaction(-1);
		Logger.debug("Message detected", 2);
		logMessage.reactions.add(new ReactionHear(mrEvent.getMessage().getFormattedContent(), mrEvent.getAuthor(), mrEvent.getChannel()));
		logMessage.reactions.add(new ReactionMessageLog(mrEvent.getChannel(), mrEvent.getMessage()));
		Logger.debug("Response produced from " + name, 1, true);
		return logMessage;
	}
	
}
