package memories;

import java.util.List;

import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import tokens.Thought;
import utilities.Handler;

public class Memory extends Handler {
	// Base Memory class
	// Doesn't return a message, returns a "thought"
	// Thoughts represent info about message
		
	@Override
	protected void setup(MessageReceivedEvent event) {
		super.setup(event);
		
		response = "";
		embed = null;
		messageText = format(event);
		tokens = tokenize(event);
		variables = Brain.guildIndex.get(event.getGuild());
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
		if( Thought.text != null ) {
			if( message != null ) {
				return new Thought(text, message);
			}
		}
	}

}
