package modules.controllers;

import modules.Handler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Controller extends Handler {
	// Base Controller class. Can only be invoked by admin. Setup removes first token.

	@Override
	protected void tokenSetup(MessageReceivedEvent event) {
		super.tokenSetup(event);
		tokens.remove(0);
	}

	@Override
	protected void messageSetup(MessageReceivedEvent event) {
		super.messageSetup(event);
		tokens.remove(0);
	}
}
