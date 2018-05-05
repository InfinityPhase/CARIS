package memories;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Thought;

public class TimeMemory extends Memory {

	@Override
	public Thought remember( MessageReceivedEvent event ) {
		setup(event);
		name = "Time";
		
		text.add("Time Sent: ");
		text.add( "" + event.getMessage().getCreationDate() );
		return think();
	}
}
