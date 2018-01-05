package commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import library.Variables;
import main.Brain;
import main.GuildInfo;
import sx.blah.discord.handle.obj.IChannel;
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
		}
	}

	private void startFRCCountdown() {
		// Add a reminder for the start of build season

		// Real
		IGuild guild = Brain.cli.getGuildByID( 359566653987487744L );
		IChannel channel = Brain.cli.getChannelByID( 359566654478483456L );
		
		/* Testing
		IGuild guild = Brain.cli.getGuildByID( 366853317709791232L );
		IChannel channel = Brain.cli.getChannelByID( 384618675841531906L ); // TEST
		*/
		
		Brain.log.indent(10).log("DOES GUILDINDEX EXIST: "+(Variables.guildIndex.containsKey(guild)?"YES":"NO"));
		//Brain.log.indent(10).log("DOES CHANNEL IN INDEX EXIST: "+(Variables.guildIndex.get(guild).settings.containsKey(channel)?"YES":"NO"));

		if( !Variables.guildIndex.containsKey( guild ) ) {
			Brain.log.indent(9).log("MADE GUILD BADLY");
			Variables.guildIndex.put( guild, new GuildInfo( guild.getName(), guild ) );
		}

		if( !Variables.guildIndex.get( guild ).settings.containsKey( channel ) ) { // Y U FAIL
			// Add the key and the hashmap
			Variables.guildIndex.get( guild ).settings.put( channel, new HashMap<String, Object>() );
		}

		// Add the next day for the countdown
		Brain.log.log("Setting next build season thing");
		Calendar next = Calendar.getInstance();
		next.add( Calendar.DATE, 1 ); // The next day
		next.set( Calendar.HOUR_OF_DAY, 3 ); // When I go to bed...
		next.set( Calendar.MINUTE, 0 );
		next.set( Calendar.SECOND, 0 );
		Variables.guildIndex.get( guild ).settings.get( channel ).put( "buildSeasonCountdown", next );


	}

	public void FRCCountdown() {
		/* Stop Build Day
		 * 02/20/2018 - 11:59pm
		 */
		SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd");

		String endSeason = "2018 02 20"; 
		String startSeason = "2018 01 06";
		long endLength = 0;
		long startLength = 0;

		Date now = new Date();

		// Real
		IGuild guild = Brain.cli.getGuildByID( 359566653987487744L ); // REAL
		IChannel channel = Brain.cli.getChannelByID( 359566654478483456L ); // REAL
		
		/*//Testing
		IGuild guild = Brain.cli.getGuildByID( 366853317709791232L );
		IChannel channel = Brain.cli.getChannelByID( 384618675841531906L );
		*/

		if( !Variables.guildIndex.get( guild ).settings.containsKey( channel ) || !Variables.guildIndex.get( guild ).settings.get( channel ).containsKey( "buildSeasonCountdown" ) ) { 
			startFRCCountdown(); // Just to be "safe"
			Brain.log.log("Countdown doesn't exist, somehow...");

		}

		if( Brain.current.after( Variables.guildIndex.get( guild ).settings.get( channel ).get( "buildSeasonCountdown" ) ) ) {
			Brain.log.indent(1).log("TIME TO SEND A MESSAGE");
			// I love stackoverflow...
			// https://stackoverflow.com/a/20165708
			try {
				Date end = format.parse( endSeason );
				Date start = format.parse( startSeason );
				endLength = TimeUnit.DAYS.convert( ( now.getTime() - end.getTime() ), TimeUnit.MILLISECONDS);
				startLength = TimeUnit.DAYS.convert( ( start.getTime() - now.getTime() ), TimeUnit.MILLISECONDS);
			} catch( ParseException e ) {
				e.printStackTrace();
			}

			// Send the message
			Brain.log.indent(1).log("SENDING BUILD SEASON THING");
			BotUtils.sendMessage( channel, "Build season continues!\n" + startLength + " days have passed from the start of build season.\nThere are " + endLength + " days remaining.");

			// Add a new reminder in the future
			// Add the next day for the countdown
			Calendar next = Calendar.getInstance();
			next.add( Calendar.DATE, 1 ); // The next day
			next.set( Calendar.HOUR_OF_DAY, 3 ); // When I go to bed...
			next.set( Calendar.MINUTE, 0 );
			next.set( Calendar.SECOND, 0 );
			Variables.guildIndex.get( guild ).settings.get( channel ).put( "buildSeasonCountdown", next );

		}
	}

}
