package invokers;

import java.util.ArrayList;
import java.util.HashMap;

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
		if( message.startsWith(Constants.PREFIX + "location ") ) {
			String command = message.substring(Constants.PREFIX.length() + 9);
			int index = command.indexOf(" ");
			if( index == -1 || index == command.length()-1 ) {
				return "Syntax Error.";
			} else {
				String action = command.substring(0, index);
				command = command.substring(index+1);
				if(action.equals("set")) {
					String person = "";
					String location = "";
					int i2 = command.indexOf(" ");
					if( i2 == -1 || i2 == command.length()-1 ) {
						return "Syntax Error.";
					}
					person = command.substring(0, i2);
					if( person.length() == 0 ) {
						return "Syntax Error.";
					}
					command = command.substring(i2+1);
					int i3 = command.indexOf(" ");
					if( i3 == -1 || i3 == command.length()-1 ) {
						return "Syntax Error.";
					}
					location = command.substring(0, i3);
					if( location.length() == 0 ) {
						return "Syntax Error.";
					}
					if( locations.containsKey(location) ) {
						if( locations.get(location).contains(person) ) {
							return person + " is already at " + location + ".";
						} else {
							locations.get(location).add(person);
							people.put(person, location);
						}
					} else {
						ArrayList<String> attendees = new ArrayList<String>();
						attendees.add(person);
						locations.put(location, attendees);
					}
				} else if(action.equals("get")) {
					String person = "";
					int i2 = command.indexOf(" ");
					if( i2 == -1 || i2 == command.length()-1 ) {
						return "Syntax Error.";
					}
					person = command.substring(0, i2);
					if( person.length() == 0 ) {
						return "Syntax Error.";
					}
					if( !people.containsKey(person) ) {
						return person + " has not set a location.";
					}
					String location = people.get(person);
					response = person + " is at " + location + ".";
				} else if(action.equals("check")) {
					String location = "";
					int i2 = command.indexOf(" ");
					if( i2 == -1 || i2 == command.length()-1 ) {
						return "Syntax Error.";
					}
					location = command.substring(0, i2);
					if( location.length() == 0 ) {
						return "Syntax Error.";
					}
					if( !locations.containsKey(location) ) {
						return location + " is not a set location.";
					}
					ArrayList<String> people = locations.get(location);
					response = "The following people are at " + location + ": ";
					for( String person : people ) {
						response += person + " ";
					}
				} else {
					return "Unrecognized command: \"" + action + "\".";
				}
			}
		}
		return response;
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

}
