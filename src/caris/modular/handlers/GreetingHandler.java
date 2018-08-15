package caris.modular.handlers;

import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.InvokedHandler;
import caris.framework.library.Constants;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class GreetingHandler extends InvokedHandler {
	
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
		if( !isMessageReceivedEvent(event) ) {
			return false;
		}
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		return startsWithAGreeting(messageReceivedEvent.getMessage().getContent()) && isMentioned(messageReceivedEvent);
	}
	
	@Override
	protected Reaction process(Event event) {
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		Logger.debug("Reaction produced from " + name, 1);
		return new ReactionMessage(getRandomGreeting() + ", " + messageReceivedEvent.getAuthor().getDisplayName(messageReceivedEvent.getGuild()) + "!", messageReceivedEvent.getChannel(), 0);
	}
	
	private boolean startsWithAGreeting(String message) {
		Logger.debug("Greeting detected", 2);
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
