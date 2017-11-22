package memories;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Thought;

public class AuthorMemory extends Memory {
	
	@Override
	public Thought remember( MessageReceivedEvent event ) {
		setup(event);
		name = "Author";
		
		text.add("Author Username: ");
		text.add( event.getAuthor().getName() );
		text.add("Author Nickname: ");
		text.add( event.getAuthor().getDisplayName( event.getGuild() ) );
		
		return think();
	}

}
