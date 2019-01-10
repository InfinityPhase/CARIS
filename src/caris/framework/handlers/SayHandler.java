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

public class SayHandler extends MessageHandler {

	public SayHandler() {
		super("Say", Access.ADMIN);
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return mentioned(messageEventWrapper) && messageEventWrapper.containsAnyWords("say", "repeat");
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		ArrayList<String> quoted = messageEventWrapper.quotedTokens;
		MultiReaction puppet = new MultiReaction(1);
		puppet.reactions.add(new ReactionMessageDelete(messageEventWrapper.getChannel(), messageEventWrapper.getMessage()));
		if( quoted.size() != 0 ) {
			puppet.reactions.add(new ReactionMessage(quoted.get(0), messageEventWrapper.getChannel()));
		}
		return puppet;
	}
	
	@Override
	public String getDescription() {
		return "Forces " + Constants.NAME + " to say something.";
	}
	
	@Override
	public HashMap<String, String> getUsage() {
		HashMap<String, String> usage = new HashMap<String, String>();
		usage.put(Constants.NAME + ", say \"message\"", "Makes " + Constants.NAME + " say the given message");
		return usage;
	}
	
}
