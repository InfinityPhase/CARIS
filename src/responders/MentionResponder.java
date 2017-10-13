package responders;

import java.util.ArrayList;

import main.Brain;
import library.Constants;
import utilities.Handler;

import sx.blah.discord.handle.impl.obj.Message;

public class MentionResponder implements Handler {
	// Placeholder example auto handler
	
	public MentionResponder() {}
	
	@Override
	public String process(Message message) {
		String messageText = message.getContent();
		
		String response = "";
		messageText = messageText.toLowerCase();
		ArrayList<String> tokens = Brain.tp.parse(messageText);
		if( tokens.contains(Constants.NAME.toLowerCase()) ) {
			response = "What is it?";
		}
		return response;
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
