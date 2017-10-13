package invokers;

import java.util.ArrayList;

import main.Brain;
import library.Constants;
import utilities.Handler;
import tokens.UserData;

import sx.blah.discord.handle.impl.obj.Message;

public class EchoInvoker extends Handler {
	// Placeholder example invoked handler
	
	public EchoInvoker() {}
	
	public String process(Message message) {
		String response = "";
		String messageText = format(message);
		ArrayList<String> tokens = Brain.tp.parse(messageText);
		tokens.remove(0);
		
		if( tokens.get(0).equals("echo") ) {
			response = tokens.get(1);;
		}
		return response;
	}
	
}
