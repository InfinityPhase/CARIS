package memories;

import java.util.ArrayList;
import java.util.List;

import main.Brain;
import main.GuildInfo;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import tokens.Thought;
import utilities.Handler;

public class Memory extends Handler {
	// Base Memory class
	// Doesn't return a message, returns a "thought"
	// Thoughts represent info about message
		
	protected List<String> text = new ArrayList<String>();
	protected IMessage message;
		
	@Override
	protected void setup(MessageReceivedEvent event) {
		super.setup(event);
		text.clear();
		message = null;
	}
	
	protected Thought think( List<String> text ) {
		return new Thought(text); 
	}
	
	protected Thought think( IMessage message ) {
		return new Thought(message);
	}
	
	protected Thought think( List<String> text, IMessage message ) {
		return new Thought(text, message);
	}
	
	protected Thought think() {
		if( text.isEmpty() ) {
			if( message != null ) {
				return new Thought(message);
			} else {
				Brain.log.debugOut("Nothing to think about");
				return null;
			}
		} else {
			if( message != null ) {
				return new Thought(text, message);
			} else {
				return new Thought(text);
			}
		}
	}

}
