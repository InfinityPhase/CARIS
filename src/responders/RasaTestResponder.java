package responders;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;
import main.Brain;

public class RasaTestResponder extends Responder {
	public Response process( MessageReceivedEvent event ) {
		setup(event);
		Brain.log.debugOut("Sending request to RASA...");
		Brain.rasa.process( event );
		Brain.log.debugOut("Event processed.");
		
		response.concat( "Intent: " + Brain.rasa.getIntent( event ) );
		response.concat( "Confidence" + Brain.rasa.getConfidence( event ) );
		
		return build();
	}

}
