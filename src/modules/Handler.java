package modules;

import java.util.ArrayList;

import library.Variables;
import main.Brain;
import main.GuildInfo;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class Handler extends Module {
	// Handles modules that depend on user interaction

	public String prefix;

	protected String message;
	protected String messageText;
	protected ArrayList<String> tokens;
	protected GuildInfo variables;

	public Handler() {}

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
		setup();
		setupType = Setup.TOKEN;
		message = "";
		messageText = format(event);
		tokens = tokenize(event);
		variables = Variables.guildIndex.get(event.getGuild());
	}

	protected void messageSetup(MessageReceivedEvent event) {
		setup();
		setupType = Setup.MESSAGE;
		message = "";
		messageText = messageFormat(event);
		tokens = tokenize(event);
		variables = Variables.guildIndex.get(event.getGuild());
	}

	protected String format(MessageReceivedEvent event) {
		String messageText = event.getMessage().getContent();
		messageText = " " + messageText + " ";
		return messageText;
	}

	protected String messageFormat(MessageReceivedEvent event) {
		String messageText = event.getMessage().getContent();
		int q1 = messageText.indexOf("\"");
		int q1alt = messageText.indexOf("�");
		int q2 = messageText.lastIndexOf("\"");
		int q2alt = messageText.indexOf("�");
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

	protected boolean containsAnyQuotes( String s ) {
		return s.contains("\"") || s.contains("�") || s.contains("�");
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
