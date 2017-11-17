package invokers;

import java.util.ArrayList;

import library.Constants;
import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class LocationInvoker extends Invoker {

	public Response process(MessageReceivedEvent event) {	
		setup(event);

		if( tokens.get(0).equals("loc") ) {
			Brain.log.debugOut("Keyword \"loc\" detected.", 4);
			if( tokens.size() < 2 ) {
				Brain.log.debugOut("Syntax Error. Aborting.", 5);
				Brain.log.debugOut("LocationInvoker triggered.", 5);
				response = "Syntax Error: Command not specified.";
				return build();
			}
			String action = tokens.get(1);
			if(action.equals("set")) {
				Brain.log.debugOut("Parameter \"set\" detected.", 4);
				if( tokens.size() < 4 ) {
					Brain.log.debugOut("Syntax Error. Aborting.", 5);
					Brain.log.debugOut("LocationInvoker triggered.", 5);
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String place = tokens.get(2);
				Brain.log.debugOut("Location set: \"" + place + "\".", 6);
				ArrayList<String> persons = new ArrayList<String>();
				for( int f=3; f<tokens.size(); f++ ) {
					persons.add(tokens.get(f));
					Brain.log.debugOut("Person added: \"" + tokens.get(f) + "\".", 5);
				}
				for( String person : persons ) {
					if( variables.locations.containsKey(place) ) {
						Brain.log.debugOut("Location \"" + place + "\" found.", 5);
						if( variables.locations.get(place).contains(person) ) {
							Brain.log.debugOut("LocationInvoker triggered.", 5);
							response = person + " is already at " + place + ".";
							return build();
						} else {
							Brain.log.debugOut("New Location \"" + place + "\" generated.", 6);
							for( String location : variables.locations.keySet() ) {
								ArrayList<String> locals = variables.locations.get(location);
								if( locals.contains(person) ) {
									locals.remove(person);
								}
							}
							variables.locations.get(place).add(person);
							variables.people.put(person, place);
							response = person + "'s location has been set to " + place + ".";
							Brain.log.debugOut("LocationInvoker triggered.", 5);
						}
					} else {
						Brain.log.debugOut("Removing previous location references.", 5);
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
						Brain.log.debugOut("Location set successfully.", 5);
						Brain.log.debugOut("LocationInvoker triggered.", 5);
					}
				}
				if( persons.size() > 1 ) {
					Brain.log.debugOut("Location updated successfully.", 5);
					response = "Locations have been updated.";
					Brain.log.debugOut("LocationInvoker triggered.", 5);
				}
			} else if(action.equals("get")) {
				Brain.log.debugOut("Parameter \"get\" detected.", 4);
				if( tokens.size() < 3 ) {
					Brain.log.debugOut("Syntax Error. Aborting.", 5);
					Brain.log.debugOut("LocationInvoker triggered.", 5);
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String person = tokens.get(2);
				if( !variables.people.containsKey(person) ) {
					Brain.log.debugOut("Location not found for \"" + person + "\".", 5);
					Brain.log.debugOut("LocationInvoker triggered.", 5);
					response = person + " has not set a location.";
					return build();
				}
				String location = variables.people.get(person);
				response = person + " is at " + location + ".";
				Brain.log.debugOut("LocationInvoker triggered.", 5);
			} else if(action.equals("check")) {
				Brain.log.debugOut("Parameter \"check\" detected.", 4);
				if( tokens.size() < 3 ) {
					Brain.log.debugOut("Syntax Error. Aborting.", 5);
					Brain.log.debugOut("LocationInvoker triggered.", 5);
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String location = tokens.get(2);
				if( !variables.locations.containsKey(location) ) {
					Brain.log.debugOut("Location \"" + location + "\"invalid.", 5);
					Brain.log.debugOut("LocationInvoker triggered.", 5);
					response = location + " is not a set location.";
					return build();
				}
				ArrayList<String> locals = variables.locations.get(location);
				if( locals.size() == 0 ) {
					Brain.log.debugOut("Location \"" + location + "\" empty.", 5);
					Brain.log.debugOut("LocationInvoker triggered.", 5);
					response = "No one is at " + location + ".";
				} else {
					Brain.log.debugOut("People detected at \"" + location + "\" successfully.", 5);
					Brain.log.debugOut("LocationInvoker triggered.", 5);
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
				Brain.log.debugOut("Parameter \"clear\" detected.", 4);
				if( tokens.size() < 3 ) {
					Brain.log.debugOut("Syntax Error. Aborting.", 5);
					Brain.log.debugOut("LocationInvoker triggered.", 5);
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String location = tokens.get(2);
				if( !variables.locations.containsKey(location) ) {
					Brain.log.debugOut("Location \t" + location + "\" not found.", 5);
					Brain.log.debugOut("LocationInvoker triggered.", 5);
					response = location + " is not a set location.";
					return build();
				}
				variables.locations.remove(location);
				for( String person : variables.people.keySet() ) {
					if(variables.people.get(person).equals(location)) {
						variables.people.remove(person);
					}
				}
				Brain.log.debugOut("Location cleared.", 5);
				response = "Cleared location " + location + ".";
				Brain.log.debugOut("LocationInvoker triggered.", 5);
			} else if( action.equals("reset") ) {
				Brain.log.debugOut("Parameter \"reset\" detected.", 4);
				if( tokens.size() < 3 ) {
					Brain.log.debugOut("Syntax Error. Aborting.", 5);
					Brain.log.debugOut("LocationInvoker triggered.", 5);
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String person = tokens.get(2);
				if( !variables.people.containsKey(person) ) {
					Brain.log.debugOut("No location set for \"" + person + "\".", 5);
					Brain.log.debugOut("LocationInvoker triggered.", 5);
					response = person + " has not set a location.";
					return build();
				}
				Brain.log.debugOut("Removing previous location references.", 5);
				variables.people.remove(person);
				for( String location : variables.locations.keySet() ) {
					if( variables.locations.get(location).contains(person) ) {
						variables.locations.get(location).remove(person);
					}
				}
				Brain.log.debugOut("Location reset successfully.", 5);
				response = "Reset location for " + person + ".";
				Brain.log.debugOut("LocationInvoker triggered.", 5);
			} else {
				Brain.log.debugOut("Keyword \"" + action + "\" unknown. Aborting.", 4);
				Brain.log.debugOut("LocationInvoker triggered.", 4);
				response = "Unrecognized command: \"" + action + "\".";
				return build();
			}
		} else {
			Brain.log.debugOut("LocationInvoker unactivated.", 4);
		}
		Brain.log.debugOut("LocationInvoker processed.", 3);
		return build();
	}

}
