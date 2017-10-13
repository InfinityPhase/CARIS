package invokers;

import java.util.ArrayList;

import library.Constants;
import main.Brain;
import utilities.Handler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class EchoInvoker extends Handler {
	// Placeholder example invoked handler
	
	public EchoInvoker() {}
	
	public String process(MessageReceivedEvent event) {
		if( Constants.DEBUG ) {System.out.println("\t\t\tProcessing EchoInvoker.");}
		String response = "";
		String messageText = format(event);
		ArrayList<String> tokens = Brain.tp.parse(messageText);
		tokens.remove(0);
		
		if( tokens.get(0).equals("echo") ) {
			if( Constants.DEBUG ) {System.out.println("\t\t\t\tEchoInvoker triggered.");}
			response = tokens.get(1);;
		} else if( Constants.DEBUG ) {System.out.println("\t\t\t\tEchoInvoker unactivated.");}
		if( Constants.DEBUG ) {System.out.println("\t\t\tEchoInvoker processed.");}
		return response;
	}
	
}
