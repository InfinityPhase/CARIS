package responders;

import java.util.ArrayList;

import library.Constants;
import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Message;
import utilities.Handler;
import tokens.UserData;

public class MentionResponder extends Handler {
	// Placeholder example auto handler
	
	public MentionResponder() {}
	
	public String process(MessageReceivedEvent event) {
		String response = "";
		String messageText = format(event);
		ArrayList<String> tokens = Brain.tp.parse(messageText);
		
		if( tokens.contains(Constants.NAME.toLowerCase()) ) {
			response = "What is it?";
		}
		return response;
	}

}
