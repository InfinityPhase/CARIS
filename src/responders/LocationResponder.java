package responders;

import java.util.ArrayList;

import main.Brain;
import utilities.Handler;

import sx.blah.discord.handle.impl.obj.Message;

public class LocationResponder extends Handler {

	public LocationResponder() {}
	
	public String process(Message message) {
		String response = "";
		String messageText = format(message);
		ArrayList<String> tokens = Brain.tp.parse(messageText.toLowerCase());
		
		if( tokens.contains("where's") || tokens.contains("where") ) {
			for( String person : Brain.locationInvoker.people.keySet() ) {
				if( messageText.contains(person) ) {
					response = person + " is at " + Brain.locationInvoker.people.get(person) + ".";
					break;
				}
			}
			for( String token : tokens ) {
				if( Brain.locationInvoker.people.keySet().contains(token) ) {
					response = token + " is at " + Brain.locationInvoker.people.get(token) + ".";
					break;
				}
			}
		} else if( tokens.contains("who") && tokens.contains("is") && tokens.contains("at") || tokens.contains("who's") && tokens.contains("at") ) {
			for( String location : Brain.locationInvoker.locations.keySet() ) {
				if( messageText.contains(location.toString()) ) {
					if( !Brain.locationInvoker.locations.containsKey(location) ) {
						return location + " is not a set location.";
					}
					ArrayList<String> locals = Brain.locationInvoker.locations.get(location);
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
					break;
				}
			}
		}
		return response;
	}

}
