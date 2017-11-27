package responders;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class NicknameResponder extends Responder {
	
	/* This is a magic ID. Used to ID when we can restore states */
	private static final long serialVersionUID = -570619837617912235L;

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
