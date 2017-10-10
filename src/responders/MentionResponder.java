package responders;

import main.Constants;
import utilities.Handler;

public class MentionResponder implements Handler {
	// Placeholder example auto handler
	
	public MentionResponder() {}
	
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
