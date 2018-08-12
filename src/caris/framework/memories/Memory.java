package caris.framework.memories;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import caris.framework.library.Variables;
import caris.framework.main.Brain;
import caris.framework.library.GuildInfo;
import caris.framework.tokens.Thought;
import caris.framework.utilities.TokenParser;

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
		tokens = TokenParser.parse(event.getMessage().getContent());
		variables = Variables.guildIndex.get(event.getGuild());
		
		text = new ArrayList<String>();
		message = null;
		name = "";
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
