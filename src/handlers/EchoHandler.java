package handlers;

import main.Constants;

public class EchoHandler implements Handler {
	// Placeholder example invoked handler
	
	public EchoHandler() {}
	
	@Override
	public String process(String message) {
		String response = "";
		if( message.startsWith(Constants.PREFIX + "echo ") ) {
			response = message.substring(5);
		}
		return response;
	}
	
	@Override
	public int getPriority() {
		return 0;
	}
}
