package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.InvokedHandler;
import caris.framework.reactions.MultiReaction;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionPurgeMessages;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MessageHistory;

public class MessagePurgeHandler extends InvokedHandler {
	
	public MessagePurgeHandler() {
		super("MessagePurge Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		if( !isMessageReceivedEvent(event) ) {
			return false;
		}
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		return isMentioned(messageReceivedEvent) && isAdmin(messageReceivedEvent) && StringUtilities.containsIgnoreCase(messageReceivedEvent.getMessage().getContent(), "purge") && StringUtilities.containsIgnoreCase(messageReceivedEvent.getMessage().getContent(), "message");
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.debug("MessagePurge detected", 2);
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		ArrayList<Integer> numbers = TokenUtilities.parseNumbers(messageReceivedEvent.getMessage().getContent());
		MultiReaction purgeMessages = new MultiReaction(2);
		if( !numbers.isEmpty() ) {
			int number = numbers.get(0);
			if( number > 0 ) {
				MessageHistory history = messageReceivedEvent.getChannel().getMessageHistory(number);
				purgeMessages.reactions.add(new ReactionPurgeMessages(messageReceivedEvent.getChannel(), history));
				purgeMessages.reactions.add(new ReactionMessage("Purged " + number + " messages!", messageReceivedEvent.getChannel(), 2));
			} else {
				Logger.debug("Purge failed because incorrect number specified", 2);
				purgeMessages.reactions.add(new ReactionMessage("You need to specify a positive number!", messageReceivedEvent.getChannel()));
			}
		} else {
			Logger.debug("Purge failed because no number specified", 2);
			purgeMessages.reactions.add(new ReactionMessage("You need to specify the number of messages to delete!", messageReceivedEvent.getChannel(), 2));
		}
		return purgeMessages;
	}
}
