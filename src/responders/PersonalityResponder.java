package responders;

import java.util.ArrayList;

import main.Brain;
import main.Constants;
import utilities.Handler;

public class PersonalityResponder implements Handler {
	
	public PersonalityResponder() {}

	@Override
	public String process(String message) {
		String response = "";
		message = message.toLowerCase();
		ArrayList<String> tokens = Brain.tp.parse(message);
		if( tokens.contains(Constants.NAME.toLowerCase()) ) {
			if( message.contains("thank you") || message.contains("thank") ) {
				response = "You're welcome.";
			} else if( message.contains("pls") ) {
				response = "I'm trying ;;";
			} else if( message.contains("gdi") || message.contains("goddammit") || message.contains("dammit") || message.contains("god damn it") ) {
				response = "<n<";
			} else if( message.contains("stfu") ) {
				response = ">m<";
			} else if( message.contains("shut up") ) {
				response = ";w;";
			} else if( tokens.contains("no") ) {
				response = "Caris YES!";
			}
		}
		return response;
	}

	@Override
	public int getPriority() {
		return 1;
	}
	
}