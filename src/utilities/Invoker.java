package utilities;

import java.util.ArrayList;

import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Invoker extends Handler {
	// Base Invoker class. Same as Handler, but here for future use.
	
	@Override
	protected ArrayList<String> tokenize(MessageReceivedEvent event) {
		return Brain.tp.parse(event.getMessage().getContent());
	}
	
	@Override
	protected String format(MessageReceivedEvent event) {
		String messageText = event.getMessage().getContent();
		messageText = " " + messageText + " ";
		return messageText;
	}
}
