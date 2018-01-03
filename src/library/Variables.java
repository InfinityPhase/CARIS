package library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import main.Brain;
import main.GuildInfo;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import utilities.DataSaver;
import utilities.Database;

public class Variables {		
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
			add("guildID integer PRIMARY KEY NOT NULL"); // The primary id, what it says on the tin.
			
			// Keys to the kingdom
			add("modules_id integer NOT NULL"); 
			add("polls_id integer NOT NULL"); 
			add("locations_id integer NOT NULL"); // Are locations even used?
			add("people_id integer NOT NULL");
			add("translator_id integer NOT NULL"); 
			add("userIndex_id integer NOT NULL"); // Make this table set of stuff and pain
			add("reminders_id integer NOT NULL"); 
			add("blacklist_id integer NOT NULL"); 
			add("whitelist_id integer NOT NULL");
			
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

	// Maps the ID of a guild to the int id
	private static Map< Long, Integer > guildID = new HashMap< Long, Integer >();

	/* Functions to get important shit from the database */
	// Maybe should be private?
	// Rename everything to be better

	public static IChannel getChannel( String key ) {
		return getChannel( new Long( key ) ); // Pls be right
	}

	public static IChannel getChannel( long key ) {
		return Brain.cli.getChannelByID( key );
	}

	public int getGuildIndex( IGuild guild ) { // Maybe private?
		return guildID.get( guild.getLongID() ); // Gets the id for the database, pass to other functions
	}

	public Map<String, Boolean> getGuildModules( int id ) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();

		ResultSet rs = server.query( "SELECT * FROM Modules WHERE id = " + server.query( "SELECT modules FROM GuildInfo WHERE id = " + id ) ); // Theoretically will work // Nope. See getPeople

		try { // Move to Database class for abstraction
			while( rs.next() ) {
				result.put( rs.getString( "name" ), rs.getBoolean( "status" ) );
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public Map<String, String> getPeople( int id ) {
		Map<String, String> result = new HashMap<String, String>();

		// First, gets the id for the People table, which represents a java map of String to String.
		// Then, converts the ResultSet into an ArrayList, of which the 0 index is used
		// Last, that is passed to the People table, where the ResultSet is retrieved
		ResultSet rs = server.query( "SELECT * FROM People WHERE id = " + server.convert( server.query( "SELECT people FROM GuildInfo WHERE id = " + id ), "" ).get( 0 ) );

		try {
			while( rs.next() ) {
				result.put( rs.getString( "key" ), rs.getString( "value" ) );
			}
		} catch( SQLException e ) {
			e.printStackTrace();
		}

		return result;
	}

	public Map<String, String> getTranslator( int id ){
		Map<String, String> result = new HashMap<String, String>();

		return result;
	}

	/* Functions to interpret the shit returned from the database */

	public String translate( IGuild guild, String query ) {
		// TODO: rename this function
		// All the functions, actually
		return getTranslator( getGuildIndex( guild ) ).get( query );
	}

	public String people( IGuild guild, String query ) {
		return getPeople( getGuildIndex( guild ) ).get( query );
	}
	
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

