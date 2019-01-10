package caris.modular.handlers;

import java.util.HashMap;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.library.Constants;
import caris.framework.reactions.ReactionMessage;

public class GreetingHandler extends MessageHandler {
	
	private String[] greetingsInput = new String[] {
			"Hello",
			"Hi ",
			"Hi,",
			"Howdy",
			"G'day",
			"Gday",
			"Good morn",
			"Good even",
			"Good aftern",
			"Good day",
			"Morning",
			"Evening",
			"Afternoon",
			"Hey",
			"Yo ",
			"Yo,",
			"Sup ",
			"Sup,",
			"'Sup",
			"Salutations",
			"Greetings",
			"Hiya",
			"Hi-ya",
			"What's up",
			"Whats up"
	};
	
	private String[] greetingsOutput = new String[] {
			"Hello",
			"Hi",
			"Hey",
			"Salutations",
			"Greetings",
			"Hiya",
	};
	
	private String[] correctionOutput = new String[] {
			"Actually, it's pronounced *Care*-is.",
			"The capital of France is pronounced *Pair* Is, not *Par* Is!",
			"Mikki, we've been over this.",
			"Mikki please.",
			"Do you want me to start pronouncing your name *Made*-ison??",
			"You hurt me with your mispronounciations."
	};
	
	public GreetingHandler() {
		super("Greeting");
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return startsWithAGreeting(messageEventWrapper.message) && mentioned(messageEventWrapper);
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		MultiReaction returnGreeting = new MultiReaction(0);
		if( messageEventWrapper.getAuthor().getLongID() == Constants.MIKKI_ID ) {
			returnGreeting.reactions.add(new ReactionMessage(getRandomCorrection(), messageEventWrapper.getChannel(), 0));
		} else {
			returnGreeting.reactions.add(new ReactionMessage(getRandomGreeting() + ", " + messageEventWrapper.getAuthor().getDisplayName(messageEventWrapper.getGuild()) + "!", messageEventWrapper.getChannel(), 0));
		}
		return returnGreeting;
	}
	
	private boolean startsWithAGreeting(String message) {
		for( String greeting : greetingsInput ) {
			if( message.toLowerCase().startsWith(greeting.toLowerCase()) ) {
				return true;
			}
		}
		return false;
	}
	
	private String getRandomGreeting() {
		return (greetingsOutput.length > 0) ? greetingsOutput[(int) (Math.random()*greetingsOutput.length)] : "Hello";
	}
	
	private String getRandomCorrection() {
		return (correctionOutput.length > 0) ? correctionOutput[(int) (Math.random()*correctionOutput.length)] : "Actually, it's pronounced *Care*-is.";
	}
	
	@Override
	public String getDescription() {
		return "Makes " + Constants.NAME + " say hi back to you!";
	}
	
	@Override
	public HashMap<String, String> getUsage() {
		HashMap<String, String> usage = new HashMap<String, String>();
		usage.put("Hello " + Constants.NAME + "!", "Produces a random greeting");
		return usage;
	}
	
}
