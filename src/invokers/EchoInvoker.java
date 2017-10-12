package invokers;

import java.util.ArrayList;

import main.Brain;
import main.Constants;
import utilities.Handler;

import sx.blah.discord.handle.impl.obj.Message;

public class EchoInvoker implements Handler {
	// Placeholder example invoked handler
	
	public EchoInvoker() {}
	
	@Override
	public String process(Message message) {
		String messageText = message.getContent();
		String response = "";
		ArrayList<String> tokens = Brain.tp.parse(messageText);
		tokens.pop(0);
		
		if( tokens.get(0).equals("echo") ) {
			response = tokens.get(1);
		}
		return response;
	}
	
	@Override
	public String process(String message) {
		String response = "";
		ArrayList<String> tokens = Brain.tp.parse(message);
		tokens.pop(0);
		
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
