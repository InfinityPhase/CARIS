package responders;

import java.util.ArrayList;

import main.Brain;
import utilities.Handler;

public class LocationResponder implements Handler {

	public LocationResponder() {}
	
	@Override
	public String process(String message) {
		String response = "";
		message.toLowerCase();
		ArrayList<String> tokens = Brain.tp.parse(message.toLowerCase());
		message = " " + message + " ";
		if( tokens.contains("where's") || tokens.contains("where") ) {
			for( String person : Brain.locationInvoker.people.keySet() ) {
				if( message.contains(person) ) {
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
				if( message.contains(location) ) {
					response = Brain.locationInvoker.process("location check " + location);
					break;
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
