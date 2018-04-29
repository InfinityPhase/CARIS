package modules.responders;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import tokens.Response;

public class TrendResponder extends Responder {
	
	String lastMessage;
	IUser lastUser;
	boolean responded;
	
	public TrendResponder() {
		this(Status.ENABLED);
	}
	
	public TrendResponder( Status status ) {
		this.status = status;
		name = "rTrend";
		lastMessage = "";
		lastUser = null;
		responded = false;
		help = "\n**__Trend Responder__**"  +
				"\nLiterally all this does is wait until two people say something in a row, and then follow the trend.";
	}
	
	@Override
	public Response process(MessageReceivedEvent event) {
		tokenSetup(event);
		log.indent(2).log("Message: " + messageText);
		log.indent(2).log("Previous Message: " + lastMessage);
		log.indent(2).log("Same Message: " + ((lastMessage.equalsIgnoreCase(message) ? "true" : "false")));
		log.indent(2).log("Different user: " + ((lastUser != event.getAuthor()) ? "true" : "false"));
		log.indent(2).log("Already responded: " + ((responded) ? "true" : "false"));
		
		if( lastUser == null ) {
			lastUser = event.getAuthor();
			log.indent(1).log("Initializing last user.");
		}
		
		if( lastMessage.equalsIgnoreCase(messageText) ) {
			if( lastUser != event.getAuthor() && !responded ) {
				response = messageText;
				responded = true;
				log.indent(1).log("Following trend.");
			}
		} else {
			responded = false;
			lastMessage = messageText;
			log.indent(1).log("No trend detected.");
		}
		
		return build();
	}
	
}
