package invokers;

import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class EchoInvoker extends Invoker {
	// Placeholder example invoked handler
		
	public Response process(MessageReceivedEvent event) {
		setup(event);
		
		if( tokens.get(0).equals("echo") ) {
			Brain.log.debugOut("\t\t\t\tEchoInvoker triggered.");
			response = tokens.get(1);;
		} else {
			Brain.log.debugOut("\t\t\t\tEchoInvoker unactivated.");
		}
		Brain.log.debugOut("\t\t\tEchoInvoker processed.");
		return build();
	}
	
}
