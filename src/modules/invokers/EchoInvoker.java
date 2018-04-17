package modules.invokers;

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
		name = "cEcho";
		help = "**__cEcho__**"  +
				"\nThis command is used to make CARIS say somemthing."  +
				"\nPlease don't abuse this command."  +
				"\nThere are no subcommands available."  +
				"\n"  +
				"\n```cEcho: My name is CARIS!```";
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
