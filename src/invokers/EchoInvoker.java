package invokers;

import main.Constants;
import utilities.Handler;

public class EchoInvoker implements Handler {
	// Placeholder example invoked handler
	
	public EchoInvoker() {}
	
	@Override
	public String process(String message) {
		String response = "";
		if( message.startsWith(Constants.PREFIX + "echo ") ) {
			response = message.substring(Constants.PREFIX.length() + 5);
		}
		return response;
	}
	
	@Override
	public int getPriority() {
		return 0;
	}
}
