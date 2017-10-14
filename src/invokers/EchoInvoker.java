package invokers;

import java.util.ArrayList;

import library.Constants;
import main.Brain;
import utilities.Handler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class EchoInvoker extends Handler {
	// Placeholder example invoked handler
	
	public EchoInvoker() {}
	
	public Response process(MessageReceivedEvent event) {
		if( Constants.DEBUG ) {System.out.println("\t\t\tProcessing EchoInvoker.");}
		String response = "";
		String messageText = format(event);
		if( Constants.DEBUG ) {System.out.println("\t\t\tFormatted message: \"" + messageText + "\"");}
		ArrayList<String> tokens = Brain.tp.parse(event.getMessage().getContent().toLowerCase());
		tokens.remove(0);
		
		if( tokens.get(0).equals("echo") ) {
			if( Constants.DEBUG ) {System.out.println("\t\t\t\tEchoInvoker triggered.");}
			response = tokens.get(1);;
		} else if( Constants.DEBUG ) {System.out.println("\t\t\t\tEchoInvoker unactivated.");}
		if( Constants.DEBUG ) {System.out.println("\t\t\tEchoInvoker processed.");}
		return build(response);
	}
	
}
