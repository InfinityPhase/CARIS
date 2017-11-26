package invokers;

import java.util.ArrayList;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class LocationInvoker extends Invoker {

	public Response process(MessageReceivedEvent event) {	
		setup(event);

		if( tokens.get(0).equals("loc") ) {
			log.indent(2).log("Keyword \"loc\" detected.");
			if( tokens.size() < 2 ) {
				log.indent(3).log("Syntax Error. Aborting.");
				log.indent(3).log("LocationInvoker triggered.");
				response = "Syntax Error: Command not specified.";
				return build();
			}
			String action = tokens.get(1);
			if(action.equals("set")) {
				log.indent(2).log("Parameter \"set\" detected.");
				if( tokens.size() < 4 ) {
					log.indent(3).log("Syntax Error. Aborting.");
					log.indent(3).log("LocationInvoker triggered.");
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String place = tokens.get(2);
				log.indent(3).log("Location set: \"" + place + "\".");
				ArrayList<String> persons = new ArrayList<String>();
				for( int f=3; f<tokens.size(); f++ ) {
					persons.add(tokens.get(f));
					log.indent(3).log("Person added: \"" + tokens.get(f) + "\".");
				}
				for( String person : persons ) {
					if( variables.locations.containsKey(place) ) {
						log.indent(3).log("Location \"" + place + "\" found.");
						if( variables.locations.get(place).contains(person) ) {
							log.indent(3).log("LocationInvoker triggered.");
							response = person + " is already at " + place + ".";
							return build();
						} else {
							log.indent(3).log("New Location \"" + place + "\" generated.");
							for( String location : variables.locations.keySet() ) {
								ArrayList<String> locals = variables.locations.get(location);
								if( locals.contains(person) ) {
									locals.remove(person);
								}
							}
							variables.locations.get(place).add(person);
							variables.people.put(person, place);
							response = person + "'s location has been set to " + place + ".";
							log.indent(3).log("LocationInvoker triggered.");
						}
					} else {
						log.indent(3).log("Removing previous location references.");
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
						log.indent(3).log("Location set successfully.");
						log.indent(3).log("LocationInvoker triggered.");
					}
				}
				if( persons.size() > 1 ) {
					log.indent(3).log("Location updated successfully.");
					response = "Locations have been updated.";
					log.indent(3).log("LocationInvoker triggered.");
				}
			} else if(action.equals("get")) {
				log.indent(2).log("Parameter \"get\" detected.");
				if( tokens.size() < 3 ) {
					log.indent(3).log("Syntax Error. Aborting.");
					log.indent(3).log("LocationInvoker triggered.");
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String person = tokens.get(2);
				if( !variables.people.containsKey(person) ) {
					log.indent(3).log("Location not found for \"" + person + "\".");
					log.log("LocationInvoker triggered.");
					response = person + " has not set a location.";
					return build();
				}
				String location = variables.people.get(person);
				response = person + " is at " + location + ".";
				log.indent(3).log("LocationInvoker triggered.");
			} else if(action.equals("check")) {
				log.indent(2).log("Parameter \"check\" detected.");
				if( tokens.size() < 3 ) {
					log.indent(3).log("Syntax Error. Aborting.");
					log.indent(3).log("LocationInvoker triggered.");
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String location = tokens.get(2);
				if( !variables.locations.containsKey(location) ) {
					log.indent(3).log("Location \"" + location + "\"invalid.");
					log.indent(3).log("LocationInvoker triggered.");
					response = location + " is not a set location.";
					return build();
				}
				ArrayList<String> locals = variables.locations.get(location);
				if( locals.size() == 0 ) {
					log.indent(3).log("Location \"" + location + "\" empty.");
					log.indent(3).log("LocationInvoker triggered.");
					response = "No one is at " + location + ".";
				} else {
					log.indent(3).log("People detected at \"" + location + "\" successfully.");
					log.indent(3).log("LocationInvoker triggered.");
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
				log.indent(2).log("Parameter \"clear\" detected.");
				if( tokens.size() < 3 ) {
					log.indent(3).log("Syntax Error. Aborting.");
					log.indent(3).log("LocationInvoker triggered.");
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String location = tokens.get(2);
				if( !variables.locations.containsKey(location) ) {
					log.indent(3).log("Location \t" + location + "\" not found.");
					log.indent(3).log("LocationInvoker triggered.");
					response = location + " is not a set location.";
					return build();
				}
				variables.locations.remove(location);
				for( String person : variables.people.keySet() ) {
					if(variables.people.get(person).equals(location)) {
						variables.people.remove(person);
					}
				}
				log.indent(3).log("Location cleared.");
				response = "Cleared location " + location + ".";
				log.indent(3).log("LocationInvoker triggered.");
			} else if( action.equals("reset") ) {
				log.indent(2).log("Parameter \"reset\" detected.");
				if( tokens.size() < 3 ) {
					log.indent(3).log("Syntax Error. Aborting.");
					log.indent(3).log("LocationInvoker triggered.");
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String person = tokens.get(2);
				if( !variables.people.containsKey(person) ) {
					log.indent(3).log("No location set for \"" + person + "\".");
					log.indent(3).log("LocationInvoker triggered.");
					response = person + " has not set a location.";
					return build();
				}
				log.indent(3).log("Removing previous location references.");
				variables.people.remove(person);
				for( String location : variables.locations.keySet() ) {
					if( variables.locations.get(location).contains(person) ) {
						variables.locations.get(location).remove(person);
					}
				}
				log.indent(3).log("Location reset successfully.");
				response = "Reset location for " + person + ".";
				log.indent(3).log("LocationInvoker triggered.");
			} else {
				log.indent(2).log("Keyword \"" + action + "\" unknown. Aborting.");
				log.indent(2).log("LocationInvoker triggered.");
				response = "Unrecognized command: \"" + action + "\".";
				return build();
			}
		} else {
			log.indent(2).log("LocationInvoker unactivated.");
		}
		log.indent(1).log("LocationInvoker processed.");
		return build();
	}

}
