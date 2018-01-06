package commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import controller.Controller;
import invokers.Invoker;
import library.Constants;
import library.Variables;
import main.Brain;
import main.GuildInfo;
import main.UserInfo;
import memories.Memory;
import responders.Responder;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import tokens.Response;
import tokens.Thought;
import utilities.BotUtils;
import utilities.Logger;

public class MessageReceived extends SuperEvent {
	private Logger log = new Logger().setDefaultIndent(1).build();

	@EventSubscriber
	@Override
	public void onMessageReceived( MessageReceivedEvent event ) {
		IChannel recipient = event.getChannel();
		log.log("Message received: \"" + event.getMessage().getContent() + "\" from User \"" + event.getAuthor().getName() + "\" on Guild \"" + event.getGuild().getName() + "\".");		
		
		GuildInfo gi = Variables.guildIndex.get(event.getGuild());
		if( !gi.userIndex.containsKey( event.getAuthor() ) ) {
			gi.userIndex.put( event.getAuthor(), new UserInfo( event.getAuthor() ) );
			log.log("Adding new User \"" + event.getAuthor().getName() + "\" to Guild " + event.getGuild().getName() + ".");
			log.log( event.getAuthor().getLongID() );
		}

		String messageText = event.getMessage().getContent();
		long id = event.getAuthor().getLongID();
		boolean admin = false;
		for( long l : Constants.ADMIN_IDS ) {
			if( id == l ) {
				admin = true; // TODO: Store this per user, per guild
			}
		}

		ArrayList<Response> responses = new ArrayList<Response>();

		// Name of thought, the actual thoughts
		// Later use TreeMap, sort values better
		Map< String, Thought > thoughts = new HashMap< String, Thought >();
		log.log("Recording Message...");
		for( String s : Brain.memoryModules.keySet() ) {
			Memory h = Brain.memoryModules.get( s );
			Thought t = h.remember(event);

			if( !t.name.isEmpty() && ( !t.text.isEmpty() /*|| !t.equals(null) */) ) {
				thoughts.put( t.name, t );
			}
		}

		if( !thoughts.isEmpty() ) {
			// Say what you think
			for( String name : thoughts.keySet() ) {
				// Compile thoughts in order
				for( int i = 0; i < Constants.THOUGHT_ORDER.length; i++ ) {
					if( Constants.THOUGHT_ORDER[i].equalsIgnoreCase( name ) ) {
						for( int j = 0; j < thoughts.get(name).text.size(); j++ ) {
							log.indent(2).log( thoughts.get(name).text.get( j ) );
						}
					}
				}
			}
		} else {
			log.indent(0).log("Nothing to think about");
		}

		// Checks if a message begins with the bot command prefix
		if ( messageText.startsWith( Constants.ADMIN_PREFIX ) && admin ) {
			log.log("Admin detected.");
			for( String s : Brain.controllerModules.keySet() ) { // try each invocation handler
				Controller h = Brain.controllerModules.get(s);
				Response r = h.process(event);
				if( r.embed ) {
					log.indent(1).log("Response embed option generated.");
					responses.add(r);
				} if( !r.text.equals("") ) { // if this produces a result
					log.indent(1).log("Response option generated: \"" + r.text + "\"");
					responses.add( r ); // add it to the list of potential responses
				}
				else {
					log.indent(1).log("No response generated.");
				}
			}
		} else if( messageText.startsWith("==>") && !admin ) {
			responses.add( new Response("Please stop trying to abuse me.", 0) );
		} else if ( messageText.startsWith( Constants.COMMAND_PREFIX ) ) { // if invoked
			log.log("Invocation detected.");
			for( String s : Brain.invokerModules.keySet() ) { // try each invocation handler
				boolean check = gi.modules.keySet().contains(s);
				if( !check ) {
					continue;
				} else if( gi.modules.get(s) ) {
					Invoker h = Brain.invokerModules.get(s);
					Response r = h.process(event);
					if( r.embed ) {
						log.indent(1).log("Response embed option generated.");
						responses.add(r);
					} if( !r.text.equals("") ) { // if this produces a result
						log.indent(1).log("Response option generated: \"" + r.text + "\"");
						responses.add( r ); // add it to the list of potential responses
					}
					else {
						log.indent(1).log("No response generated.");
					}
				}
			}
		} else { // if not being invoked
			log.log("Generating automatic response.");
			for( String s : Brain.responderModules.keySet() ) { // then try each auto handler
				boolean check = gi.modules.keySet().contains(s);
				if( !check ) {
					continue;
				} else if( gi.modules.get(s) ) {
					Responder h = Brain.responderModules.get(s);
					Response r = h.process(event);
					if( r.embed ) {
						log.indent(1).log("Response embed option generated.");
						responses.add(r);
					} if( !r.text.equals("") ) { // if this produces a result
						log.indent(1).log("Response option generated: \"" + r.text + "\"");
						responses.add( r ); // add it to the list of potential responses
					}
					else {
						log.indent(1).log("No response generated.");
					}
				}
			}
		}
		if( responses.size() != 0 ) { // if any response exists
			log.log("Selecting optimal response.");
			Response[] options = new Response[responses.size()]; // create a static array of response options
			for( int f=0; f<responses.size(); f++ ) {
				options[f] = responses.get(f);
			}
			Arrays.sort(options); // sort these options
			log.log("Optimal response selected.");
			if( options[0].embed ) {
				BotUtils.sendMessage( recipient, options[0].builder );
			} else {
				if( options[0].text.startsWith("Nickname set to ") ) {
					int index1 = options[0].text.indexOf('\"') + 1;
					int index2 = options[0].text.lastIndexOf('\"');
					event.getGuild().setUserNickname(event.getAuthor(), options[0].text.substring(index1, index2));
				}
				if( options[0].proxy ) {
					recipient = options[0].recipient;
				}
				BotUtils.sendMessage( recipient, options[0].text ); // print out highest priority response option 
			}
			gi.userIndex.get( event.getAuthor() ).lastMessage = event.getMessage();
		}
	}
}
