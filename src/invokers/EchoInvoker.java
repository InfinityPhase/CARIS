package invokers;

import java.util.ArrayList;

import main.Brain;
import main.Constants;
import utilities.Handler;

public class EchoInvoker implements Handler {
	// Placeholder example invoked handler
	
	public EchoInvoker() {}
	
	@Override
	public String process(String message) {
		String response = "";
		ArrayList<String> tokens = Brain.tp.parse(message);
		if( tokens.get(0).equals("echo") ) {
			response = tokens.get(1);
		}
		return response;
	}
	
	@Override
	public int getPriority() {
		return 0;
	}
}
