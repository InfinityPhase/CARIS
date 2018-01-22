package invokers;


import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import utilities.Handler;

public class Invoker extends Handler {
	// Base Invoker class.
	
	@Override
	protected void tokenSetup(MessageReceivedEvent event) {
		super.tokenSetup(event);
	}
	
	@Override
	protected void messageSetup(MessageReceivedEvent event) {
		super.messageSetup(event);
	}

}
