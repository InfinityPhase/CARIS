package memories;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class getAuthor extends Memory {
	
	public Response remember( MessageReceivedEvent event ) {
		setup(event);
		
		
		
		return think();
	}

}
