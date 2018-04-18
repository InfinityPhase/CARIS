package modules.responders;

import java.util.ArrayList;

import main.Brain;
import modules.Handler;
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

}