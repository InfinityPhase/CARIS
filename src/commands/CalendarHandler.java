package commands;

import java.util.Calendar;

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
			for( String s : info.reminders.keySet() ) {
				Calendar c = Variables.toCalendar( s );
				Reminder reminder = info.reminders.get(s);
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
					BotUtils.sendMessage( Variables.getChannel(reminder.channelID), send );
					info.reminders.remove(s);
				}
			}
		}
	}
	
}
