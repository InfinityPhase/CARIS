package utilities;

import java.util.ArrayList;
import java.util.Set;

import library.Variables;
import main.Brain;
import main.GuildInfo;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import tokens.Response;

public class Handler {
	// The base handler class. Extend this into other classes.
	
	protected String response;
	protected EmbedBuilder embed;
	protected String messageText;
	protected ArrayList<String> tokens;
	protected GuildInfo variables;
	
	public Handler() {}
	
	public Response process(MessageReceivedEvent event) {
		setup(event);
		return build();
	}
	
	protected void setup(MessageReceivedEvent event) {
		response = "";
		embed = null;
		messageText = format(event);
		tokens = tokenize(event);
		variables = Variables.guildIndex.get(event.getGuild());
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
	
	protected Response build() {
		if( embed == null ) {
			return buildResponse();
		} else {
			return buildEmbed();
		}
	}
	
	protected Response buildResponse() {
		return new Response(response, getPriority());
	}
	protected Response buildEmbed() {
		return new Response(embed, getPriority());
	}
	
	protected boolean containsIgnoreCase(String a, String b) {
		return a.toLowerCase().contains(b.toLowerCase());
	}
	
	protected boolean containsIgnoreCase(ArrayList<String> a, String b) {
		for( String token : a ) {
			if( containsIgnoreCase(token, b) ) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean containsIgnoreCase(String[] a, String b) {
		for( String token : a ) {
			if( containsIgnoreCase(token, b) ) {
				return true;
			}
		}
		return false;
	}
	
	protected Boolean containsIgnoreCase(Set<String> a, String b) {
		for( String token : a ) {
			if( containsIgnoreCase(token, b) ) {
				return true;
			}
		}
		return false;
	}
}
