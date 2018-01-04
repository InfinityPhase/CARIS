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
import utilities.DataSaver;
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
	 */

	private static Database server = null;
	private static SimpleDateFormat format = new SimpleDateFormat("YYYYMMddhhmmss", Locale.ENGLISH);


	// Dynamic global variables

	/* Gigantic Variable Library */
	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>(); /* Stores info about each guild for caris */

	/* Global Utilities */
	public static DataSaver ds = new DataSaver();

	public static void init() {
		// Connect to database
		init( Constants.DATABASE_FILE );
	}

	@SuppressWarnings("serial")
	public static void init( String file ) {

		server = new Database( file );

		/* Set the nessessary PRAGMA */

		server.setPragma( "foreign_keys", true ); // Make sure tables that are dependent on others work right

		/* Create tables, collumns, if nessessary */

		server.makeTable( "Guild", new ArrayList<String>() {{
			add("guild_id integer PRIMARY KEY NOT NULL"); // The primary id, what it says on the tin.

			// Keys to the kingdom
			// I wonder if all of these could be replaced with one collumn, that could be linked to each required table...
			add("modules_id integer NOT NULL AUTOINCREMENT"); 
			add("polls_id integer NOT NULL AUTOINCREMENT"); 
			add("locations_id integer NOT NULL AUTOINCREMENT"); // Are locations even used?
			add("people_id integer NOT NULL AUTOINCREMENT");
			add("translator_id integer NOT NULL AUTOINCREMENT"); 
			add("userIndex_id integer NOT NULL AUTOINCREMENT"); // Make this table set of stuff and pain
			add("reminders_id integer NOT NULL AUTOINCREMENT"); 
			add("blacklist_id integer NOT NULL AUTOINCREMENT"); 
			add("whitelist_id integer NOT NULL AUTOINCREMENT");

			// Don't need to refrence other tables
			add("pollBuilder text NOT NULL"); add("moduleStatusBuilder text NOT NULL"); add("logChannel Integer"); // Some of these may actually be null. I dunno.

			// It seems all these lines must be placed last, for whatever reason. Argh.
			add("FOREIGN KEY (modules_id) REFERENCES Modules(modules_id)"); add("FOREIGN KEY (polls_id) REFERENCES Polls(polls_id)"); 
			add("FOREIGN KEY (locations_id) REFERENCES Locations(locations_id)"); add("FOREIGN KEY (people_id) REFERENCES People(people_id)");
			add("FOREIGN KEY (translator_id) REFERENCES Translator(translator_id)"); add("FOREIGN KEY (userIndex_id) REFERENCES UserIndex(userIndex_id)"); 
			add("FOREIGN KEY (reminders_id) REFERENCES Reminders(reminders_id)"); add("FOREIGN KEY (blacklist_id) REFERENCES Blacklist(blacklist_id)"); 
			add("FOREIGN KEY (whitelist_id) REFERENCES Whitelist(whitelist_id)");
		}});

		server.makeTable( "Modules", new ArrayList<String>() {{
			add("modules_id integer PRIMARY KEY NOT NULL");
			add("name text NOT NULL"); add("status integer NOT NULL"); // See https://stackoverflow.com/a/843786 for why status is an integer, not a boolean
		}});

		server.makeTable( "Polls", new ArrayList<String>() {{
			add("polls_id integer PRIMARY KEY NOT NULL"); 
			add("name text NOT NULL"); add("poll text NOT NULL");
		}});

		server.makeTable( "Locations", new ArrayList<String>() {{
			add("locations_id integer PRIMARY KEY NOT NULL"); 
			add("name text NOT NULL");
			add("place_id integer NOT NULL"); add("FOREIGN KEY (place_id) REFERENCES Location_P2(place_id)"); 
		}});

		server.makeTable( "Location_P2", new ArrayList<String>() {{ // Rename to Place?
			add("place_id integer PRIMARY KEY NOT NULL"); 
			add("name text NOT NULL");
		}});

		server.makeTable( "People", new ArrayList<String>() {{
			add("people_id integer PRIMARY KEY NOT NULL");
			add("person text NOT NULL"); add("place text NOT NULL");
		}});

		server.makeTable( "Translator", new ArrayList<String>() {{
			add("translator_id integer PRIMARY KEY NOT NULL"); 
			add("name text NOT NULL"); add("otherName text NOT NULL");
		}});

		server.makeTable( "UserIndex", new ArrayList<String>() {{
			add("userIndex_id integer PRIMARY KEY NOT NULL"); 
			add("userID integer NOT NULL"); 
			add("userInfo_id integer NOT NULL"); add("FOREIGN KEY (userInfo_id) REFERENCES UserInfo(userInfo_id)"); // Maybe this can be temporarily null? 
		}});

		server.makeTable( "Reminders", new ArrayList<String>() {{
			add("reminders_id integer PRIMARY KEY NOT NULL"); 
			add("time text NOT NULL"); 
			add("reminderData_id integer NOT NULL"); add("FOREIGN KEY (reminderData_id) REFERENCES ReminderData(reminderData_id)"); 
		}});

		server.makeTable( "ReminderData", new ArrayList<String>() {{
			add("reminderData_id integer PRIMARY KEY NOT NULL"); 
			add("message text NOT NULL"); add("author text NOT NULL"); add("channelID text NOT NULL"); 
		}});

		server.makeTable( "Blacklist", new ArrayList<String>() {{
			add("blacklist_id integer PRIMARY KEY NOT NULL");
			add("channelID integer NOT NULL"); 
		}});

		server.makeTable( "Whitelist", new ArrayList<String>() {{
			add("whitelist_id integer PRIMARY KEY NOT NULL"); 
			add("channelID integer NOT NULL"); 
		}});
	}

	/* Functions to get important shit from the database */

	/* NEED TO BE ABLE TO:
	 * modules and status
	 * Polls
	 * Locations
	 * People
	 * Transator
	 * UserIndex
	 * Reminders
	 * Blacklist
	 * Whitelist
	 * PollBuilder May not need
	 * moduleStatusBuilder May not need
	 * logChannel GS Done
	 */

	// These are old, check validity
	public static IChannel getChannel( String key ) {
		return getChannel( new Long( key ) );
	}

	public static IChannel getChannel( long key ) {
		return Brain.cli.getChannelByID( key );
	}

	public static Map<String, Boolean> getGuildModules( String guildID ) {
		return getGuildModules( new Long(guildID ) );
	}

	public static Map<String, Boolean> getGuildModules( long guildID ) {
		return getGuildModules( null ); // TODO FIX ME
	}

	public static Map<String, Boolean> getGuildModules( int id ) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();

		ResultSet rs = server.query( "SELECT * FROM Modules WHERE modules_id = " + server.query( "SELECT modules_id FROM Guild WHERE guild_id = " + id ) ); // Theoretically will work // Nope. See getPeople

		try { // Move to Database class for abstraction
			while( rs.next() ) {
				result.put( rs.getString( "name" ), rs.getBoolean( "status" ) );
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Map<String, String> getPeople( int id ) {
		Map<String, String> result = new HashMap<String, String>();

		// First, gets the id for the People table, which represents a java map of String to String.
		// Then, converts the ResultSet into an ArrayList, of which the 0 index is used
		// Last, that is passed to the People table, where the ResultSet is retrieved
		ResultSet rs = server.query( "SELECT * FROM People WHERE people_id = " + server.convert( server.query( "SELECT people_id FROM Guild WHERE guild_id = " + id ), "" ).get( 0 ) );

		try {
			while( rs.next() ) {
				result.put( rs.getString( "key" ), rs.getString( "value" ) );
			}
		} catch( SQLException e ) {
			e.printStackTrace();
		}

		return result;
	} // End old functions
	
	public List<Long> getWhitelist( IGuild guild ) {
		return getWhitelist( guild.getStringID() );
	}
	
	public List<Long> getWhitelist( long guild ){
		return getWhitelist( String.valueOf(guild) );
	}
	
	public List<Long> getWhitelist( String guild ){
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
	
	public List<Long> getBlacklist( IGuild guild ) {
		return getBlacklist( guild.getStringID() );
	}
	
	public List<Long> getBlacklist( long guild ){
		return getBlacklist( String.valueOf(guild) );
	}
	
	public List<Long> getBlacklist( String guild ){
		ArrayList<Long> result = new ArrayList<Long>();
		ResultSet rs = server.query( "SELECT blacklist_id FROM guild WHERE guild_id = " + guild );
		
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

	public long getLogChannel( IGuild guild ) {
		return getLogChannel( guild.getLongID() );
	}

	public long getLogChannel( String guild ) {
		return getLogChannel( new Long( guild ) );
	}

	public long getLogChannel( long guild ) {
		ResultSet rs = server.query( "SELECT logChannel FROM Guild WHERE guild_id = " + guild );
		try {
			return rs.getLong( "logChannel" );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; // I don't want to use an object, so have a negative
	}

	/* Functions to set values in the database */

	public void addBlacklist( IGuild guild, IChannel channel ) {
		addBlacklist( guild.getLongID(), channel.getLongID() );
	}

	public void addBlacklist( long guild, long channel ) {
		addBlacklist( String.valueOf(guild), String.valueOf(channel) ); 
	}

	@SuppressWarnings("serial")
	public void addBlacklist( String guild, String channel ) {
		ResultSet rs = server.query( "SELECT blacklist_id FROM Guild WHERE guild_id = " + guild + ";" ); // Could make this a one-liner, just for kicks...

		try {
			server.insert( guild, new ArrayList<String>() {{
				add(rs.getString("blacklist_id")); add(channel);
			}});
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void addWhitelist( IGuild guild, IChannel channel ) {
		addWhitelist( guild.getLongID(), channel.getLongID() );
	}

	public void addWhitelist( long guild, long channel ) {
		addWhitelist( String.valueOf(guild), String.valueOf(channel) );
	}

	@SuppressWarnings("serial")
	public void addWhitelist( String guild, String channel ) {
		ResultSet rs = server.query( "SELECT whitelist_id FROM Guild WHERE guild_id = " + guild + ";" ); // Could make this a one-liner, just for kicks...

		try {
			server.insert( guild, new ArrayList<String>() {{
				add(rs.getString("whitelist_id")); add(channel);
			}});
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void setLogChannel( IGuild guild, IChannel channel ) {
		setLogChannel( guild.getLongID(), channel.getLongID() );
	}

	@SuppressWarnings("serial")
	public void setLogChannel( String guild, String channel ) {		
		server.update( guild, new HashMap<String, String>() {{
			put("logChannel",channel);
		}});
	}

	public void setLogChannel( long guild, long channel ) {
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

}

