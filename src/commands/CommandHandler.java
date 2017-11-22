package commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import controller.Controller;
import invokers.Invoker;
import library.Constants;
import main.Brain;
import main.GuildInfo;
import memories.Memory;
import responders.Responder;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;
import tokens.Thought;
import tokens.UserData;
import utilities.BotUtils;

public class CommandHandler {

	@EventSubscriber
	public void onMessageRecieved(MessageReceivedEvent event) {
		Brain.log.debugOut("Message received: \"" + event.getMessage().getContent() + "\" from User \"" + event.getAuthor().getName() + "\" on Guild \"" + event.getGuild().getName() + "\".", 1);
		if( !Brain.guildIndex.containsKey(event.getGuild()) ) {
			Brain.guildIndex.put(event.getGuild(), new GuildInfo(event.getGuild().getName()));
			Brain.log.debugOut("Creating new Guild Object \"" + event.getGuild().getName() + "\".", 1);
		}
		GuildInfo gi = Brain.guildIndex.get(event.getGuild());
		if( !gi.userIndex.containsKey(event.getAuthor().getName()) ) {
			gi.userIndex.put(event.getAuthor().getName(), new UserData(event.getAuthor().getLongID()));
			Brain.log.debugOut("Adding new User \"" + event.getAuthor().getName() + "\" to Guild " + event.getGuild().getName() + ".", 1);
			Brain.log.debugOut(event.getAuthor().getLongID(), 1);
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

		// Name of thought, the actual thoughts
		// Later use TreeMap, sort values better
		Map< String, Thought > thoughts = new HashMap< String, Thought >();
		Brain.log.debugOut("Recording Message...");
		for( String s : Brain.memories.keySet() ) {
			Memory h = Brain.memories.get( s );
			Thought t = h.ponder(event);
			if( !t.name.isEmpty() && ( !t.text.isEmpty() /*|| !t.equals(null) */) ) {
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
		if ( messageText.startsWith( Constants.ADMIN_PREFIX ) && admin ) {
			Brain.log.debugOut("Admin detected.", 1);
			for( String s : Brain.controllerModules.keySet() ) { // try each invocation handler
				Controller h = Brain.controllerModules.get(s);
				Response r = h.process(event);
				if( r.embed ) {
					Brain.log.debugOut("Response embed option generated.", 2);
					responses.add(r);
				} if( !r.text.equals("") ) { // if this produces a result
					Brain.log.debugOut("Response option generated: \"" + r.text + "\"", 2);
					responses.add( r ); // add it to the list of potential responses
				}
				else {
					Brain.log.debugOut("No response generated.", 2);
				}
			}
		} else if( messageText.startsWith("==>") && !admin ) {
			responses.add( new Response("The \"==>\" command invoker is now deprecated. Please use \".c\" instead.", 0) );
		} else if ( messageText.startsWith( Constants.COMMAND_PREFIX ) ) { // if invoked
			Brain.log.debugOut("\tInvocation detected.");
			for( String s : Brain.invokerModules.keySet() ) { // try each invocation handler
				boolean check = gi.modules.keySet().contains(s);
				if( !check ) {
					continue;
				} else if( gi.modules.get(s) ) {
					Invoker h = Brain.invokerModules.get(s);
					Response r = h.process(event);
					if( r.embed ) {
						Brain.log.debugOut("Response embed option generated.", 2);
						responses.add(r);
					} if( !r.text.equals("") ) { // if this produces a result
						Brain.log.debugOut("Response option generated: \"" + r.text + "\"", 2);
						responses.add( r ); // add it to the list of potential responses
					}
					else {
						Brain.log.debugOut("No response generated.", 2);
					}
				}
			}
		} else { // if not being invoked
			Brain.log.debugOut("Generating automatic response.", 1);
			for( String s : Brain.responderModules.keySet() ) { // then try each auto handler
				boolean check = gi.modules.keySet().contains(s);
				if( !check ) {
					continue;
				} else if( gi.modules.get(s) ) {
					Responder h = Brain.responderModules.get(s);
					Response r = h.process(event);
					if( r.embed ) {
						Brain.log.debugOut("Response embed option generated.", 2);
						responses.add(r);
					} if( !r.text.equals("") ) { // if this produces a result
						Brain.log.debugOut("Response option generated: \"" + r.text + "\"", 2);
						responses.add( r ); // add it to the list of potential responses
					}
					else {
						Brain.log.debugOut("No response generated.", 2);
					}
				}
			}
		}
		if( responses.size() != 0 ) { // if any response exists
			Brain.log.debugOut("Selecting optimal response.", 1);
			Response[] options = new Response[responses.size()]; // create a static array of response options
			for( int f=0; f<responses.size(); f++ ) {
				options[f] = responses.get(f);
			}
			Arrays.sort(options); // sort these options
			Brain.log.debugOut("Optimal response selected.", 1);
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
			gi.userIndex.get(event.getAuthor().getName()).lastMessage = messageText;
		}
	}
}
