package commands;

import java.util.Calendar;
import java.util.HashMap;

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
			//GuildInfo info = Variables.guildIndex.get(guild);
			HashMap<String,Reminder> reminders = (HashMap<String, Reminder>) Variables.getReminders( guild );
			for( String s : reminders.keySet() ) {
				Calendar c = Variables.toCalendar( s );
				Reminder reminder = reminders.get(s);
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

					// TODO: Remove reminder
				}
			}
		}
	}
	
}
