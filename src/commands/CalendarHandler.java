package commands;
/*
 * > Initializing.
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Discord4J: ERROR INITIALIZING LOGGER!
Discord4J: No SLF4J implementation found, reverting to the internal implementation (sx.blah.discord.Discord4J$Discord4JLogger)
Discord4J: It is *highly* recommended to use a fully featured implementation like logback!
18:44:39.311: [INFO][main][sx.blah.discord.Discord4J] - Discord4J v2.9.1 474619a (https://github.com/austinv11/Discord4J)
18:44:39.311: [INFO][main][sx.blah.discord.Discord4J] - A Java binding for the official Discord API, forked from the inactive https://github.com/nerd/Discord4J. Copyright (c) 2017, Licensed under GNU LGPLv3
> Client built successfully.
> Listener established successfully.
> Client logged in.
> Loaded Channel Map.
18:45:16.838: [INFO][HttpClient@1243866820-14][sx.blah.discord.Discord4J] - Websocket Connected.
18:45:17.373: [INFO][Dispatch Handler][sx.blah.discord.Discord4J] - Connected to Discord Gateway v6. Receiving 5 guilds.
=> Checking guild...
=====> cli.isReady() is TRUE
==> Creating new Guild Object "Piltover's Finest".
=> Checking guild...
==> Creating new Guild Object "604 Robotics".
=> I SET it TO FUCKING TRUE, DAMMNIT
=> Checking guild...
==> Creating new Guild Object "BOT_TEST".
=> I SET it TO FUCKING TRUE, DAMMNIT
=> Checking guild...
==> Creating new Guild Object "discord chat".
=> I SET it TO FUCKING TRUE, DAMMNIT
=> Checking guild...
==> Creating new Guild Object "Hex Gambit".
=> I SET it TO FUCKING TRUE, DAMMNIT
==========> DOES GUILDINDEX EXIST: YES
> Setting next build season thing
> Countdown doesn't exist, somehow...
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import library.Variables;
import main.Brain;
import main.GuildInfo;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.EmbedBuilder;
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
		IChannel channel = Brain.cli.getChannelByID( 359566654478483456L ); // REAL // 367738662043254784 Bot_testng // 359566654478483456L General
		
		/*//Testing
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
		next.set( Calendar.HOUR_OF_DAY, 7 ); // When I go to bed...
		next.set( Calendar.MINUTE, 0 );
		next.set( Calendar.SECOND, 0 );
		Variables.guildIndex.get( guild ).settings.get( channel ).put( "buildSeasonCountdown", next );


	}

	public void FRCCountdown() { // This is all hardcoded shit
		/* Stop Build Day
		 * 02/20/2018 - 11:59pm
		 */
		SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd");
		SimpleDateFormat prettyFormat = new SimpleDateFormat("YYYY MMMM DD, HH:mm", Locale.US); // 2018 January 5, 12:53

		String endSeason = "2018 02 20"; // real
		String startSeason = "2018 01 06"; //real
//		String startSeason = "2018 01 01"; // fake
		long endLength = 0;
		long startLength = 0;

		Date now = new Date();

		// Real
		IGuild guild = Brain.cli.getGuildByID( 359566653987487744L ); // REAL
		IChannel channel = Brain.cli.getChannelByID( 359566654478483456L ); // REAL // 367738662043254784 Bot_testng // 359566654478483456L General
		
		//Testing
		//IGuild guild = Brain.cli.getGuildByID( 366853317709791232L );
		//IChannel channel = Brain.cli.getChannelByID( 384618675841531906L );
		

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
				endLength = TimeUnit.DAYS.convert( (  end.getTime() - now.getTime() ), TimeUnit.MILLISECONDS);
				startLength = TimeUnit.DAYS.convert( ( now.getTime()- start.getTime() ), TimeUnit.MILLISECONDS);
			} catch( ParseException e ) {
				e.printStackTrace();
			}
			
			EmbedBuilder embed = new EmbedBuilder(); // The pretty thing
			
			embed.withTitle("FRC 604");
			embed.withUrl("http://604robotics.com/");
			embed.withDescription("Quixilver");
			
			embed.withAuthorName("Build Season Countdown");
			embed.withAuthorUrl("https://github.com/InfinityPhase/CARIS");
			embed.withAuthorIcon("https://png.icons8.com/metro/100/8e44ad/timer.png");
			
			embed.appendField("Days Passed", startLength + " Days", true);
			embed.appendField("Days Remaining", endLength + " Days", true);
			
			embed.withFooterText("Lets Go! | " + prettyFormat.format(now) );
			embed.withColor(255, 0, 255); // Magenta, anyone?
			//embed.withTimestamp(); // NOPE, lets do this my way
			
			Brain.log.indent(1).log("SENDING BUILD SEASON THING");
			BotUtils.sendMessage( channel, embed ); // Send the only beautiful thing to its doom in hell

			// Send the backup message
			//BotUtils.sendMessage( channel, "Build season continues!\n" + startLength + " days have passed from the start of build season.\nThere are " + endLength + " days remaining.");

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
