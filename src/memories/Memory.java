package memories;

import java.time.LocalDateTime;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import tokens.Thought;
import utilities.Handler;

public class Memory extends Handler {
	// Base Memory class
	// Doesn't return a message, returns a "thought"
	// Thoughts represent info about message
	
	protected IUser author;
	protected LocalDateTime timeStamp;
	
	@Override
	protected void setup(MessageReceivedEvent event) {
		// Gets the author, and time the message was sent (Locally)
		super.setup(event);
	}
	
	protected Thought build_V2() {
		// Figure this out
		return null; 
	}

}
