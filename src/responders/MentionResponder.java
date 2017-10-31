package responders;

import library.Constants;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class MentionResponder extends Responder {
	// Placeholder example auto handler
		
	/* This is a magic ID. Used to ID when we can restore states */
	private static final long serialVersionUID = 3611413008726703248L;

	@Override
	public Response process(MessageReceivedEvent event) {
		setup(event);

		if( containsIgnoreCase(messageText, " " + Constants.NAME + " ") ) {
			response = "What is it?";
			if( Constants.DEBUG ) {System.out.println("\t\t\t\tMentionResponder triggered.");}
		} else if( Constants.DEBUG ) {System.out.println("\t\t\t\tMentionResponder unactivated.");}
		if( Constants.DEBUG ) {System.out.println("\t\t\tMentionResponder processed.");}
		return build();
	}

}
