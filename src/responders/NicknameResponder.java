package responders;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class NicknameResponder extends Responder {

	public NicknameResponder() {
		this(Status.ENABLED);
	}

	public NicknameResponder( Status status ) {
		this.status = status;
		name = "Nickname";
		help = "**__Nickname Responder__**"  +
				"\nIf you ask CARIS to set your name to something in the chat, she'll do it for you."  +
				"\nRemember to put your name in quotes!"  +
				"\n"  +
				"\n*\"Caris, set my name to \"Alina Kim\".\"*"  +
				"\n*\"My name is \"Alina Kim\"!\"*";
	}

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
