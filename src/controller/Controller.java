package controller;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import utilities.Handler;

public class Controller extends Handler {
	// Base Controller class. Can only be invoked by admin. Setup removes first token.
	
		@Override
		protected void setup(MessageReceivedEvent event) {
			super.setup(event);
			tokens.remove(0);
		}
}
