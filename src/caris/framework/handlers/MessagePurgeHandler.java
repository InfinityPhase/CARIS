package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionMessageDelete;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;

public class MessagePurgeHandler extends MessageHandler {
	
	public MessagePurgeHandler() {
		super("MessagePurge Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return isMentioned() && isElevated() && StringUtilities.containsAnyOfIgnoreCase(message, "purge", "clear");
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.debug("MessagePurge detected", 2);
		ArrayList<Integer> integers = TokenUtilities.parseIntegers(message);
		MultiReaction purgeMessages = new MultiReaction(2);
		if( !integers.isEmpty() ) {
			int integer = integers.get(0);
			if( integer > 0 ) {
				purgeMessages.reactions.add(new ReactionMessageDelete(mrEvent.getChannel(), integer));
				purgeMessages.reactions.add(new ReactionMessage("Purged " + integer + " messages!", mrEvent.getChannel(), 2));
			} else {
				Logger.debug("Purge failed because incorrect number specified", 2);
				purgeMessages.reactions.add(new ReactionMessage("You need to specify a positive number!", mrEvent.getChannel()));
			}
		} else if( StringUtilities.containsIgnoreCase(message, "last") ) {
			purgeMessages.reactions.add(new ReactionMessageDelete(mrEvent.getChannel(), 2));
			purgeMessages.reactions.add(new ReactionMessage("Purged the last message!", mrEvent.getChannel(), 2));
		} else {
			Logger.debug("Purge failed because no number specified", 2);
			purgeMessages.reactions.add(new ReactionMessage("You need to specify the number of messages to delete!", mrEvent.getChannel(), 2));
		}
		Logger.debug("Response produced from " + name, 1, true);
		return purgeMessages;
	}
}
