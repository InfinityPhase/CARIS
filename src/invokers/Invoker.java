package invokers;


import java.io.Serializable;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import utilities.Handler;

public class Invoker extends Handler {
	// Base Invoker class. Setup removes first token.
	
	@Override
	protected void setup(MessageReceivedEvent event) {
		super.setup(event);
		tokens.remove(0);
	}
	
	@Override
	protected void messageSetup(MessageReceivedEvent event) {
		super.messageSetup(event);
		tokens.remove(0);
	}

}
