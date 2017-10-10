package commands;

import java.util.ArrayList;
import java.util.Arrays;

import handlers.Handler;
import main.Brain;
import main.Constants;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;
import utilities.BotUtils;

public class CommandHandler {
	
	// Deals with events
	
	// You know what? Ignore this crap. I don't care right now.
	// Go to https://github.com/decyg/d4jexamplebot/blob/master/src/main/java/com/github/decyg/CommandHandler.java
	// When you do.
	/*
	// Maps command strings to the command implementations
	// So, a command becomes functional
	private static Map<String, Command> commandMap = new HashMap<>();
	
	// Hum. We now deviate from the tutorial, because I do my own thing
	// Also, I don't want to add more dependencies. Eh.
	
	// Populate commandMap with functionality
	// From tutorial:
	// "Might be better practise to do this from an instantiated objects constructor"
	static {
		// TODO: STUFF HERE
	}
	*/
	
	@EventSubscriber
	public void onMessageRecieved(MessageReceivedEvent event) {
		String message = event.getMessage().getContent();
		ArrayList<Response> responses = new ArrayList<Response>();
		
		// Checks if a message begins with the bot command prefix
		if (event.getMessage().getContent().startsWith(Constants.PREFIX)) { // if invoked
			for( Handler h : Brain.invokedHandlers ) { // try each invocation handler
				String text = h.process(message); // process individual invocation handler
				if( !text.equals("") ) { // if this produces a result
					responses.add(new Response(text, h.getPriority())); // add it to the list of potential responses
				}
			}
		} else { // if not being invoked
			for( Handler h : Brain.autoHandlers ) { // then try each auto handler
				String text = h.process(message); // process individual handler
				if( !text.equals("") ) { // if this produces a result
					responses.add(new Response(text, h.getPriority())); // add it to the list of potential responses
				}
			}
		}
		if( responses.size() != 0 ) { // if any response exists
			Response[] options = new Response[responses.size()]; // create a static array of response options
			Arrays.sort(options); // sort these options
			BotUtils.sendMessage(event.getChannel(), options[0].text); // print out highest priority response option 
		}
	}
}
