package caris.modular.handlers;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.library.Constants;
import caris.framework.reactions.MultiReaction;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionJoinCall;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;

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
			"The capital of France is pronounced *Pare*-is, not *Par*-is!",
			"Mikki, we've been over this.",
			"Mikki please.",
			"Do you want me to start pronouncing your name *Made*-ison??",
			"You hurt me with your mispronounciations."
	};
	
	public GreetingHandler() {
		super("Greeting Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return startsWithAGreeting(message) && isMentioned();
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.debug("Greeting detected", 2);
		MultiReaction returnGreeting = new MultiReaction(0);
		if( mrEvent.getAuthor().getLongID() == Constants.MIKKI_ID ) {
			if( mrEvent.getAuthor().getVoiceStateForGuild(mrEvent.getGuild()).getChannel() != null ) {
				returnGreeting.reactions.add(new ReactionJoinCall(mrEvent.getAuthor().getVoiceStateForGuild(mrEvent.getGuild()).getChannel()));
				returnGreeting.reactions.add(new ReactionMessage("/tts It's pronounced Caris!", mrEvent.getChannel()));
			} else {
				returnGreeting.reactions.add(new ReactionMessage(getRandomCorrection(), mrEvent.getChannel(), 0));
			}
		} else {
			returnGreeting.reactions.add(new ReactionMessage(getRandomGreeting() + ", " + mrEvent.getAuthor().getDisplayName(mrEvent.getGuild()) + "!", mrEvent.getChannel(), 0));
		}
		Logger.debug("Reaction produced from " + name, 1);
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
		return (correctionOutput.length > 0) ? greetingsOutput[(int) (Math.random()*greetingsOutput.length)] : "Actually, it's pronounced *Care*-is.";
	}
	
}
