package memories;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;

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
		text.add( event.getMessage().getCreationDate().format( sdf.parse( Constants.DATEFORMAT ) ) );
		
		return think();
	}
}
