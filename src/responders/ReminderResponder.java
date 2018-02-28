package responders;

import java.util.ArrayList;
import java.util.Calendar;

import library.PunctuationOptions;
import library.Variables;
import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Reminder;
import tokens.Response;
import utilities.Logger.level;
import utilities.TimeParser;

public class ReminderResponder extends Responder {
		
	public ReminderResponder() {
		this(Status.ENABLED);
	}

	public ReminderResponder( Status status ) {
		this.status = status;
		name = "Reminder";
		help = "\n**__Reminder Responder__**"  +
				"\nYou can even ask CARIS to set reminders for you!"  +
				"\nYou can ask her to remind you at a certain time, or in a certain timer."  +
				"\nKeep in mind that you need to use military time, or PM."  +
				"\nUse digits instead of words to express numbers."  +
				"\nIf you want to include a message, put it in quotes."  +
				"\n"  +
				"\n*\"Can someone remind me to \"check messages\" at 4 PM?\"*"  +
				"\n*\"Remind me on March 26th to wish Alina a happy birthday.\"*"  +
				"\n*\"CARIS, try to remind me in about 5 minutes.*";
	}

	private TimeParser timeParser = new TimeParser();

	@Override
	public Response process(MessageReceivedEvent event) {
		messageSetup(event);
		if( hasIgnoreCase( tokens, "reminder" ) || hasIgnoreCase( tokens, "remind" ) && hasIgnoreCase( tokens, "on" ) || hasIgnoreCase( tokens, "remind") && hasIgnoreCase( tokens, "at" ) ) {
			Calendar c = timeParser.parseAlarm(tokens);
			if( c != null ) {
				Variables.guildIndex.get(event.getGuild()).reminders.put( Variables.timeString(c), new Reminder(message, event));
				response = "Reminder set successfully.";
				log.indent(1).level( level.DEBUG ).log(c.toString());
			}
		} else if( hasIgnoreCase( tokens, "remind" ) ) {
			Calendar c = timeParser.parseTimer(tokens);
			if( c != null ) {
				Variables.guildIndex.get(event.getGuild()).reminders.put( Variables.timeString(c), new Reminder(message, event));
				response = "Reminder set successfully.";
				log.indent(1).level( level.DEBUG ).log(c.toString());
			}
		} else if( hasIgnoreCase( tokens, "countdown" ) ) {

			if( hasIgnoreCase( tokens, "build" ) && hasIgnoreCase( tokens, "season" ) ) { 
				if( hasIgnoreCase( tokens, "off" ) || hasIgnoreCase( tokens, "deactivate" ) || hasIgnoreCase( tokens, "disable" ) ) {
					if( Variables.guildIndex.get(event.getGuild()).buildSeasonCountdown ) {
						response = "The countdown is not enabled.";
					} else {
						response = "Build Season Countdown deactivated.";
					}
					Variables.guildIndex.get(event.getGuild()).buildSeasonCountdown = false;
				}
				else {
					if( Variables.guildIndex.get(event.getGuild()).buildSeasonCountdown ) {
						response = "The countdown is already enabled!";
					} else {
						response = "Build Season Countdown activated!";
					}
					Variables.guildIndex.get(event.getGuild()).buildSeasonCountdown = true;
				}
			}
		}
		return build();
	}

	@Override
	protected ArrayList<String> tokenize(MessageReceivedEvent event) {
		return Brain.tp.parse(event.getMessage().getContent().toLowerCase(), PunctuationOptions.TIME);
	}

}
