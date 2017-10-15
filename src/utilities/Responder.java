package utilities;

import java.util.ArrayList;
import java.util.Set;

import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Responder extends Handler {
	// Base Responder class. Ignores case.
	
	protected String format(MessageReceivedEvent event) {
		String messageText = event.getMessage().getContent();
		messageText = messageText.toLowerCase();
		messageText = " " + messageText + " ";
		return messageText;
	}
	
	protected ArrayList<String> tokenize(MessageReceivedEvent event) {
		return Brain.tp.parse(event.getMessage().getContent().toLowerCase());
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
