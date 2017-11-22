package memories;

import java.util.ArrayList;
import java.util.List;

import library.Variables;
import main.Brain;
import main.GuildInfo;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import tokens.Thought;

public class Memory {
	// Base Memory class
	// Doesn't return a message, returns a "thought"
	// Thoughts represent info about message
	
	// Prob. shouldn't extend handler, bc handler is about messaging
	// But I want to share some functions, so maybe do extend?
	
	// reponses for thinking
	protected List<String> text;
	protected IMessage message;
	protected String name;

	protected String messageText;
	protected ArrayList<String> tokens;
	protected GuildInfo variables;
	
	public Memory() {}
	
	public Thought remember( MessageReceivedEvent event ) {
		setup(event);
		return think();
	}
		
	protected void setup(MessageReceivedEvent event) {
		messageText = format(event);
		tokens = Brain.tp.parse(event.getMessage().getContent());
		variables = Variables.guildIndex.get(event.getGuild());
		
		text = new ArrayList<String>();
		message = null;
		name = "";
		Brain.log.debugOut( name.isEmpty() );
		Brain.log.debugOut( text.isEmpty() );
	}
	
	protected Thought think() {
		if( text.isEmpty() ) {
			if( message != null ) {
				return new Thought(message, name);
			} else {
				return new Thought();
			}
		} else {
			if( message != null ) {
				return new Thought(text, message, name);
			} else {
				return new Thought(text, name);
			}
		}
	}
	
	protected String format(MessageReceivedEvent event) {
		// Copied from handler
		String messageText = event.getMessage().getContent();
		messageText = " " + messageText + " ";
		return messageText;
	}
}
