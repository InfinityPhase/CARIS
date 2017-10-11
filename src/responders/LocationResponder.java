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
			for( String token : tokens ) {
				if( Brain.locationInvoker.people.keySet().contains(token) ) {
					response = token + " is at " + Brain.locationInvoker.people.get(token) + ".";
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
