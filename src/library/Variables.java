package library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		
		/* Create tables, collumns, if nessessary */
		// This is going to SUCK to debug
		// We both know that it will be nessesary, its just a matter of time...
				
		server.makeTable( "GuildID", new HashMap<String, String>() {{ 
			put("id","int"); put("modules","int"); put("polls","int"); put("locations","int"); 
			put("people","int"); put("pollBuilder","String"); put("moduleStatusBuilder","String"); put("translator","int"); 
			put("userIndex","int"); put("reminders","int"); put("logChannel","long"); put("blacklist","int"); 
			put("whitelist","int");
		}} );
		
		server.makeTable( "Modules", new HashMap<String, String>() {{
			put("id","int"); put("name","String"); put("status","Boolean");
		}} );
		
		server.makeTable( "Polls", new HashMap<String, String>() {{
			put("id","int"); put("name","String"); put("poll","String");
		}});
		
		server.makeTable( "Locations", new HashMap<String, String>() {{
			put("id","int"); put("name","String"); put("place","int"); // place links to Location_P2
		}});
		
		server.makeTable( "Location_P2", new HashMap<String, String>() {{
			put("id","int"); put("name","String"); // Used by Locations to emulate nested arraylist
		}});
		
		server.makeTable( "Translator", new HashMap<String, String>() {{
			put("id","int"); put("name","String"); put("otherName","String"); // Rename to be better
		}});
		
		server.makeTable( "UserIndex", new HashMap<String, String>() {{
			put("id","int"); put("userID","long"); put("userInfo","int"); // Need table for userInfo
		}});
		
		server.makeTable( "Reminders", new HashMap<String, String>() {{
			put("id","int"); put("time","String"); put("reminder","int"); // Links string representing calendar to ReminderObject
		}});
		
		server.makeTable( "ReminderObject", new HashMap<String, String>() {{
			put("id","int"); put("message","String"); put("author","String"); put("channelID","String"); 
		}});
		
		server.makeTable( "Blacklist", new HashMap<String, String>() {{
			put("id","int"); put("channelID","long"); // Notice this is the long, not the object
		}});
		
		server.makeTable( "Whitelist", new HashMap<String, String>() {{
			put("id","int"); put("channelID","long");
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

