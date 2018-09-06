package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Keywords;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionPollClose;
import caris.framework.reactions.ReactionPollOpenStart;
import caris.framework.reactions.ReactionPollOpenCreate;
import caris.framework.tokens.Poll;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;

public class PollHandler extends MessageHandler {
	
	public PollHandler() {
		super("Poll Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return isMentioned() && StringUtilities.containsIgnoreCase(message, "poll");
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.print("Poll detected!", 2);
		MultiReaction manipulatePoll = new MultiReaction(2);
		ArrayList<String> quoted = TokenUtilities.parseQuoted(message);
		ArrayList<Integer> numbers = TokenUtilities.parseNumbers(message);
		if( StringUtilities.containsAnyOfIgnoreCase(message, Keywords.POSITIVE) ) {
			if( quoted.size() == 0 ) {
				Logger.debug("Operation failed due to syntax error", 2);
				manipulatePoll.reactions.add(new ReactionMessage("You need to put the description and options in quotes!", mrEvent.getChannel()));
			} else if( quoted.size() > 1 ) {
				// Jesus Christ this is going to cause so many threading issues down the line and I'm going to have no idea where it's coming from
				Poll poll = new Poll(mrEvent.getAuthor(), quoted.remove(0), quoted, Variables.guildIndex.get(mrEvent.getGuild()));
				manipulatePoll.reactions.add(new ReactionPollOpenCreate(poll, mrEvent.getChannel()));
				manipulatePoll.reactions.add(new ReactionPollOpenStart(poll, mrEvent.getChannel()));
			} else {
				Poll poll = new Poll(mrEvent.getAuthor(), quoted.get(0), Variables.guildIndex.get(mrEvent.getGuild()));
				manipulatePoll.reactions.add(new ReactionPollOpenCreate(poll, mrEvent.getChannel()));
				manipulatePoll.reactions.add(new ReactionPollOpenStart(poll, mrEvent.getChannel()));
			}
		} else if( StringUtilities.containsAnyOfIgnoreCase(message, Keywords.NEGATIVE) ) {
			if( numbers.size() == 0 ) {
				Logger.debug("Operation failed due to syntax error", 2);
				manipulatePoll.reactions.add(new ReactionMessage("You need to specify a Poll ID!", mrEvent.getChannel()));
			} else {
				int number = numbers.get(0);
				if( number >= Variables.guildIndex.get(mrEvent.getGuild()).polls.size() ) {
					Logger.debug("Operation failed due to syntax error", 2);
					manipulatePoll.reactions.add(new ReactionMessage(number + " is not a valid Poll ID!", mrEvent.getChannel()));
				} else {
					Poll poll = Variables.guildIndex.get(mrEvent.getGuild()).polls.get(number);
					if( mrEvent.getAuthor().equals(poll.author) || isElevated() ) {
						manipulatePoll.reactions.add(new ReactionPollClose(poll, mrEvent.getChannel()));
					} else {
						Logger.debug("Operation failed due to permission error", 2);
						manipulatePoll.reactions.add(new ReactionMessage("That isn't your poll!", mrEvent.getChannel()));
					}
				}
			}
		}
		Logger.debug("Response produced from " + name, 1, true);
		return manipulatePoll;
	}
}
