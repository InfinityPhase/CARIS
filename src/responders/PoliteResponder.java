package responders;

import java.util.ArrayList;

import main.Brain;
import main.Constants;
import utilities.Handler;

import sx.blah.discord.handle.impl.obj.Message;

public class PoliteResponder implements Handler {
	
	public PoliteResponder() {}

	@Override
	public String process(Message message) {
		String messageText = message.getContent();
		
		String response = "";
		messageText = messageText.toLowerCase();
		ArrayList<String> tokens = Brain.tp.parse(messageText);
		if( tokens.contains(Constants.NAME.toLowerCase()) ) {
			if( messageText.contains("thank you") ) {
				response = "You're welcome.";
			} else if( messageText.contains("pls") ) {
				response = "I'm trying ;;";
			} else if( messageText.contains("gdi") || messageText.contains("goddammit") || messageText.contains("dammit") || messageText.contains("god damn it") ) {
				response = "<n<";
			} else if( messageText.contains("stfu") ) {
				response = ">m<";
			} else if( messageText.contains("shut up") ) {
				response = ";w;";
			}
		}
		return response;
	}

	@Override
	public int getPriority() {
		return 1;
	}
	
	
	
}
