package library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import main.Brain;
import main.GuildInfo;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import tokens.NullReminderIDException;
import tokens.Reminder;
import utilities.Database;

public class Variables {
	/*
	 * So, this class is intresting. The origional purpose was to hold all volatile data
	 * in one location, such as the GuildIndex. This was for organizational purposes to
	 * allow for easier extendability.
	 * 
	 * Now, this class provides for the ability to access the data stored in the database,
	 * without complexity elsewhere. That said, this class needs some reorganizing, for
	 * cleanup. But it should work, for now.
	 * 
	 * The database is accessed using the Database class, which provides a wrapper to
	 * the currently used database software, SQLite.
	 * 
	 * An idea for the future is to make this extendable, so that there are no needed
	 * imports from the rest of caris. That would be smoother, and more organized than
	 * this mess of code and functions...
	 */

	/* NOTE
	 * If ResultSet error, https://stackoverflow.com/a/5841485
	 */

	private static Database server = null;
	private static SimpleDateFormat format = new SimpleDateFormat("YYYYMMddhhmmss", Locale.ENGLISH);


	// Dynamic global variables

	/* Gigantic Variable Library */
	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>(); /* Stores info about each guild for caris */

	/* Global Utilities */
	public static List<String> commandPrefixes = new ArrayList<String>();
	public static List<String> commandExacts = new ArrayList<String>();

	public static void init() {
		// Connect to database
		init( Constants.DATABASE_FILE );
	}

	public static void init( String file ) {

		server = new Database( file );

		/* Set the nessessary PRAGMA */

		server.setPragma( "foreign_keys", true ); // Make sure tables that are dependent on others work right

		/* Create tables, collumns, if nessessary */
		// TODO All this shit needs replacing with arrays, instead of lists

		server.makeTable( "Guild", new String[] {
				"guild_id integer PRIMARY KEY NOT NULL", // The primary id, what it says on the tin.

				// Keys to the kingdom
				// I wonder if all of these could be replaced with one collumn, that could be linked to each required table...
				"modules_id integer NOT NULL", "polls_id integer NOT NULL", 
				"locations_id integer NOT NULL", "people_id integer NOT NULL", "translator_id integer NOT NULL",
				"userIndex_id integer NOT NULL", "reminders_id integer NOT NULL", "blacklist_id integer NOT NULL",
				"whitelist_id integer NOT NULL", 

				// Don't need to refrence other tables
				"pollBuilder text NOT NULL", "moduleStatusBuilder text NOT NULL", "logChannel Integer", // Some of these may actually be null. I dunno.

				// It seems all these lines must be placed last, for whatever reason. Argh.
				"FOREIGN KEY (modules_id) REFERENCES Modules(modules_id)", "FOREIGN KEY (polls_id) REFERENCES Polls(polls_id)",
				"FOREIGN KEY (locations_id) REFERENCES Locations(locations_id)", "FOREIGN KEY (people_id) REFERENCES People(people_id)",
				"FOREIGN KEY (translator_id) REFERENCES Translator(translator_id)", "FOREIGN KEY (userIndex_id) REFERENCES UserIndex(userIndex_id)",
				"FOREIGN KEY (reminders_id) REFERENCES Reminders(reminders_id)", "FOREIGN KEY (blacklist_id) REFERENCES Blacklist(blacklist_id)",
				"FOREIGN KEY (whitelist_id) REFERENCES Whitelist(whitelist_id)"
		});

		server.makeTable( "Modules", new String[] {
				"modules_id integer PRIMARY KEY NOT NULL",
				"name text NOT NULL UNIQUE", "status integer NOT NULL" // See https://stackoverflow.com/a/843786 for why status is an integer, not a boolean
		});

		server.makeTable( "Polls", new String[] {
				"polls_id integer PRIMARY KEY NOT NULL",
				"name text NOT NULL UNIQUE", "poll text NOT NULL"
		});

		server.makeTable( "Locations", new String[] {
				"locations_id integer PRIMARY KEY NOT NULL",
				"name text NOT NULL UNIQUE",
				"place_id integer NOT NULL", "FOREIGN KEY (place_id) REFERENCES Location_P2(place_id)"
		});

		server.makeTable( "Location_P2", new String[] {
				"place_id integer PRIMARY KEY NOT NULL",
				"name text NOT NULL"
		});

		server.makeTable( "People", new String[] {
				"people_id integer PRIMARY KEY NOT NULL",
				"person text NOT NULL UNIQUE", "place text NOT NULL",
		});

		server.makeTable( "Translator", new String[] {
				"translator_id integer PRIMARY KEY NOT NULL",
				"name text NOT NULL", "otherName text NOT NULL"
		});

		server.makeTable( "UserIndex", new String[] {
				"userIndex_id integer PRIMARY KEY NOT NULL",
				"userID integer NOT NULL UNIQUE",
				"userInfo_id integer NOT NULL", "FOREIGN KEY (userInfo_id) REFERENCES UserInfo(userInfo_id)" // Maybe this can be temporarily null? 
		});

		server.makeTable( "Reminders", new String[] {
				"reminders_id integer PRIMARY KEY NOT NULL",
				"time text NOT NULL",
				"reminderData_id integer NOT NULL", "FOREIGN KEY (reminderData_id) REFERENCES ReminderData(reminderData_id)"
		});

		server.makeTable( "ReminderData", new String[] {
				"reminderData_id integer PRIMARY KEY NOT NULL",
				"message text NOT NULL", "author text NOT NULL", "channelID text NOT NULL"
		});

		server.makeTable( "Blacklist", new String[] {
				"blacklist_id integer PRIMARY KEY NOT NULL",
				"channelID integer NOT NULL UNIQUE"
		});

		server.makeTable( "Whitelist", new String[] {
				"whitelist_id integer PRIMARY KEY NOT NULL",
				"channelID integer NOT NULL UNIQUE"
		});

	}

	/* Adds actual guilds to the things */

	public static void addGuild( IGuild guild ) {
		addGuild( guild.getStringID() );
	}

	public static void addGuild( long guild ) {
		addGuild( String.valueOf(guild) );
	}

	public static void addGuild( String guild ) {

	}

	/* Functions to get important shit from the database */

	/* NEED TO BE ABLE TO:
	 * modules and status
	 * Polls
	 * Locations
	 * People Done
	 * Transator Done
	 * UserIndex
	 * Reminders Done
	 * Blacklist Done
	 * Whitelist Done
	 * PollBuilder May not need
	 * moduleStatusBuilder May not need
	 * logChannel GS Done
	 */

	public static String getPerson( IGuild guild, String name ) {
		return getPerson( guild.getStringID(), name );
	}

	public static String getPerson( long guild, String name ) {
		return getPerson( String.valueOf(guild), name );
	}

	public static String getPerson( String guild, String name ) {
		try { // Checks for two collumns, translator_id and name, returns othername
			// Could make the majority of this line a function, see translator...
			return server.query( "SELECT place FROM People WHERE people_id = " + server.query( "SELECT people_id FROM Guild WHERE guild_id = " + guild + ";" ).getString("people_id") + " person = " + name +";" ).getString("place");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String translate( IGuild guild, String name ) {
		return translate( guild.getStringID(), name );
	}

	public static String translate( long guild, String name ) {
		return translate( String.valueOf(guild), name );
	}

	public static String translate( String guild, String name ) {
		try { // Checks for two collumns, translator_id and name, returns othername
			return server.query( "SELECT otherName FROM Translator WHERE translator_id = " + server.query( "SELECT translator_id FROM Guild WHERE guild_id = " + guild + ";" ).getString("translator_id") + " name = " + name +";" ).getString("otherName");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static Map<String, Reminder> getReminders( IGuild guild ){
		return getReminders( guild.getStringID() );
	}

	public static Map<String, Reminder> getReminders( long guild ) {
		return getReminders( String.valueOf( guild ) );
	}

	public static Map<String, Reminder> getReminders( String guild ) {
		Map<String, Reminder> result = new HashMap<String, Reminder>();
		String reminderData_id = "";

		try {
			ResultSet guildID_rs = server.query( "SELECT reminders_id FROM Guild WHERE guild_id = " + guild + ";" );
			if( guildID_rs.next() ) { // Alright. This is really important, because an error will be thrown if there is no guild_id
				String id = guildID_rs.getString( "reminders_id" );
				guildID_rs.close();
				ResultSet times_rs = server.query( "SELECT * FROM Reminders WHERE reminders_id = " + id + ";" );
				
				if( times_rs.next() ) {
					reminderData_id = times_rs.getString("reminderData_id");
				} else {
					return null;
				}
				
				while( times_rs.next() ) {
					// Who loves one liners? We love one liners!
					// Basically is three queries fed into a new Reminder object, and stored with the time as the key
					ResultSet message_rs = server.query( "SELECT message FROM ReminderData WHERE reminderData_id = " + reminderData_id );
					ResultSet author_rs = server.query( "SELECT author FROM ReminderData WHERE reminderData_id = " + reminderData_id );
					ResultSet channelID_rs = server.query( "SELECT channelID FROM ReminderData WHERE reminderData_id = " + reminderData_id );
					
					if( message_rs.next() && author_rs.next() && channelID_rs.next() ) {
						result.put( times_rs.getString("time"), new Reminder( message_rs.getString("message"), author_rs.getString("author"), channelID_rs.getString("channelID") ) ); // TODO Also return the ID of the reminder
					} else {
						return null;
					}
				}
				times_rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static List<Long> getWhitelist( IGuild guild ) {
		return getWhitelist( guild.getStringID() );
	}

	public static List<Long> getWhitelist( long guild ){
		return getWhitelist( String.valueOf(guild) );
	}

	public static List<Long> getWhitelist( String guild ){
		ArrayList<Long> result = new ArrayList<Long>();
		ResultSet rs = server.query( "SELECT whitelist_id FROM guild WHERE guild_id = " + guild );

		try {
			ResultSet rs2 = server.query( "SELECT channelID FROM Whitelist WHERE whitelist_id = " + rs.getString( "whitelist_id" ) + ";" );
			while( rs2.next() ) {
				result.add( rs2.getLong( "channelID" ) );
			}
		} catch( SQLException e ) {
			e.printStackTrace();
		}

		return result;
	}

	public static List<Long> getBlacklist( IGuild guild ) {
		return getBlacklist( guild.getStringID() );
	}

	public static List<Long> getBlacklist( long guild ){
		return getBlacklist( String.valueOf(guild) );
	}

	public static List<Long> getBlacklist( String guild ){
		ArrayList<Long> result = new ArrayList<Long>();
		ResultSet rs = server.query( "SELECT blacklist_id FROM Guild WHERE guild_id = " + guild );

		try {
			ResultSet rs2 = server.query( "SELECT channelID FROM Whitelist WHERE blacklist_id = " + rs.getString( "blacklist_id" ) + ";" );
			while( rs2.next() ) {
				result.add( rs2.getLong( "channelID" ) );
			}
		} catch( SQLException e ) {
			e.printStackTrace();
		}

		return result;
	}

	public static long getLogChannel( IGuild guild ) {
		return getLogChannel( guild.getLongID() );
	}

	public static long getLogChannel( String guild ) {
		return getLogChannel( new Long( guild ) );
	}
	
	public static List<String> toolPrefixes = new ArrayList<String>();

	public static long getLogChannel( long guild ) {
		ResultSet rs = server.query( "SELECT logChannel FROM Guild WHERE guild_id = " + guild );
		try {
			if( rs.next() ) {
				return rs.getLong( "logChannel" );
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; // I don't want to use an object, so have a negative
	}

	/* ================================================================================ */
	/* Functions to set values in the database */
	/* ================================================================================ */
	/* TODO
	 * Make sure we use insertReplace when the entry should be unique
	 *   Or just do a simple check 
	 */

	public static void addPersonLocation( IGuild guild, String name, String place ) {
		addPersonLocation( guild.getStringID(), name, place );
	}

	public static void addPersonLocation( long guild, String name, String place ) {
		addPersonLocation( String.valueOf(guild), name, place);
	}

	public static void addPersonLocation( String guild, String name, String place ) {
		// NOTE I... think this will work. Maybe.
		String person_id = ""; // TODO: MAke sure no duplicate entries (like a map)
		ResultSet guild_id = server.query( "SELECT person_id FROM guild WHERE guild_id = " + guild + ";" );

		try {
			if( guild_id.next() ) {
				person_id = guild_id.getString("person_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		server.insertReplace( guild, new String[] {
				"person_id", "person", "place"
		}, new String[] {
				person_id, name, place
		});
	}

	public static void addTranslation( IGuild guild, String name, String otherName ) {
		addTranslation( guild.getStringID(), name, otherName);
	}

	public static void addTranslation( long guild, String name, String otherName ) {
		addTranslation( String.valueOf(guild), name, otherName);
	}

	public static void addTranslation( String guild, String name, String otherName ) {
		// TODO make this prevent duplicate entries
		// See addPersonLocation

		String translator_id = "";
		ResultSet guild_id = server.query( "SELECT translator_id FROM guild WHERE guild_id = " + guild + ";" );
		try {
			if( guild_id.next() ) {
				translator_id = guild_id.getString("translator_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		server.insert( "Translator", new String[] {
				"translator_id", "name", "otherName"
		}, new String[] {
				// Data
				translator_id, name, otherName
		});
	}

	public static void addReminder( IGuild guild, String time, Reminder remind ) {
		addReminder( guild.getStringID(), time, remind.message, remind.author, remind.channelID );
	}

	public static void addReminder( IGuild guild, String time, IMessage message, IUser author, IChannel channel ) {
		addReminder( guild.getStringID(), time, message.getContent(), author.getStringID(), channel.getStringID() ); // TODO: Is it really storing ID, or the username?!?!?!
	}

	public static void addReminder( IGuild guild, String time, String message, String author, String channel ) {
		addReminder( guild.getStringID(), time, message, author, channel );
	}

	public static void addReminder( String guild, String time, Reminder remind ) {
		addReminder( guild, time, remind.message, remind.author, remind.channelID );
	}

	public static void addReminder( String guild, String time, IMessage message, IUser author, IChannel channel ) {
		addReminder( guild, time, message.getContent(), author.getStringID(), channel.getStringID() ); // TODO: Is it really storing ID, or the username?!?!?!
	}

	public static void addReminder( String guild, String time, String message, String author, String channel ) {
		String reminders_id = "";
		String reminderData_id = "";
		ResultSet guild_id = server.query( "SELECT reminders_id FROM Guild WHERE guild_id = " + guild + ";" );
		try {
			if( guild_id.next() ) {
				reminders_id = guild_id.getString("reminders_id");
			} else { // If the guild is not found, do nothing
				return;
			}

			ResultSet reminderDataMax = server.query( "SELECT MAX(reminderData_id) FROM Reminders" );
			if( reminderDataMax.next() ) {
				reminderData_id = String.valueOf( reminderDataMax.getLong("reminderData_id") + 1 ); // Gets the biggest reminderData_id and adds one, then makes it a string
			} else {
				return;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		server.insert( "Reminders", new String[]{
				"reminders_id","time","reminderData_id"
		}, new String[]{
				reminders_id, time, reminderData_id
		}
				);

		server.insert( "ReminderData",  new String[] {
				"reminderData_id", "message", "author", "channelID"
		}, new String[] {
				reminderData_id, message, author, channel
		});
	}

	public static void addBlacklist( IGuild guild, IChannel channel ) {
		addBlacklist( guild.getLongID(), channel.getLongID() );
	}

	public static void addBlacklist( long guild, long channel ) {
		addBlacklist( String.valueOf(guild), String.valueOf(channel) ); 
	}

	public static void addBlacklist( String guild, String channel ) {
		ResultSet rs = server.query( "SELECT blacklist_id FROM Guild WHERE guild_id = " + guild + ";" ); // Could make this a one-liner, just for kicks...

		try {
			if( rs.next() ) {
				server.insert( guild,  new String[] {
						rs.getString("blacklist_id"), channel
				});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addWhitelist( IGuild guild, IChannel channel ) {
		addWhitelist( guild.getLongID(), channel.getLongID() );
	}

	public static void addWhitelist( long guild, long channel ) {
		addWhitelist( String.valueOf(guild), String.valueOf(channel) );
	}

	public static void addWhitelist( String guild, String channel ) {
		ResultSet rs = server.query( "SELECT whitelist_id FROM Guild WHERE guild_id = " + guild + ";" ); // Could make this a one-liner, just for kicks...

		try {	
			if( rs.next() ) {
				server.insert( guild,  new String[] {
						rs.getString("whitelist_id"), channel
				});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void setLogChannel( IGuild guild, IChannel channel ) {
		setLogChannel( guild.getLongID(), channel.getLongID() );
	}

	@SuppressWarnings("serial")
	public static void setLogChannel( String guild, String channel ) {		
		server.update( guild, new HashMap<String, String>() {{
			put("logChannel",channel);
		}});
	}

	public static void setLogChannel( long guild, long channel ) {
		setLogChannel( String.valueOf(guild), String.valueOf(channel) );
	}

	/* Functions to interpret the shit returned from the database */

	public static String timeString( Calendar input ) {
		return format.format( input.getTime() );
	}

	public static Calendar toCalendar( String wtf ) {
		Calendar result = Calendar.getInstance();

		try {
			result.setTime( format.parse( wtf ) );
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	/* Remove things from the database */ // TODO: REMOVE THINGS FROM DATABASE

	public static void removeReminder( Reminder reminder ) throws NullReminderIDException {
		if( reminder.reminderID == null ) {
			throw new NullReminderIDException();
		} else {
			removeReminder( reminder.reminderID );
		}
	}

	public static void removeReminder( String reminderID ) {
		//TODO Remove reminder based on id
		ResultSet id = server.query( "SELECT reminderData_id FROM Reminders WHERE rowid = " + reminderID + ";" );
		
		try {
			if( id.next() ) {
				server.remove( "ReminderData", "reminderData_id", id.getString( "reminderData_id" ) ); // Remove all rows that match
				server.remove( "Reminders", "rowid", reminderID );
			}
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	/* I had no idea we could do this... */

	public static IChannel getChannel( String key ) {
		return getChannel( new Long( key ) );
	}

	public static IChannel getChannel( long key ) {
		return Brain.cli.getChannelByID( key );
	}

	public static IGuild getGuild( String key ) {
		return getGuild( new Long( key ) );
	}

	public static IGuild getGuild( long key ) {
		return Brain.cli.getGuildByID( key );
	}

	public static IUser getUser( long user ) {
		return Brain.cli.getUserByID( user );
	}

	public static IUser getUser( String user ) {
		return Brain.cli.getUserByID( new Long( user) );
	}
}

