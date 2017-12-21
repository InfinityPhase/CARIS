package library;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import main.GuildInfo;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import utilities.DataSaver;
import utilities.Database;

public class Variables {		
	private Database server = null;
	
	// Dynamic global variables
	
	/* Gigantic Variable Library */
	public static HashMap<String, IChannel> channelMap = new HashMap<String, IChannel>(); /* Replace String with Long, needed to link id of channel to channel */
	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>(); /* Stores info about each guild for caris */
	
	/* Global Utilities */
	public static DataSaver ds = new DataSaver();
	
	public void init() {
		// Connect to database
		server = new Database();
	}
	
	/* Functions to get important shit from the database */
	// Maybe should be private?
	// Rename everything to be better
	
	public int getGuildIndex( IGuild guild ) {
		return 0; // Gets the id for the database, pass to other functions
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
		
		return result; // get all the things given the id
	}
	
	public Map<String, String> getPeople( int id ) {
		Map<String, String> result = new HashMap<String, String>();
		
		// Oh wow, this line is UGLY
		// First, gets the id for the People table, which represents a java map of String to String.
		// Then, converts the ResultSet into an ArrayList, of which the 0 index is used
		// Last, that is passed to the People table, where the ResultSet is retrieved
		// Supposedly.
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
		return getTranslator( getGuildIndex( guild ) ).get( query ); // Tada, the all in one solution to all the problems
	}
	
	public String people( IGuild guild, String query ) {
		return getPeople( getGuildIndex( guild ) ).get( query );
	}
	
}

