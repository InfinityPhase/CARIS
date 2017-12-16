package commands;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import library.Constants;
import library.Variables;
import main.Brain;
import main.GuildInfo;
import sx.blah.discord.handle.obj.IGuild;
import tokens.Reminder;
import utilities.BotUtils;

public class CalendarHandler {
	
	public void check() {
		for( IGuild guild : Variables.guildIndex.keySet() ) {
			GuildInfo info = Variables.guildIndex.get(guild);
			for( Calendar c : info.reminders.keySet() ) {
				Reminder reminder = info.reminders.get(c);
				if( Brain.current.after(c) ) {
					System.out.println(Brain.current.toString());
					String send = "";
					send += reminder.author;
					send += ", here's your reminder";
					if( reminder.message.isEmpty() ) {
						send += ".";
					} else {
						send += ": \"";
						send += reminder.message;
						send += "\".";
					}
					BotUtils.sendMessage( Variables.channelMap.get(reminder.channelID), send );
					info.reminders.remove(c);
				}
			}
			if( info.buildSeasonCountdown ) {
				long kickoffTime = Constants.kickoff.getTimeInMillis();
				long currentTime = Brain.current.getTimeInMillis();
				int dayDiff = (int) (TimeUnit.MILLISECONDS.toDays(kickoffTime-currentTime));
				if( TimeUnit.MILLISECONDS.toDays(kickoffTime-currentTime) > 5 ) {
					
					
				}
			}
		}
	}
	
}
