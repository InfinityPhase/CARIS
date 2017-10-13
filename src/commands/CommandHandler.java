package commands;

import java.util.ArrayList;
import java.util.Arrays;

import main.Brain;
import main.ChannelInfo;
import library.Constants;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Message;
import sx.blah.discord.handle.obj.IUser;
import tokens.Response;
import tokens.UserData;
import utilities.BotUtils;
import utilities.Handler;

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
		if( !Brain.channelIndex.containsKey(event.getChannel()) ) {
			Brain.channelIndex.put(event.getChannel(), new ChannelInfo());
		}
		if( !Brain.channelIndex.get(event.getChannel()).userIndex.containsKey(event.getAuthor().getName()) ) {
			Brain.channelIndex.get(event.getChannel()).userIndex.put(event.getAuthor().getName(), new UserData(event.getAuthor()));
		}
		
		String messageText = event.getMessage().getContent();
		
		ArrayList<Response> responses = new ArrayList<Response>();
		
		// Checks if a message begins with the bot command prefix
		if ( messageText.startsWith( Constants.PREFIX ) ) { // if invoked
			for( Handler h : Brain.invokers ) { // try each invocation handler
				// TODO: Try to optimize this later
				String text = h.process( event ); // process individual invocation handler
				if( !text.equals("") ) { // if this produces a result
					responses.add( new Response( text, h.getPriority() ) ); // add it to the list of potential responses
				}
			}
		} else { // if not being invoked
			for( Handler h : Brain.responders ) { // then try each auto handler
				// TODO: Try to optimize this later.
				String text = h.process( event ); // process individual handler
				if( !text.equals("") ) { // if this produces a result
					responses.add( new Response( text, h.getPriority() ) ); // add it to the list of potential responses
				}
			}
		}
		if( responses.size() != 0 ) { // if any response exists
			Response[] options = new Response[responses.size()]; // create a static array of response options
			for( int f=0; f<responses.size(); f++ ) {
				options[f] = responses.get(f);
			}
			Arrays.sort(options); // sort these options
			BotUtils.sendMessage( event.getChannel(), options[0].text ); // print out highest priority response option 
		}
	}

}
