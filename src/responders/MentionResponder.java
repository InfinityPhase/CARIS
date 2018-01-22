package responders;

import library.Constants;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class MentionResponder extends Responder {
	// Placeholder example auto handler
		
	@Override
	public Response process(MessageReceivedEvent event) {
		tokenSetup(event);

		if( containsIgnoreCase(tokens, Constants.NAME) ) {
			response = "What is it?";
			log.indent(2).log("MentionResponder triggered.");
		} else {
			log.indent(2).log("MentionResponder unactivated."); 
		}
		log.indent(1).log("MentionResponder processed.");
		return build();
	}

}
