package handlers;

import main.Constants;

public class MentionHandler implements Handler {
	// Placeholder example auto handler
	
	public MentionHandler() {}
	
	@Override
	public String process(String message) {
		String response = "";
		if( message.toLowerCase().contains(Constants.NAME.toLowerCase()) ) {
			response = "I have been summoned.";
		}
		return response;
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
