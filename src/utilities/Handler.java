package utilities;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import tokens.Response;

public class Handler {
	// The base handler class. Extend this into other classes.
	
	public Handler() {}
	
	public Response process(MessageReceivedEvent event) {
		return new Response("", getPriority());
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
	
	public Response build(String response) {
		return new Response(response, getPriority());
	}
	public Response biuld(EmbedBuilder builder) {
		return new Response(builder, getPriority());
	}
}
