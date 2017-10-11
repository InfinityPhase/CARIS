package invokers;

import java.util.ArrayList;
import java.util.HashMap;

import main.Brain;
import main.Constants;
import utilities.Handler;

public class LocationInvoker implements Handler {
	
	public HashMap<String, ArrayList<String>> locations;
	public HashMap<String, String> people;
	
	public LocationInvoker() {
		locations = new HashMap<String, ArrayList<String>>();
		people = new HashMap<String, String>();
	}
	
	@Override
	public String process(String message) {
		String response = "";
		ArrayList<String> tokens = Brain.tp.parse(message);
		if( tokens.get(0).equals("location") ) {
			if( tokens.size() < 2 ) {
				return "Syntax Error: Command not specified.";
			}
			String action = tokens.get(1);
			if(action.equals("set")) {
				if( tokens.size() < 4 ) {
					return "Syntax Error: Insufficient parameters.";
				}
				String person = tokens.get(2);
				String place = tokens.get(3);
				if( locations.containsKey(place) ) {
					if( locations.get(place).contains(person) ) {
						return person + " is already at " + place + ".";
					} else {
						for( String location : locations.keySet() ) {
							ArrayList<String> locals = locations.get(location);
							if( locals.contains(person) ) {
								locals.remove(person);
							}
						}
						locations.get(place).add(person);
						people.put(person, place);
						response = person + "'s location has been set to " + place + ".";
					}
				} else {
					for( String location : locations.keySet() ) {
						ArrayList<String> locals = locations.get(location);
						if( locals.contains(person) ) {
							locals.remove(person);
						}
					}
					ArrayList<String> locals = new ArrayList<String>();
					locals.add(person);
					locations.put(place, locals);
					people.put(person, place);
					response = person + "'s location has been set to " + place + ".";
				}
			} else if(action.equals("get")) {
				if( tokens.size() < 3 ) {
					return "Syntax Error: Insufficient parameters.";
				}
				String person = tokens.get(2);
				if( !people.containsKey(person) ) {
					return person + " has not set a location.";
				}
				String location = people.get(person);
				response = person + " is at " + location + ".";
			} else if(action.equals("check")) {
				if( tokens.size() < 3 ) {
					return "Syntax Error: Insufficient parameters.";
				}
				String location = tokens.get(2);
				if( !locations.containsKey(location) ) {
					return location + " is not a set location.";
				}
				ArrayList<String> locals = locations.get(location);
				if( locals.size() == 0 ) {
					response = "No one is at " + location + ".";
				} else {
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
			} else {
				return "Unrecognized command: \"" + action + "\".";
			}
		}
		return response;
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
