package commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.Constants;
import main.Brain;
import main.GuildInfo;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;
import tokens.Thought;
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
		Brain.log.debugOut("\tMessage received: \"" + event.getMessage().getContent() + "\" from User \"" + event.getAuthor().getName() + "\" on Guild \"" + event.getGuild().getName() + "\".");
		if( !Brain.guildIndex.containsKey(event.getGuild()) ) {
			Brain.guildIndex.put(event.getGuild(), new GuildInfo());
			Brain.log.debugOut("\tCreating new Guild Object \"" + event.getGuild().getName() + "\".");
		}
		GuildInfo gi = Brain.guildIndex.get(event.getGuild());
		if( !gi.userIndex.containsKey(event.getAuthor().getName()) ) {
			gi.userIndex.put(event.getAuthor().getName(), new UserData(event.getAuthor()));
			Brain.log.debugOut("\tAdding new User \"" + event.getAuthor().getName() + "\" to Guild " + event.getGuild().getName() + ".");
		}

		String messageText = event.getMessage().getContent();

		ArrayList<Response> responses = new ArrayList<Response>();

		// Name of thought, the actual thoughts
		// Later use TreeMap, sort values better
		Map< String, Thought > thoughts = new HashMap< String, Thought >();

		// Time to think
		for( Handler h : Brain.memories ) {
			// Think
			Brain.log.debugOut("Recording Message...");
			Thought t = h.ponder(event);
			if( !t.name.isEmpty() && ( !t.text.isEmpty() || !t.equals(null) ) ) {
				thoughts.put( t.name, t );
			} else {
				Brain.log.debugOut("No thought generated");
			}
		}

		if( !thoughts.isEmpty() ) {
			// Say what you think
			for( String name : thoughts.keySet() ) {
				// Compile thoughts in order
				for( int i = 0; i < Constants.THOUGHT_ORDER.length; i++ ) {
					if( Constants.THOUGHT_ORDER[i].equalsIgnoreCase( name ) ) {
						// Write to file
						// Check if text array exists, and iterate over it
						// Then, check for a message and print the text
						for( int j = 0; j < thoughts.get(name).text.size(); j++ ) {
							Brain.log.out( thoughts.get(name).text.get( j ) );
							
						}
					}
				}
			}
		} else {
			Brain.log.debugOut("Nothing to think about");
		}

		// Checks if a message begins with the bot command prefix
		if ( messageText.startsWith( Constants.PREFIX ) ) { // if invoked
			Brain.log.debugOut("\tInvocation detected.");
			for( Handler h : Brain.invokers ) { // try each invocation handler
				// TODO: Try to optimize this later
				Response r = h.process(event);
				if( r.embed ) {
					Brain.log.debugOut("\t\tResponse embed option generated.");
					responses.add(r);
				} if( !r.text.equals("") ) { // if this produces a result
					Brain.log.debugOut("\t\tResponse option generated: \"" + r.text + "\"");
					responses.add( r ); // add it to the list of potential responses
				}
				else {
					Brain.log.debugOut("\t\tNo response generated.");
				}
			}
		} else { // if not being invoked
			Brain.log.debugOut("\tGenerating automatic response.");
			for( Handler h : Brain.responders ) { // then try each auto handler
				// TODO: Try to optimize this later.
				Response r = h.process( event ); // process individual handler
				if( r.embed ) {
					Brain.log.debugOut("\t\tResponse embed option generated.");
					responses.add(r);
				} else if( !r.text.equals("") ) { // if this produces a result
					Brain.log.debugOut("\t\tResponse option generated: \"" + r.text + "\"");
					responses.add( r ); // add it to the list of potential responses
				}
				else {
					Brain.log.debugOut("\t\tNo response generated.");
				}
			}
		}
		if( responses.size() != 0 ) { // if any response exists
			if( Constants.DEBUG ) {System.out.println("\tSelecting optimal response.");
			Response[] options = new Response[responses.size()]; // create a static array of response options
			for( int f=0; f<responses.size(); f++ ) {
				options[f] = responses.get(f);
			}
			Arrays.sort(options); // sort these options
			Brain.log.debugOut("\tOptimal response selected.");
			if( options[0].embed ) {
				event.getChannel().sendMessage(options[0].builder.build());
			} else {
				if( options[0].text.startsWith("Nickname set to ") ) {
					int index1 = options[0].text.indexOf('\"') + 1;
					int index2 = options[0].text.lastIndexOf('\"');
					event.getGuild().setUserNickname(event.getAuthor(), options[0].text.substring(index1, index2));
				}
				BotUtils.sendMessage( event.getChannel(), options[0].text ); // print out highest priority response option 
			}
			} else {
				Brain.log.debugOut("\tNo responses available.");
			}
			gi.userIndex.get(event.getAuthor().getName()).lastMessage = messageText;
		}
	}
}
