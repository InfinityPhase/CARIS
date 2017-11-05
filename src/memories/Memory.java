package memories;

import java.util.ArrayList;
import java.util.List;

import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import tokens.Thought;
import utilities.Handler;

public class Memory extends Handler {
	// Base Memory class
	// Doesn't return a message, returns a "thought"
	// Thoughts represent info about message
		
	protected List<String> text = new ArrayList<String>();
	protected IMessage message;
	protected String name;
		
	@Override
	protected void setup(MessageReceivedEvent event) {
		super.setup(event);
		text.clear();
		message = null;
	}
	
	protected Thought think() {
		if( text.isEmpty() ) {
			if( message != null ) {
				return new Thought(message, name);
			} else {
				Brain.log.debugOut("Nothing to think about");
				return null;
			}
		} else {
			if( message != null ) {
				return new Thought(text, message, name);
			} else {
				return new Thought(text, name);
			}
		}
	}

}
