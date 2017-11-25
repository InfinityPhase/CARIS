package invokers;

import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class EchoInvoker extends Invoker {
	// Placeholder example invoked handler

	@Override
	public Response process(MessageReceivedEvent event) {
		setup(event);
		
		if( tokens.get(0).equals("echo") ) {
			log.indent(3).log("EchoInvoker triggered.");
			response = tokens.get(1);;
		} else {
			log.indent(3).log("EchoInvoker unactivated.");
		}
		log.indent(2).log("EchoInvoker processed.");
		return build();
	}
	
}
