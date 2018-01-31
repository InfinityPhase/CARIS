package invokers;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class EchoInvoker extends Invoker {
	// Placeholder example invoked handler

	public EchoInvoker() {
		this( Status.ENABLED ); // This should almost always be ENABLED, unless you are testing something.
	}

	public EchoInvoker( Status status ) {
		log.log("Initializing Echo Invoker");
		this.status = status;
		prefix = "cEcho";
		name = "Echo Invoker";
	}

	@Override
	public Response process(MessageReceivedEvent event) {
		conditionalSetup(event);

		if( setupType == Setup.TOKEN ) {
			response = remainder(tokens.get(0));
		} else if( setupType == Setup.MESSAGE ) {
			response = message;
		}

		log.indent(1).log("EchoInvoker processed.");
		return build();
	}

}
