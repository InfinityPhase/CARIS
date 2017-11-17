package invokers;


import java.io.Serializable;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import utilities.Handler;

public class Invoker extends Handler implements Serializable {
	// Base Invoker class. Setup removes first token.
	
	/* This is a magic ID. Used to ID when we can restore states */
	private static final long serialVersionUID = -113505923518244286L;

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
