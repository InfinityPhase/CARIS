package invokers;

import library.Constants;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class EchoInvoker extends Invoker {
	// Placeholder example invoked handler
		
	/* This is a magic ID. Used to ID when we can restore states */
	private static final long serialVersionUID = -1614802225751128805L;

	public Response process(MessageReceivedEvent event) {
		setup(event);
		
		if( tokens.get(0).equals("echo") ) {
			if( Constants.DEBUG ) {System.out.println("\t\t\t\tEchoInvoker triggered.");}
			response = tokens.get(1);;
		} else if( Constants.DEBUG ) {System.out.println("\t\t\t\tEchoInvoker unactivated.");}
		if( Constants.DEBUG ) {System.out.println("\t\t\tEchoInvoker processed.");}
		return build();
	}
	
}
