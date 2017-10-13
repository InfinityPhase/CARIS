package utilities;

import sx.blah.discord.handle.impl.obj.Message;

public class Handler {
	// The base handler class. Extend this into other classes.
	
	public Handler() {}
	
	public String process(Message message) {
		String response = "";
		String messageText = format(message);
		return "";
	}
	
	public int getPriority() {
		return 0;
	}
	
	public String format(Message message) {
		String messageText = message.getContent();
		messageText = messageText.toLowerCase();
		messageText = " " + messageText + " ";
		return messageText;
	}
	
}
