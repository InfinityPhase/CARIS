package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Constants;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionMessageDelete;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;

public class SayHandler extends MessageHandler {

	public SayHandler() {
		super("Say", Access.ADMIN, false);
		description = "Forces " + Constants.NAME + " to say something.";
		usage.put(getInvocation() + " \"message\"", "Makes " + Constants.NAME + " say the given message");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return isMentioned() && StringUtilities.containsAnyOfIgnoreCase(message, "say", "repeat");
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.debug("Say detected", 2);
		ArrayList<String> quoted = TokenUtilities.parseQuoted(message);
		MultiReaction puppet = new MultiReaction(1);
		puppet.reactions.add(new ReactionMessageDelete(mrEvent.getChannel(), mrEvent.getMessage()));
		if( quoted.size() != 0 ) {
			puppet.reactions.add(new ReactionMessage(quoted.get(0), mrEvent.getChannel()));
		}
		Logger.debug("Response produced from " + name, 1, true);
		return puppet;
	}
	
	
}
