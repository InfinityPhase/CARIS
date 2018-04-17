package modules.invokers;


import java.util.ArrayList;

import library.Variables;
import modules.Handler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.LineSet;
import tokens.Response;

public class Invoker extends Handler {
	public LineSet command;
	
	@Override
	public Response process(MessageReceivedEvent event) {
		if( containsIgnoreCase( "\n", event.getMessage().getContent() ) ) {
			linesetSetup(event);
		}
		return build();
	}
	
	protected void linesetSetup(MessageReceivedEvent event) {
		response = "";
		message = "";
		embed = null;
		messageText = format(event);
		command = linesetHandle(event);
		tokens = tokenize(event);
		variables = Variables.guildIndex.get(event.getGuild());
	}
	
	protected LineSet linesetHandle(MessageReceivedEvent event) {
		String line = linesetFormat(event);
		ArrayList<String> lineTokens = tokenize(line);
		LineSet command = new LineSet(line, lineTokens);
		return command;
	}
	
	protected String linesetFormat(MessageReceivedEvent event) {
		String baseText = event.getMessage().getContent();
		int index = baseText.indexOf("\n");
		if( index != -1 ) {
			baseText = baseText.substring(0, index);
		}
		return baseText;
	}
}
