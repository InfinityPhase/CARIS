package responders;

import java.util.ArrayList;

import main.Brain;
import utilities.Handler;

import sx.blah.discord.handle.impl.obj.Message;

public class LocationResponder implements Handler {

	public LocationResponder() {}
	
	@Override
	public String process(Message message) {
		String messageText = message.getContent();
		
		String response = "";
		messageText = messageText.toLowerCase();
		ArrayList<String> tokens = Brain.tp.parse(messageText.toLowerCase());
		tokens.pop(0);

		messageText = " " + messageText + " ";
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
					response = Brain.locationInvoker.process("loc check " + location);
					break;
				}
			}
		}
		return response;
	}

	@Override
	public String process(String message) {		
		String response = "";
		message = message.toLowerCase();
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
					response = Brain.locationInvoker.process("loc check " + location);
					break;
				}
			}
		}
		return response;
	}
	
	@Override
	public int getPriority() {
		return 0;
	}

}
