package responders;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class NicknameResponder extends Responder {
	
	/* This is a magic ID. Used to ID when we can restore states */
	private static final long serialVersionUID = -570619837617912235L;

	@Override
	public Response process(MessageReceivedEvent event) {
		System.out.println("NickResp");
		setup(event);
		if( containsIgnoreCase( tokens, "my" ) && ( containsIgnoreCase( tokens, "name" ) && containsIgnoreCase( tokens, "is" ) || containsIgnoreCase( tokens, "names" ) || containsIgnoreCase( tokens, " name's " ) ) ) {
			int index1 = messageText.indexOf('\"');
			int index2 = messageText.lastIndexOf('\"');
			if( index1 != -1 && index2 != -1 && index1 != index2 ) {
				String nick = messageText.substring(index1+1, index2);
				response = "Nickname set to \"" + nick + "\"!";
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
