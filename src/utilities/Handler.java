package utilities;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Handler {
	// The base handler class. Extend this into other classes.
	
	public Handler() {}
	
	public String process(MessageReceivedEvent event) {
		String response = "";
		String messageText = format(event);
		return "";
	}
	
	public int getPriority() {
		return 0;
	}
	
	public String format(MessageReceivedEvent event) {
		String messageText = event.getMessage().getContent();
		messageText = messageText.toLowerCase();
		messageText = " " + messageText + " ";
		return messageText;
	}
	
}
