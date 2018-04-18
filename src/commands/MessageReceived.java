package commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.Constants;
import library.Variables;
import main.Brain;
import main.GuildInfo;
import memories.Memory;
import modules.Handler.Avalibility;
import modules.constructors.Constructor;
import modules.controllers.Controller;
import modules.invokers.Invoker;
import modules.responders.Responder;
import modules.tools.Tool;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
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
		boolean blacklisted = false;
		boolean notWhitelistedAndShouldBe = false; // True if there is a whitelist, and this channel is not on it.

		log.log("Message received: \"" + event.getMessage().getContent() + "\" from User \"" + event.getAuthor().getName() + "\" on Guild \"" + event.getGuild().getName() + "\".");	

		if( Variables.guildIndex.get( event.getGuild() ).blacklist.contains( event.getChannel() ) ){
			blacklisted = true;
			log.log("Channel is on the blacklist, command set reduced");
		}
		
		if( !Variables.guildIndex.get( event.getGuild() ).whitelist.isEmpty() && !Variables.guildIndex.get( event.getGuild() ).whitelist.contains( event.getChannel() ) ) {
			notWhitelistedAndShouldBe = true;
			log.log("Channel is not on the whitelist, command set reduced");
		}
		
		if( event.getAuthor().isBot() && !Constants.RESPOND_TO_BOT ) {
			log.indent(2).log("Message is from a bot, ignoring");
			return; // Stops checking the message, ignoring any possible commands
		}

		GuildInfo gi = Variables.guildIndex.get(event.getGuild());

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
		if( !Constants.MEMORY_RESPECT_LIST || ( !blacklisted && !notWhitelistedAndShouldBe ) ) {
			log.log("Recording Message...");
			for( String s : Brain.memoryModules.keySet() ) {
				Memory h = Brain.memoryModules.get( s );
				Thought t = h.remember(event);

				if( !t.name.isEmpty() && ( !t.text.isEmpty() /*|| !t.equals(null) */) ) {
					thoughts.put( t.name, t );
				}
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
				if( ( !blacklisted && !notWhitelistedAndShouldBe ) || h.avalibility == Avalibility.ALWAYS ) {
					Response r = h.process(event);
					if( r.embed ) {
						log.indent(1).log("Response embed option generated.");
						responses.add(r);
					} if( !r.text.equals("") ) { // if this produces a result
						log.indent(1).log("Response option generated: \"" + r.text + "\"");
						responses.add( r ); // add it to the list of potential responses
					} else {
						log.indent(1).log("No response generated.");
					}
				}
			}
		} else if( messageText.startsWith( Constants.ADMIN_PREFIX ) && !admin ) {
			responses.add( new Response("Please stop trying to abuse me.", 0) );
		} else if( startsWithOneOf( messageText, Variables.toolPrefixes ) ) {
			log.log("Tool Usage detected.");
			for( String s : Brain.toolModules.keySet() ) {
				Tool t = Brain.toolModules.get(s);
				log.indent(1).log("Checking " + s);
				if( t.prefix.equalsIgnoreCase( getPrefix(event) ) && ( !blacklisted && !notWhitelistedAndShouldBe ) || t.avalibility == Avalibility.ALWAYS ) {
					if( event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(Permissions.ADMINISTRATOR) ) {
						log.indent(10).log(blacklisted);
						log.indent(2).log("Prefix match found");
						Response r = t.process(event);
						if( r.embed ) {
							log.indent(3).log("Response embed option generated.");
						}
						responses.add(r);
					} else {
						Response r = new Response("You need to be an admin to do that!", 0);
						responses.add(r);
					}
				}else {
					log.indent(2).log("Prefix does not match");
					continue;
				}
			}
		} else if ( startsWithOneOf( messageText, Variables.commandPrefixes ) ) { // if invoked
			log.log("Invocation detected.");
			for( String s : Brain.invokerModules.keySet() ) {
				Invoker i = Brain.invokerModules.get(s);
				log.indent(1).log("Checking " + s);
				if( i.prefix.equalsIgnoreCase( getPrefix(event) ) && ( ( !blacklisted && !notWhitelistedAndShouldBe ) || i.avalibility == Avalibility.ALWAYS ) ) {
					log.indent(10).log(blacklisted);
					log.indent(2).log("Prefix match found");
					Response r = i.process(event);
					if( r.embed ) {
						log.indent(3).log("Response embed option generated.");
						responses.add(r);
					}
					
					if( !r.text.equals("") ) { // if this produces a result
						log.indent(3).log("Response option generated: \"" + r.text + "\"");
						responses.add( r ); // add it to the list of potential responses
					}
				} else {
					log.indent(2).log("Prefix does not match");
					continue;
				}
			}
			for( String s : Brain.constructorModules.keySet() ) {
				Constructor co = Brain.constructorModules.get(s);
				log.indent(1).log("Checking " + s);
				if( co.prefix.equalsIgnoreCase( getPrefix(event) ) && ( ( !blacklisted && !notWhitelistedAndShouldBe ) || co.avalibility == Avalibility.ALWAYS ) ) {
					log.indent(10).log(blacklisted);
					log.indent(2).log("Prefix match found");
					Response r = co.process(event);
					if( r.embed ) {
						log.indent(3).log("Response embed option generated.");
						responses.add(r);
					}
					
					if( !r.text.equals("") ) { // if this produces a result
						log.indent(3).log("Response option generated: \"" + r.text + "\"");
						responses.add( r ); // add it to the list of potential responses
					}
				} else {
					log.indent(2).log("Prefix does not match");
					continue;
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
					if( ( !blacklisted && !notWhitelistedAndShouldBe ) || h.avalibility == Avalibility.ALWAYS ) {
						Response r = h.process(event);
						if( r.embed ) {
							log.indent(1).log("Response embed option generated.");
							responses.add(r);
						} if( !r.text.equals("") ) { // if this produces a result
							log.indent(1).log("Response option generated: \"" + r.text + "\"");
							responses.add( r ); // add it to the list of potential responses
						} else {
							log.indent(1).log("No response generated.");
						}
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

	public static boolean startsWithOneOf( String s, String[] prefixes ) {
		for( String prefix : prefixes ) {
			if( s.toLowerCase().startsWith(prefix.toLowerCase()) ) {
				return true;
			}
		}
		return false;
	}

	public static boolean startsWithOneOf( String s, List<String> prefixes ) {
		return startsWithOneOf( s, prefixes.toArray( new String[ prefixes.size() ] ) );
	}

	public static boolean isOneOf( String s, String[] texts ) {
		for( String text: texts ) {
			if( s.equalsIgnoreCase(text) ) {
				return true;
			}
		}
		return false;
	}

	public static String getPrefix( String line ) {
		line += " ";
		int id = line.indexOf(":");
		if( id == -1 ) {
			id = line.indexOf(" ");
		}
		return line.substring(0, id);
	}

	public static String getPrefix( IMessage message ) {
		return getPrefix( message.getContent() );
	}

	public static String getPrefix( MessageReceivedEvent event ) {
		return getPrefix( event.getMessage().getContent() );
	}
}
