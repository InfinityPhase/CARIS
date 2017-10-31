package responders;

import java.io.Serializable;
import java.util.ArrayList;

import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import utilities.Handler;

public class Responder extends Handler implements Serializable {
	// Base Responder class. Ignores case.
	
	/* This is a magic ID. Used to ID when we can restore states */
	private static final long serialVersionUID = 3576312616803414827L;

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
