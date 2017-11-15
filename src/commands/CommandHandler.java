package commands;

import java.util.ArrayList;
import java.util.Arrays;

import controller.Controller;
import invokers.Invoker;
import main.Brain;
import main.GuildInfo;
import responders.Responder;
import library.Constants;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;
import tokens.UserData;
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
		Brain.log.debugOut("\tMessage received: \"" + event.getMessage().getContent() + "\" from User \"" + event.getAuthor().getName() + "\" on Guild \"" + event.getGuild().getName() + "\".");
		if( !Brain.guildIndex.containsKey(event.getGuild()) ) {
			Brain.guildIndex.put(event.getGuild(), new GuildInfo());
			Brain.log.debugOut("\tCreating new Guild Object \"" + event.getGuild().getName() + "\".");
		}
		GuildInfo gi = Brain.guildIndex.get(event.getGuild());
		if( !gi.userIndex.containsKey(event.getAuthor().getName()) ) {
			gi.userIndex.put(event.getAuthor().getName(), new UserData(event.getAuthor()));
			Brain.log.debugOut("\tAdding new User \"" + event.getAuthor().getName() + "\" to Guild " + event.getGuild().getName() + ".");
			Brain.log.debugOut("\t"+event.getAuthor().getLongID());
		}

		String messageText = event.getMessage().getContent();
		long id = event.getAuthor().getLongID();
		boolean admin = false;
		for( long l : Constants.ADMIN_IDS ) {
			if( id == l ) {
				admin = true;
			}
		}
		
		ArrayList<Response> responses = new ArrayList<Response>();

		// Checks if a message begins with the bot command prefix
		if ( messageText.startsWith( Constants.ADMIN_PREFIX ) && admin ) {
			Brain.log.debugOut("\tAdmin detected.");
			for( String s : Brain.controllerModules.keySet() ) { // try each invocation handler
				boolean check = gi.modules.keySet().contains(s);
				if( !check ) {
					continue;
				} else if( gi.modules.get(s) ) {
					Controller h = Brain.controllerModules.get(s);
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
			}
		}
		if ( messageText.startsWith( Constants.COMMAND_PREFIX ) ) { // if invoked
			Brain.log.debugOut("\tInvocation detected.");
			for( String s : Brain.invokerModules.keySet() ) { // try each invocation handler
				boolean check = gi.modules.keySet().contains(s);
				if( !check ) {
					continue;
				} else if( gi.modules.get(s) ) {
					Invoker h = Brain.invokerModules.get(s);
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
			}
		} else { // if not being invoked
			Brain.log.debugOut("\tGenerating automatic response.");
			for( String s : Brain.responderModules.keySet() ) { // then try each auto handler
				boolean check = gi.modules.keySet().contains(s);
				if( !check ) {
					continue;
				} else if( gi.modules.get(s) ) {
					Responder h = Brain.responderModules.get(s);
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
