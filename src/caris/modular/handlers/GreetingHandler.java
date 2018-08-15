package caris.modular.handlers;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.reactions.Reaction;
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
		Logger.debug("Reaction produced from " + name, 1);
		return new ReactionMessage(getRandomGreeting() + ", " + mrEvent.getAuthor().getDisplayName(mrEvent.getGuild()) + "!", mrEvent.getChannel(), 0);
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
	
}
