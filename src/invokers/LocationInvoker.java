package invokers;

import java.util.ArrayList;

import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class LocationInvoker extends Invoker {
		
	public Response process(MessageReceivedEvent event) {	
		setup(event);
		
		if( tokens.get(0).equals("loc") ) {
			Brain.log.debugOut("\t\t\t\tKeyword \"loc\" detected.");
			if( tokens.size() < 2 ) {
				Brain.log.debugOut("\t\t\t\t\tSyntax Error. Aborting.");
			Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
				response = "Syntax Error: Command not specified.";
				return build();
			}
			String action = tokens.get(1);
			if(action.equals("set")) {
				Brain.log.debugOut("\t\t\t\tParameter \"set\" detected.");
				if( tokens.size() < 4 ) {
					Brain.log.debugOut("\t\t\t\t\tSyntax Error. Aborting.");
					Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String place = tokens.get(2);
				Brain.log.debugOut("\t\t\t\t\t\tLocation set: \"" + place + "\".");
				ArrayList<String> persons = new ArrayList<String>();
				for( int f=3; f<tokens.size(); f++ ) {
					persons.add(tokens.get(f));
					Brain.log.debugOut("\t\t\t\t\tPerson added: \"" + tokens.get(f) + "\".");
				}
				for( String person : persons ) {
					if( variables.locations.containsKey(place) ) {
						Brain.log.debugOut("\t\t\t\t\tLocation \"" + place + "\" found.");
						if( variables.locations.get(place).contains(person) ) {
							Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
							response = person + " is already at " + place + ".";
							return build();
						} else {
							Brain.log.debugOut("\t\t\t\t\t\tNew Location \"" + place + "\" generated.");
							for( String location : variables.locations.keySet() ) {
								ArrayList<String> locals = variables.locations.get(location);
								if( locals.contains(person) ) {
									locals.remove(person);
								}
							}
							variables.locations.get(place).add(person);
							variables.people.put(person, place);
							response = person + "'s location has been set to " + place + ".";
							Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
						}
					} else {
						Brain.log.debugOut("\t\t\t\t\tRemoving previous location references.");
						for( String location : variables.locations.keySet() ) {
							ArrayList<String> locals = variables.locations.get(location);
							if( locals.contains(person) ) {
								locals.remove(person);
							}
						}
						ArrayList<String> locals = new ArrayList<String>();
						locals.add(person);
						variables.locations.put(place, locals);
						variables.people.put(person, place);
						response = person + "'s location has been set to " + place + ".";
						Brain.log.debugOut("\t\t\t\t\tLocation set successfully.");
						Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
					}
				}
				if( persons.size() > 1 ) {
					Brain.log.debugOut("\t\t\t\t\tLocation updated successfully.");
					response = "Locations have been updated.";
					Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
				}
			} else if(action.equals("get")) {
				Brain.log.debugOut("\t\t\t\tParameter \"get\" detected.");
				if( tokens.size() < 3 ) {
					Brain.log.debugOut("\t\t\t\t\tSyntax Error. Aborting.");
					Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String person = tokens.get(2);
				if( !variables.people.containsKey(person) ) {
					Brain.log.debugOut("\t\t\t\t\tLocation not found for \"" + person + "\".");
				Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
					response = person + " has not set a location.";
					return build();
				}
				String location = variables.people.get(person);
				response = person + " is at " + location + ".";
				Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
			} else if(action.equals("check")) {
				Brain.log.debugOut("\t\t\t\tParameter \"check\" detected.");
				if( tokens.size() < 3 ) {
					Brain.log.debugOut("\t\t\t\t\tSyntax Error. Aborting.");
					Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String location = tokens.get(2);
				if( !variables.locations.containsKey(location) ) {
					Brain.log.debugOut("\t\t\t\t\tLocation \"" + location + "\"invalid.");
					Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
					response = location + " is not a set location.";
					return build();
				}
				ArrayList<String> locals = variables.locations.get(location);
				if( locals.size() == 0 ) {
					Brain.log.debugOut("\t\t\t\t\tLocation \"" + location + "\" empty.");
				Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
					response = "No one is at " + location + ".";
				} else {
					Brain.log.debugOut("\t\t\t\t\tPeople detected at \"" + location + "\" successfully.");
				Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
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
				Brain.log.debugOut("\t\t\t\tParameter \"clear\" detected.");
				if( tokens.size() < 3 ) {
					Brain.log.debugOut("\t\t\t\t\tSyntax Error. Aborting.");
					Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String location = tokens.get(2);
				if( !variables.locations.containsKey(location) ) {
					Brain.log.debugOut("\t\t\t\t\tLocation \t" + location + "\" not found." );
					Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
					response = location + " is not a set location.";
					return build();
				}
				variables.locations.remove(location);
				for( String person : variables.people.keySet() ) {
					if(variables.people.get(person).equals(location)) {
						variables.people.remove(person);
					}
				}
				Brain.log.debugOut("\t\t\t\t\tLocation cleared.");
				response = "Cleared location " + location + ".";
				Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
			} else if( action.equals("reset") ) {
				Brain.log.debugOut("\t\t\t\tParameter \"reset\" detected.");
				if( tokens.size() < 3 ) {
					Brain.log.debugOut("\t\t\t\t\tSyntax Error. Aborting.");
					Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String person = tokens.get(2);
				if( !variables.people.containsKey(person) ) {
					Brain.log.debugOut("\t\t\t\t\tNo location set for \"" + person + "\".");
					Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
					response = person + " has not set a location.";
					return build();
				}
				Brain.log.debugOut("\t\t\t\t\tRemoving previous location references.");
				variables.people.remove(person);
				for( String location : variables.locations.keySet() ) {
					if( variables.locations.get(location).contains(person) ) {
						variables.locations.get(location).remove(person);
					}
				}
				Brain.log.debugOut("\t\t\t\t\tLocation reset successfully.");
				response = "Reset location for " + person + ".";
				Brain.log.debugOut("\t\t\t\t\tLocationInvoker triggered.");
			} else {
				Brain.log.debugOut("\t\t\t\tKeyword \"" + action + "\" unknown. Aborting.");
				Brain.log.debugOut("\t\t\t\tLocationInvoker triggered.");
				response = "Unrecognized command: \"" + action + "\".";
				return build();
			}
		} else {
			Brain.log.debugOut("\t\t\t\tLocationInvoker unactivated.");
		}
		Brain.log.debugOut("\t\t\tLocationInvoker processed.");
		return build();
	}

}
