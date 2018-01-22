package utilities;

import java.util.ArrayList;
import java.util.Set;

import library.Variables;
import main.Brain;
import main.GuildInfo;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;
import tokens.Response;

public class Handler {
	// The base handler class. Extend this into other classes.
	
	protected enum Setup {
		UNSET,
		TOKEN,
		MESSAGE
	}
	
	protected Setup setupType = Setup.UNSET;
	
	protected Logger log = new Logger().setDefaultIndent( 0 ).setBaseIndent( 2 ).build();
	
	protected String response;
	protected String message;
	protected EmbedBuilder embed;
	protected String messageText;
	protected IChannel recipient;
	protected ArrayList<String> tokens;
	protected GuildInfo variables;
	
	public Handler() {
		
	}
	
	public Response process(MessageReceivedEvent event) {
		conditionalSetup(event);
		return build();
	}
	
	protected void conditionalSetup(MessageReceivedEvent event) {
		if( containsAnyQuotes(event.getMessage().getContent()) ) {
			messageSetup(event);
		} else {
			tokenSetup(event);
		}
	}
	
	protected void tokenSetup(MessageReceivedEvent event) {
		setupType = Setup.TOKEN;
		response = "";
		message = "";
		embed = null;
		messageText = format(event);
		tokens = tokenize(event);
		variables = Variables.guildIndex.get(event.getGuild());
	}
	
	protected void messageSetup(MessageReceivedEvent event) {
		setupType = Setup.MESSAGE;
		response = "";
		message = "";
		embed = null;
		messageText = messageFormat(event);
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
	
	protected String messageFormat(MessageReceivedEvent event) {
		String messageText = event.getMessage().getContent();
		int q1 = messageText.indexOf("\"");
		int q1alt = messageText.indexOf("“");
		int q2 = messageText.lastIndexOf("\"");
		int q2alt = messageText.indexOf("”");
		if( q1 != -1 && q1 != q2 ) {
			message = messageText.substring(q1+1, q2);
			String a = messageText.substring(0, q1);
			String b = "";
			if( q2 != messageText.length()-1 ) {
				b = messageText.substring(q2+1);
			}
			messageText = a + b;
		} else if ( q1alt != -1 && q2alt != q1alt ) {
			message = messageText.substring(q1alt+1, q2alt);
			String a = messageText.substring(0, q1alt);
			String b = "";
			if( q2alt != messageText.length()-1 ) {
				b = messageText.substring(q2alt+1);
			}
			messageText = a + b;
		}
		return messageText;
	}
	
	protected ArrayList<String> tokenize(MessageReceivedEvent event) {
		return Brain.tp.parse(event.getMessage().getContent());
	}
	
	protected ArrayList<String> tokenize( String s ) {
		return Brain.tp.parse(s);
	}
	
	protected Response build() {
		if( embed == null ) {
			return buildResponse();
		} else {
			return buildEmbed();
		}
	}
	
	protected Response buildResponse() {
		if( recipient == null ) {
			return new Response(response, getPriority());
		} else {
			return new Response(response, getPriority(), recipient, true);
		}
	}
	protected Response buildEmbed() {
		if( recipient == null ) {
			return new Response(embed, getPriority());
		} else {
			return new Response(embed, getPriority(), recipient, true);
		}
	}
	
	protected boolean hasIgnoreCase(ArrayList<String> a, String b) {
		for( String token: a ) {
			if( token.equalsIgnoreCase(b) ) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean hasIgnoreCase(String[] a, String b) {
		for( String token: a ) {
			if( token.equalsIgnoreCase(b) ) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean hasIgnoreCase(Set<String> a, String b) {
		for( String token: a ) {
			if( token.equalsIgnoreCase(b) ) {
				return true;
			}
		}
		return false;
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
	
	protected boolean containsAnyQuotes( String s ) {
		return s.contains("\"") || s.contains("“") || s.contains("”");
	}
	
	protected Boolean containsIgnoreCase(Set<String> a, String b) {
		for( String token : a ) {
			if( containsIgnoreCase(token, b) ) {
				return true;
			}
		}
		return false;
	}
	
	protected String remainder( String prefix ) {
		return remainder(prefix, messageText);
	}
	
	protected String remainder( String prefix, String line ) {
		int i1 = line.indexOf(prefix);
		if( i1 != -1 ) {
			int i2 = i1 + prefix.length();
			String s = line.substring(i2);
			while( s.startsWith(" ") ) {
				s = s.substring(1);
			}
			return s;
		} else {
			return "";
		}
	}
}
