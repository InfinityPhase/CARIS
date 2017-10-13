package invokers;

import java.util.ArrayList;
import java.util.HashMap;

import main.Brain;
import library.Constants;
import utilities.Handler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Message;

public class LocationInvoker extends Handler {
	
	public LocationInvoker() {}
	
	public String process(MessageReceivedEvent event) {		
		String response = "";
		String messageText = format(event);
		ArrayList<String> tokens = Brain.tp.parse(messageText);
		tokens.remove(0);
		
		if( tokens.get(0).equals("loc") ) {
			if( tokens.size() < 2 ) {
				return "Syntax Error: Command not specified.";
			}
			String action = tokens.get(1);
			if(action.equals("set")) {
				if( tokens.size() < 4 ) {
					return "Syntax Error: Insufficient parameters.";
				}
				String place = tokens.get(2);
				ArrayList<String> persons = new ArrayList<String>();
				for( int f=3; f<tokens.size(); f++ ) {
					persons.add(tokens.get(f));
				}
				for( String person : persons ) {
					if( Brain.channelIndex.get(event.getChannel()).locations.containsKey(place) ) {
						if( Brain.channelIndex.get(event.getChannel()).locations.get(place).contains(person) ) {
							return person + " is already at " + place + ".";
						} else {
							for( String location : Brain.channelIndex.get(event.getChannel()).locations.keySet() ) {
								ArrayList<String> locals = Brain.channelIndex.get(event.getChannel()).locations.get(location);
								if( locals.contains(person) ) {
									locals.remove(person);
								}
							}
							Brain.channelIndex.get(event.getChannel()).locations.get(place).add(person);
							Brain.channelIndex.get(event.getChannel()).people.put(person, place);
							response = person + "'s location has been set to " + place + ".";
						}
					} else {
						for( String location : Brain.channelIndex.get(event.getChannel()).locations.keySet() ) {
							ArrayList<String> locals = Brain.channelIndex.get(event.getChannel()).locations.get(location);
							if( locals.contains(person) ) {
								locals.remove(person);
							}
						}
						ArrayList<String> locals = new ArrayList<String>();
						locals.add(person);
						Brain.channelIndex.get(event.getChannel()).locations.put(place, locals);
						Brain.channelIndex.get(event.getChannel()).people.put(person, place);
						response = person + "'s location has been set to " + place + ".";
					}
				}
				if( persons.size() > 1 ) {
					response = "Locations have been updated.";
				}
			} else if(action.equals("get")) {
				if( tokens.size() < 3 ) {
					return "Syntax Error: Insufficient parameters.";
				}
				String person = tokens.get(2);
				if( !Brain.channelIndex.get(event.getChannel()).people.containsKey(person) ) {
					return person + " has not set a location.";
				}
				String location = Brain.channelIndex.get(event.getChannel()).people.get(person);
				response = person + " is at " + location + ".";
			} else if(action.equals("check")) {
				if( tokens.size() < 3 ) {
					return "Syntax Error: Insufficient parameters.";
				}
				String location = tokens.get(2);
				if( !Brain.channelIndex.get(event.getChannel()).locations.containsKey(location) ) {
					return location + " is not a set location.";
				}
				ArrayList<String> locals = Brain.channelIndex.get(event.getChannel()).locations.get(location);
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
			} else if( action.equals("clear") ) {
				if( tokens.size() < 3 ) {
					return "Syntax Error: Insufficient parameters.";
				}
				String location = tokens.get(2);
				if( !Brain.channelIndex.get(event.getChannel()).locations.containsKey(location) ) {
					return location + " is not a set location.";
				}
				Brain.channelIndex.get(event.getChannel()).locations.remove(location);
				for( String person : Brain.channelIndex.get(event.getChannel()).people.keySet() ) {
					if(Brain.channelIndex.get(event.getChannel()).people.get(person).equals(location)) {
						Brain.channelIndex.get(event.getChannel()).people.remove(person);
					}
				}
				response = "Cleared location " + location + ".";
			} else if( action.equals("reset") ) {
				if( tokens.size() < 3 ) {
					return "Syntax Error: Insufficient parameters.";
				}
				String person = tokens.get(2);
				if( !Brain.channelIndex.get(event.getChannel()).people.containsKey(person) ) {
					return person + " has not set a location.";
				}
				Brain.channelIndex.get(event.getChannel()).people.remove(person);
				for( String location : Brain.channelIndex.get(event.getChannel()).locations.keySet() ) {
					if( Brain.channelIndex.get(event.getChannel()).locations.get(location).contains(person) ) {
						Brain.channelIndex.get(event.getChannel()).locations.get(location).remove(person);
					}
				}
				response = "Reset location for " + person + ".";
			} else {
				return "Unrecognized command: \"" + action + "\".";
			}
		}
		return response;
	}

}
