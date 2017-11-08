package responders;

import java.util.Calendar;

import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Reminder;
import tokens.Response;
import utilities.TimeParser;

public class ReminderResponder extends Responder {
	
	private TimeParser timeParser = new TimeParser();
	
	@Override
	public Response process(MessageReceivedEvent event) {
		messageSetup(event);
		if( hasIgnoreCase( tokens, "reminder" ) || hasIgnoreCase( tokens, "remind" ) && hasIgnoreCase( tokens, "on" ) ) {
			Calendar c = timeParser.parseAlarm(tokens);
			if( c != null ) {
				Brain.guildIndex.get(event.getGuild()).reminders.put(c, new Reminder(message, event));
				response = "Reminder set successfully.";
				System.out.println(c.toString());
			}
		} else if( hasIgnoreCase( tokens, "remind" ) && hasIgnoreCase( tokens, "in" )) {
			Calendar c = timeParser.parseTimer(tokens);
			if( c != null ) {
				Brain.guildIndex.get(event.getGuild()).reminders.put(c, new Reminder(message, event));
				response = "Reminder set successfully.";
				System.out.println(c.toString());
			}
		}
		return build();
	}

}
