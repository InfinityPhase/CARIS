package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.Logger;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;

public class CountdownHandler extends MessageHandler {

	public CountdownHandler() {
		super("Countdown", Access.DEVELOPER, false);
		description = "Counts down to kickoff!";
		usage.put(getKeyword() + " enable", "Subscribes this channel to the countdown message.");
		usage.put(getKeyword() + " disable", "Unsubscribes this channel to the countdown message.");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return isInvoked();
	}
	
	protected Reaction process(Event event) {
		ArrayList<String> tokens = TokenUtilities.parseTokens(message);
		if ( tokens.size() >= 2 ) {
			if( tokens.get(1).equalsIgnoreCase("enable") ) {
				Logger.debug("Reaction produced from " + name, 1, true);
				Brain.countdownChannels.add(mrEvent.getChannel());
				return new ReactionMessage("Countdown subscription enabled!", mrEvent.getChannel());
			} else if( tokens.get(1).equalsIgnoreCase("disable") ) {
				Logger.debug("Reaction produced from " + name, 1, true);
				if( Brain.countdownChannels.contains(mrEvent.getChannel()) ) {
					Brain.countdownChannels.remove(mrEvent.getChannel());
				}
				return new ReactionMessage("Countdown subscription disabled!", mrEvent.getChannel());
			} else {
				Logger.debug("Operation failed due to syntax error", 2);
				Logger.debug("Reaction produced from " + name, 1, true);
				return new ReactionMessage("Syntax Error! Specify \"enable\" or \"disable\"!", mrEvent.getChannel());
			}
		} else {
			Logger.debug("Operation failed due to syntax error", 2);
			Logger.debug("Reaction produced from " + name, 1, true);
			return new ReactionMessage("Syntax Error! Specify \"enable\" or \"disable\"!", mrEvent.getChannel());
		}
	}
	
}
