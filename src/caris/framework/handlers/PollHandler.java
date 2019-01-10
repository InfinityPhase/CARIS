package caris.framework.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.library.Constants;
import caris.framework.library.Keywords;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionPollClose;
import caris.framework.reactions.ReactionPollOpenCreate;
import caris.framework.reactions.ReactionPollOpenStart;
import caris.framework.tokens.Poll;

public class PollHandler extends MessageHandler {
	
	public PollHandler() {
		super("Poll");
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return mentioned(messageEventWrapper) && messageEventWrapper.containsWord("poll");
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		MultiReaction manipulatePoll = new MultiReaction(2);
		ArrayList<String> quoted = messageEventWrapper.quotedTokens;
		ArrayList<Integer> integers = messageEventWrapper.integerTokens;
		if( messageEventWrapper.containsAnyWords(Keywords.POSITIVE) ) {
			if( quoted.size() == 0 ) {
				manipulatePoll.reactions.add(new ReactionMessage("You need to put the description and options in quotes!", messageEventWrapper.getChannel()));
			} else if( quoted.size() > 1 ) {
				// Jesus Christ this is going to cause so many threading issues down the line and I'm going to have no idea where it's coming from
				Poll poll = new Poll(messageEventWrapper.getAuthor(), quoted.remove(0), quoted, Variables.guildIndex.get(messageEventWrapper.getGuild()));
				manipulatePoll.reactions.add(new ReactionPollOpenCreate(poll, messageEventWrapper.getChannel()));
				manipulatePoll.reactions.add(new ReactionPollOpenStart(poll, messageEventWrapper.getChannel()));
			} else {
				Poll poll = new Poll(messageEventWrapper.getAuthor(), quoted.get(0), Variables.guildIndex.get(messageEventWrapper.getGuild()));
				manipulatePoll.reactions.add(new ReactionPollOpenCreate(poll, messageEventWrapper.getChannel()));
				manipulatePoll.reactions.add(new ReactionPollOpenStart(poll, messageEventWrapper.getChannel()));
			}
		} else if( messageEventWrapper.containsAnyWords(Keywords.NEGATIVE) ) {
			if( integers.size() == 0 ) {
				manipulatePoll.reactions.add(new ReactionMessage("You need to specify a Poll ID!", messageEventWrapper.getChannel()));
			} else {
				int number = integers.get(0);
				if( number >= Variables.guildIndex.get(messageEventWrapper.getGuild()).polls.size() ) {
					manipulatePoll.reactions.add(new ReactionMessage(number + " is not a valid Poll ID!", messageEventWrapper.getChannel()));
				} else {
					Poll poll = Variables.guildIndex.get(messageEventWrapper.getGuild()).polls.get(number);
					if( messageEventWrapper.getAuthor().equals(poll.author) || messageEventWrapper.elevatedAuthor ) {
						manipulatePoll.reactions.add(new ReactionPollClose(poll, messageEventWrapper.getChannel()));
					} else {
						manipulatePoll.reactions.add(new ReactionMessage("That isn't your poll!", messageEventWrapper.getChannel()));
					}
				}
			}
		}
		return manipulatePoll;
	}
	
	@Override
	public String getDescription() {
		return "Creates polls for users to easily vote on.";
	}
	
	@Override
	public HashMap<String, String> getUsage() {
		HashMap<String, String> usage = new HashMap<String, String>();
		usage.put(Constants.NAME + ", start a poll for \"Question\"", "Creates a yes/no poll for the given question");
		usage.put(Constants.NAME + ", start a poll for \"Question\" with options \"option1\" \"option2\" ... \"optionN\"", "Creates a poll for the given question with up to 10 options.");
		usage.put(Constants.NAME + ", end poll <#>", "Closes the poll with the given ID, and displays its results");
		return usage;
	}
}
