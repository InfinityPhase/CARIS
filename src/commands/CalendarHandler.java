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
import utilities.Logger;

public class CalendarHandler {
	Logger log = new Logger().setBaseIndent(1).setDefaultShouldAppendTime(true).build();
	
	public void check() {
		for( IGuild guild : Variables.guildIndex.keySet() ) {
			GuildInfo info = Variables.guildIndex.get(guild);
			for( Calendar c : info.reminders.keySet() ) {
				Reminder reminder = info.reminders.get(c);
				if( Brain.current.after(c) ) {
					log.log("Sending reminder...");
					log.indent(1).log("Guild: " + guild.getName() + " : " + guild.getStringID() );
					log.indent(1).log("Channel: " + reminder.channelID );
					log.indent(1).log("Author: " + reminder.author );
					log.indent(1).log("Content: " + reminder.message );
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
					info.reminders.remove(c);
				}
			}
		}
	}
	
}
