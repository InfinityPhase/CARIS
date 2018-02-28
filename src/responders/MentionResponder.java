package responders;

import library.Constants;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class MentionResponder extends Responder {
	// Placeholder example auto handler

	public MentionResponder() {
		this(Status.ENABLED);
	}

	public MentionResponder( Status status ) {
		this.status = status;
		name = "Mention";
		help = "**__Mention Reponder__**"  +
				"\nPretty simple: you say CARIS's name, she responds."  +
				"\n"  +
				"\n*\"Caris, are you online?\"*";
	}

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
