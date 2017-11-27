package responders;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class NicknameResponder extends Responder {
	
	@Override
	public Response process(MessageReceivedEvent event) {
		log.log("NickResp");
		messageSetup(event);
		if( hasIgnoreCase( tokens, "my" ) && ( containsIgnoreCase( tokens, "name" )) ) {
			if( !message.isEmpty() ) {
				response = "Nickname set to \"" + message + "\"!";
			}
		}
		return build();
	}
	
	@Override
	protected String format(MessageReceivedEvent event) {
		String messageText = event.getMessage().getContent();
		return messageText;
	}
	
	@Override
	protected int getPriority() {
		return 1;
	}
}
