package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.InvokedHandler;
import caris.framework.library.Variables;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionMessage;
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
		if( !numbers.isEmpty() ) {
			int number = numbers.get(0);
			if( number > 0 ) {
				MessageHistory history = messageReceivedEvent.getChannel().getMessageHistory(number);
				int count = history.bulkDelete().size();
				Logger.print("Deleted " + count + " messages from (" + messageReceivedEvent.getChannel().getLongID() + ") <" + messageReceivedEvent.getChannel().getName() + ">", 2);
				return new ReactionMessage("Purged " + count + " messages!", messageReceivedEvent.getChannel(), 2);
			} else {
				Logger.debug("Purge failed because incorrect number specified", 2);
				return new ReactionMessage("You need to specify a positive number!", messageReceivedEvent.getChannel(), 2);
			}
		} else {
			Logger.debug("Purge failed because no number specified", 2);
			return new ReactionMessage("You need to specify the number of messages to delete!", messageReceivedEvent.getChannel(), 2);
		}
	}
}
