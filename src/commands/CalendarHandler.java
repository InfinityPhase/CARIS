package commands;

import java.util.Calendar;

import main.Brain;
import main.GuildInfo;
import sx.blah.discord.handle.obj.IGuild;
import tokens.Reminder;
import utilities.BotUtils;

public class CalendarHandler {
	
	public void check() {
		for( IGuild guild : Brain.guildIndex.keySet() ) {
			GuildInfo info = Brain.guildIndex.get(guild);
			for( Calendar c : info.reminders.keySet() ) {
				Reminder reminder = info.reminders.get(c);
				if( Brain.current.after(c) ) {
					System.out.println(Brain.current.toString());
					String send = "";
					send += reminder.origin.getAuthor().mention();
					send += ", here's your reminder";
					if( reminder.message.isEmpty() ) {
						send += ".";
					} else {
						send += ": \"";
						send += reminder.message;
						send += "\".";
					}
					BotUtils.sendMessage( reminder.origin.getChannel(), send );
					info.reminders.remove(c);
				}
			}
		}
	}
	
}
