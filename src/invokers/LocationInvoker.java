package invokers;

import java.util.ArrayList;

import library.Constants;
import main.Brain;
import utilities.Handler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class LocationInvoker extends Handler {
	
	public LocationInvoker() {}
	
	public Response process(MessageReceivedEvent event) {	
		if( Constants.DEBUG ) {System.out.println("\t\t\tProcessing LocationInvoker.");}
		String response = "";
		String messageText = format(event);
		if( Constants.DEBUG ) {System.out.println("\t\t\tFormatted message: \"" + messageText + "\"");}
		ArrayList<String> tokens = Brain.tp.parse(event.getMessage().getContent().toLowerCase());
		tokens.remove(0);
		
		if( tokens.get(0).equals("loc") ) {
			if( Constants.DEBUG ) {System.out.println("\t\t\t\tKeyword \"loc\" detected.");}
			if( tokens.size() < 2 ) {
				if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tSyntax Error. Aborting.");}
				if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
				return build("Syntax Error: Command not specified.");
			}
			String action = tokens.get(1);
			if(action.equals("set")) {
				if( Constants.DEBUG ) {System.out.println("\t\t\t\tParameter \"set\" detected.");}
				if( tokens.size() < 4 ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tSyntax Error. Aborting.");}
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
					return build("Syntax Error: Insufficient parameters.");
				}
				String place = tokens.get(2);
				if( Constants.DEBUG ) {System.out.println("\t\t\t\t\t\tLocation set: \"" + place + "\".");}
				ArrayList<String> persons = new ArrayList<String>();
				for( int f=3; f<tokens.size(); f++ ) {
					persons.add(tokens.get(f));
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tPerson added: \"" + tokens.get(f) + "\".");}
				}
				for( String person : persons ) {
					if( Brain.guildIndex.get(event.getGuild()).locations.containsKey(place) ) {
						if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation \"" + place + "\" found.");}
						if( Brain.guildIndex.get(event.getGuild()).locations.get(place).contains(person) ) {
							if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
							return build(person + " is already at " + place + ".");
						} else {
							if( Constants.DEBUG ) {System.out.println("\t\t\t\t\t\tNew Location \"" + place + "\" generated.");}
							for( String location : Brain.guildIndex.get(event.getGuild()).locations.keySet() ) {
								ArrayList<String> locals = Brain.guildIndex.get(event.getGuild()).locations.get(location);
								if( locals.contains(person) ) {
									locals.remove(person);
								}
							}
							Brain.guildIndex.get(event.getGuild()).locations.get(place).add(person);
							Brain.guildIndex.get(event.getGuild()).people.put(person, place);
							response = person + "'s location has been set to " + place + ".";
							if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
						}
					} else {
						if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tRemoving previous location references.");}
						for( String location : Brain.guildIndex.get(event.getGuild()).locations.keySet() ) {
							ArrayList<String> locals = Brain.guildIndex.get(event.getGuild()).locations.get(location);
							if( locals.contains(person) ) {
								locals.remove(person);
							}
						}
						ArrayList<String> locals = new ArrayList<String>();
						locals.add(person);
						Brain.guildIndex.get(event.getGuild()).locations.put(place, locals);
						Brain.guildIndex.get(event.getGuild()).people.put(person, place);
						response = person + "'s location has been set to " + place + ".";
						if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation set successfully.");}
						if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
					}
				}
				if( persons.size() > 1 ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation updated successfully.");}
					response = "Locations have been updated.";
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
				}
			} else if(action.equals("get")) {
				if( Constants.DEBUG ) {System.out.println("\t\t\t\tParameter \"get\" detected.");}
				if( tokens.size() < 3 ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tSyntax Error. Aborting.");}
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
					return build("Syntax Error: Insufficient parameters.");
				}
				String person = tokens.get(2);
				if( !Brain.guildIndex.get(event.getGuild()).people.containsKey(person) ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation not found for \"" + person + "\".");}
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
					return build(person + " has not set a location.");
				}
				String location = Brain.guildIndex.get(event.getGuild()).people.get(person);
				response = person + " is at " + location + ".";
				if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
			} else if(action.equals("check")) {
				if( Constants.DEBUG ) {System.out.println("\t\t\t\tParameter \"check\" detected.");}
				if( tokens.size() < 3 ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tSyntax Error. Aborting.");}
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
					return build("Syntax Error: Insufficient parameters.");
				}
				String location = tokens.get(2);
				if( !Brain.guildIndex.get(event.getGuild()).locations.containsKey(location) ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation \"" + location + "\"invalid.");}
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
					return build(location + " is not a set location.");
				}
				ArrayList<String> locals = Brain.guildIndex.get(event.getGuild()).locations.get(location);
				if( locals.size() == 0 ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation \"" + location + "\" empty.");}
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
					response = "No one is at " + location + ".";
				} else {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tPeople detected at \"" + location + "\" successfully.");}
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
					response = "The following people are at " + location + ": ";
					for( int f=0; f<locals.size(); f++ ) {
						response += locals.get(f);
						if( f < locals.size()-1 ) {
							response += ", ";
						} else {
							response += ".";
						}
					}
				}
			} else if( action.equals("clear") ) {
				if( Constants.DEBUG ) {System.out.println("\t\t\t\tParameter \"clear\" detected.");}
				if( tokens.size() < 3 ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tSyntax Error. Aborting.");}
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
					return build("Syntax Error: Insufficient parameters.");
				}
				String location = tokens.get(2);
				if( !Brain.guildIndex.get(event.getGuild()).locations.containsKey(location) ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation \t" + location + "\" not found." );}
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
					return build(location + " is not a set location.");
				}
				Brain.guildIndex.get(event.getGuild()).locations.remove(location);
				for( String person : Brain.guildIndex.get(event.getGuild()).people.keySet() ) {
					if(Brain.guildIndex.get(event.getGuild()).people.get(person).equals(location)) {
						Brain.guildIndex.get(event.getGuild()).people.remove(person);
					}
				}
				if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation cleared.");}
				response = "Cleared location " + location + ".";
				if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
			} else if( action.equals("reset") ) {
				if( Constants.DEBUG ) {System.out.println("\t\t\t\tParameter \"reset\" detected.");}
				if( tokens.size() < 3 ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tSyntax Error. Aborting.");}
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
					return build("Syntax Error: Insufficient parameters.");
				}
				String person = tokens.get(2);
				if( !Brain.guildIndex.get(event.getGuild()).people.containsKey(person) ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tNo location set for \"" + person + "\".");}
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
					return build(person + " has not set a location.");
				}
				if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tRemoving previous location references.");}
				Brain.guildIndex.get(event.getGuild()).people.remove(person);
				for( String location : Brain.guildIndex.get(event.getGuild()).locations.keySet() ) {
					if( Brain.guildIndex.get(event.getGuild()).locations.get(location).contains(person) ) {
						Brain.guildIndex.get(event.getGuild()).locations.get(location).remove(person);
					}
				}
				if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation reset successfully.");}
				response = "Reset location for " + person + ".";
				if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationInvoker triggered.");}
			} else {
				if( Constants.DEBUG ) {System.out.println("\t\t\t\tKeyword \"" + action + "\" unknown. Aborting.");}
				if( Constants.DEBUG ) {System.out.println("\t\t\t\tLocationInvoker triggered.");}
				return build("Unrecognized command: \"" + action + "\".");
			}
		} else if( Constants.DEBUG ) {System.out.println("\t\t\t\tLocationInvoker unactivated.");}
		if( Constants.DEBUG ) {System.out.println("\t\t\tLocationInvoker processed.");}
		return build(response);
	}

}
