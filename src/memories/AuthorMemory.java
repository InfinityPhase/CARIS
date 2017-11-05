package memories;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;
import tokens.Thought;

public class AuthorMemory extends Memory {
	
	public Thought remember( MessageReceivedEvent event ) {
		setup(event);
		
		text.add("Message Author: ");
		text.add( event.getAuthor().getName() );
		text.add("Author Display Name: ");
		text.add( event.getAuthor().getDisplayName( event.getGuild() ) );
		
		
		return think();
	}

}
