package caris.modular.handlers;

import caris.framework.handlers.Handler;
import caris.framework.library.Constants;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionMessage;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class GreetingHandler extends Handler {
	
	private String[] greetingsInput = new String[] {
			"Hello",
			"Hi ",
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
			"Sup ",
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
			"Sup ",
			"'Sup",
			"Salutations",
			"Greetings",
			"Hiya",
	};
	
	public GreetingHandler() {
		super("Greeting Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		if( event instanceof MessageReceivedEvent ) {
			return startsWithAGreeting(((MessageReceivedEvent) event).getMessage().getContent()) && ((MessageReceivedEvent) event).getMessage().getContent().toLowerCase().contains(Constants.NAME.toLowerCase());
		} else {
			return false;
		}
	}
	
	@Override
	protected Reaction process(Event event) {
		return new ReactionMessage(getRandomGreeting() + ", " + ((MessageReceivedEvent) event).getAuthor().getDisplayName(((MessageReceivedEvent) event).getGuild()) + "!", ((MessageReceivedEvent) event).getChannel());
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
		return (greetingsOutput.length > 0) ? greetingsOutput[(int) Math.random()*greetingsOutput.length] : "Hello";
	}
	
}
