package utilities;

import java.util.ArrayList;

import library.Constants;
import main.Brain;
import main.GuildInfo;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import tokens.Response;

public class Handler {
	// The base handler class. Extend this into other classes.
	
	protected String response;
	protected String messageText;
	protected ArrayList<String> tokens;
	protected GuildInfo variables;
	
	public Handler() {}
	
	
	public Response process(MessageReceivedEvent event) {
		return new Response("", getPriority());
	}
	
	protected void setup(MessageReceivedEvent event) {
		if( Constants.DEBUG ) {System.out.println("\t\t\tProcessing LocationResponder.");}
		response = "";
		messageText = format(event);
		if( Constants.DEBUG ) {System.out.println("\t\t\tFormatted message: \"" + messageText + "\"");}
		tokens = tokenize(event);
		variables = Brain.guildIndex.get(event.getGuild());
	}
	
	protected int getPriority() {
		return 0;
	}
	
	protected String format(MessageReceivedEvent event) {
		String messageText = event.getMessage().getContent();
		messageText = " " + messageText + " ";
		return messageText;
	}
	
	protected ArrayList<String> tokenize(MessageReceivedEvent event) {
		return Brain.tp.parse(event.getMessage().getContent());
	}
	
	protected Response build(String response) {
		return new Response(response, getPriority());
	}
	protected Response build(EmbedBuilder builder) {
		return new Response(builder, getPriority());
	}
}
