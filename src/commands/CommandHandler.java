package commands;

import java.util.ArrayList;
import java.util.Arrays;

import main.Brain;
import main.GuildInfo;
import library.Constants;
import library.Variables;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
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
		if( Constants.DEBUG ) {System.out.println("\tMessage received: \"" + event.getMessage().getContent() + "\" from User \"" + event.getAuthor().getName() + "\" on Guild \"" + event.getGuild().getName() + "\".");}
		if( !Variables.guildIndex.containsKey(event.getGuild()) ) {
			Variables.guildIndex.put(event.getGuild(), new GuildInfo());
			if( Constants.DEBUG ) {System.out.println("\tCreating new Guild Object \"" + event.getGuild().getName() + "\".");}
		}
		GuildInfo gi = Variables.guildIndex.get(event.getGuild());
		if( !gi.userIndex.containsKey(event.getAuthor().getName()) ) {
			gi.userIndex.put(event.getAuthor().getName(), new UserData(event.getAuthor()));
			if( Constants.DEBUG ) {System.out.println("\tAdding new User \"" + event.getAuthor().getName() + "\" to Guild " + event.getGuild().getName() + ".");}
		}
		
		String messageText = event.getMessage().getContent();
		
		ArrayList<Response> responses = new ArrayList<Response>();
		
		// Checks if a message begins with the bot command prefix
		if ( messageText.startsWith( Constants.PREFIX ) ) { // if invoked
			if( Constants.DEBUG ) {System.out.println("\tInvocation detected.");}
			for( Handler h : Brain.invokers ) { // try each invocation handler
				// TODO: Try to optimize this later
				Response r = h.process(event);
				if( r.embed ) {
					if( Constants.DEBUG ) {System.out.println("\t\tResponse embed option generated.");}
					responses.add(r);
				} if( !r.text.equals("") ) { // if this produces a result
					if( Constants.DEBUG ) {System.out.println("\t\tResponse option generated: \"" + r.text + "\"");}
					responses.add( r ); // add it to the list of potential responses
				}
				else if( Constants.DEBUG ) {System.out.println("\t\tNo response generated.");}
			}
		} else { // if not being invoked
			if( Constants.DEBUG ) {System.out.println("\tGenerating automatic response.");}
			for( Handler h : Brain.responders ) { // then try each auto handler
				// TODO: Try to optimize this later.
				Response r = h.process( event ); // process individual handler
				if( r.embed ) {
					if( Constants.DEBUG ) {System.out.println("\t\tResponse embed option generated.");}
					responses.add(r);
				} else if( !r.text.equals("") ) { // if this produces a result
					if( Constants.DEBUG ) {System.out.println("\t\tResponse option generated: \"" + r.text + "\"");}
					responses.add( r ); // add it to the list of potential responses
				}
				else if( Constants.DEBUG ) {System.out.println("\t\tNo response generated.");}
			}
		}
		if( responses.size() != 0 ) { // if any response exists
			if( Constants.DEBUG ) {System.out.println("\tSelecting optimal response.");}
			Response[] options = new Response[responses.size()]; // create a static array of response options
			for( int f=0; f<responses.size(); f++ ) {
				options[f] = responses.get(f);
			}
			Arrays.sort(options); // sort these options
			if( Constants.DEBUG ) {System.out.println("\tOptimal response selected.");}
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
			if( Constants.DEBUG ) {System.out.println("\tNo responses available.");}
		}
		gi.userIndex.get(event.getAuthor().getName()).lastMessage = messageText;
	}

}
