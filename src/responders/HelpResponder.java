package responders;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class HelpResponder extends Responder {
	
	@Override
	public Response process(MessageReceivedEvent event) {
		tokenSetup(event);
		
		return build();
	}
	
}
