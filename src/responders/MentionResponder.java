package responders;

import java.util.ArrayList;

import library.Constants;
import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import utilities.Handler;

public class MentionResponder extends Handler {
	// Placeholder example auto handler
	
	public MentionResponder() {}
	
	public String process(MessageReceivedEvent event) {
		if( Constants.DEBUG ) {System.out.println("\t\t\tProcessing MentionResponder.");}
		String response = "";
		String messageText = format(event);
		if( Constants.DEBUG ) {System.out.println("\t\t\tFormatted message: \"" + messageText + "\"");}
		ArrayList<String> tokens = Brain.tp.parse(event.getMessage().getContent().toLowerCase());
		
		if( tokens.contains(Constants.NAME.toLowerCase()) ) {
			response = "What is it?";
			if( Constants.DEBUG ) {System.out.println("\t\t\t\tMentionResponder triggered.");}
		} else if( Constants.DEBUG ) {System.out.println("\t\t\t\tMentionResponder unactivated.");}
		if( Constants.DEBUG ) {System.out.println("\t\t\tMentionResponder processed.");}
		return response;
	}

}
