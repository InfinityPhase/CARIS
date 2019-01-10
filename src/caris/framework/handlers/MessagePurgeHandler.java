package caris.framework.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.library.Constants;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionMessageDelete;

public class MessagePurgeHandler extends MessageHandler {
	
	public MessagePurgeHandler() {
		super("MessagePurge", Access.ADMIN);
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return mentioned(messageEventWrapper) && messageEventWrapper.containsAnyWords("purge", "clear");
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		ArrayList<Integer> integers = messageEventWrapper.integerTokens;
		MultiReaction purgeMessages = new MultiReaction(2);
		if( !integers.isEmpty() ) {
			int integer = integers.get(0);
			if( integer > 0 ) {
				purgeMessages.reactions.add(new ReactionMessageDelete(messageEventWrapper.getChannel(), integer));
				purgeMessages.reactions.add(new ReactionMessage("Purged " + integer + " messages!", messageEventWrapper.getChannel(), 2));
			} else {
				purgeMessages.reactions.add(new ReactionMessage("You need to specify a positive number!", messageEventWrapper.getChannel()));
			}
		} else if( messageEventWrapper.containsWord("last") ) {
			purgeMessages.reactions.add(new ReactionMessageDelete(messageEventWrapper.getChannel(), 2));
			purgeMessages.reactions.add(new ReactionMessage("Purged the last message!", messageEventWrapper.getChannel(), 2));
		} else {
			purgeMessages.reactions.add(new ReactionMessage("You need to specify the number of messages to delete!", messageEventWrapper.getChannel(), 2));
		}
		return purgeMessages;
	}
	
	@Override
	public String getDescription() {
		return "Deletes messages in bulk.";
	}
	
	@Override
	public HashMap<String, String> getUsage() {
		HashMap<String, String> usage = new HashMap<String, String>();
		usage.put(Constants.NAME + ", purge <#> messages", "Clears the last number of messages specified");
		usage.put(Constants.NAME + ", purge the last message", "Clears the last message sent");
		return usage;
	}
}
