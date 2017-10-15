package invokers;

import java.util.ArrayList;

import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import utilities.Handler;

public class Invoker extends Handler {
	// Base Invoker class. Setup removes first token.
	
	@Override
	protected void setup(MessageReceivedEvent event) {
		response = "";
		messageText = format(event);
		tokens = tokenize(event);
		variables = Brain.guildIndex.get(event.getGuild());
		tokens.remove(0);
	}
	
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
