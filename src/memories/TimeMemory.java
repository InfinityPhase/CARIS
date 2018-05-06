package memories;

import java.text.SimpleDateFormat;

import library.Constants;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Thought;

public class TimeMemory extends Memory {
	SimpleDateFormat sdf = new SimpleDateFormat( Constants.DATEFORMAT );
	
	@Override
	public Thought remember( MessageReceivedEvent event ) {
		setup(event);
		name = "Time";
		
		text.add("Time Sent: ");
		text.add( "" + event.getMessage().getCreationDate() );
		return think();
	}
}
