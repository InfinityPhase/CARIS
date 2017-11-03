package memories;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class getAuthor extends Memory {
	
	public Response remember( MessageReceivedEvent event ) {
		setup(event);
		
		response = "Author Display Name: ";
		response.concat( event.getAuthor().getDisplayName( event.getGuild() ) );
		
		response.concat("\n");
		response.concat("Author User Name: ");
		response.concat( event.getAuthor().getName() );
		
		return build();
	}

}
