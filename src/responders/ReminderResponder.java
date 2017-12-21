package responders;

import java.util.ArrayList;
import java.util.Calendar;

import library.PunctuationOptions;
import library.Variables;
import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Reminder;
import tokens.Response;
import utilities.TimeParser;

public class ReminderResponder extends Responder {
	
	static private TimeParser timeParser = new TimeParser();
	
	@Override
	public Response process(MessageReceivedEvent event) {
		messageSetup(event);
		if( hasIgnoreCase( tokens, "reminder" ) || hasIgnoreCase( tokens, "remind" ) && hasIgnoreCase( tokens, "on" ) || hasIgnoreCase( tokens, "remind") && hasIgnoreCase( tokens, "at" ) ) {
			Calendar c = timeParser.parseAlarm(tokens);
			if( c != null ) {
				Variables.guildIndex.get(event.getGuild()).reminders.put(c, new Reminder(message, event));
				response = "Reminder set successfully.";
				System.out.println(c.toString());
			}
		} else if( hasIgnoreCase( tokens, "remind" ) ) {
			Calendar c = timeParser.parseTimer(tokens);
			if( c != null ) {
				Variables.guildIndex.get(event.getGuild()).reminders.put(c, new Reminder(message, event));
				response = "Reminder set successfully.";
				System.out.println(c.toString());
			}
		}
		return build();
	}

	@Override
	protected ArrayList<String> tokenize(MessageReceivedEvent event) {
		return Brain.tp.parse(event.getMessage().getContent().toLowerCase(), PunctuationOptions.TIME);
	}
	
}
