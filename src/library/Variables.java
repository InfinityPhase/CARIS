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
import tokens.Reminder;
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
	 * 
	 * An idea for the future is to make this extendable, so that there are no needed
	 * imports from the rest of caris. That would be smoother, and more organized than
	 * this mess of code and functions...
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
		// TODO All this shit needs replacing with arrays, instead of lists

		server.makeTable( "Guild", new ArrayList<String>() {{
			add("guild_id integer PRIMARY KEY NOT NULL"); // The primary id, what it says on the tin.

			// Keys to the kingdom
			// I wonder if all of these could be replaced with one collumn, that could be linked to each required table...
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

	/* Adds actual guilds to the things */

	public void addGuild( IGuild guild ) {
		addGuild( guild.getStringID() );
	}

	public void addGuild( long guild ) {
		addGuild( String.valueOf(guild) );
	}

	public void addGuild( String guild ) {

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
	
	public String getPerson( IGuild guild, String name ) {
		return getPerson( guild.getStringID(), name );
	}
	
	public String getPerson( long guild, String name ) {
		return getPerson( String.valueOf(guild), name );
	}
	
	public String getPerson( String guild, String name ) {
		try { // Checks for two collumns, translator_id and name, returns othername
			// Could make the majority of this line a function, see translator...
			return server.query( "SELECT place FROM People WHERE people_id = " + server.query( "SELECT people_id FROM Guild WHERE guild_id = " + guild + ";" ).getString("people_id") + " person = " + name +";" ).getString("place");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public String translate( IGuild guild, String name ) {
		return translate( guild.getStringID(), name );
	}
	
	public String translate( long guild, String name ) {
		return translate( String.valueOf(guild), name );
	}
	
	public String translate( String guild, String name ) {
		try { // Checks for two collumns, translator_id and name, returns othername
			return server.query( "SELECT otherName FROM Translator WHERE translator_id = " + server.query( "SELECT translator_id FROM Guild WHERE guild_id = " + guild + ";" ).getString("translator_id") + " name = " + name +";" ).getString("otherName");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "";
	}

	public Map<String, Reminder> getReminders( IGuild guild ){
		return getReminders( guild.getStringID() );
	}

	public Map<String, Reminder> getReminders( long guild ) {
		return getReminders( String.valueOf( guild ) );
	}

	public Map<String, Reminder> getReminders( String guild ) {
		Map<String, Reminder> result = new HashMap<String, Reminder>();

		try {
			String id = server.query( "SELECT reminders_id FROM Guild WHERE guild_id = " + guild + ";" ).getString("reminders_id");
			ResultSet times_rs = server.query( "SELECT * FROM Reminders WHERE reminders_id = " + id + ";" );
			while( times_rs.next() ) {
				// Who loves one liners? We love one liners!
				// Basically is three queries fed into a new Reminder object, and stored with the time as the key
				result.put( times_rs.getString("time"), new Reminder( server.query( "SELECT message FROM ReminderData WHERE reminderData_id = " + times_rs.getString("reminderData_id") ).getString("message"), server.query( "SELECT author FROM ReminderData WHERE reminderData_id = " + times_rs.getString("reminderData_id") ).getString("author"), server.query( "SELECT channelID FROM ReminderData WHERE reminderData_id = " + times_rs.getString("reminderData_id") ).getString("channelID") ) );
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

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
	
	public void addTranslation( IGuild guild, String name, String otherName ) {
		addTranslation( guild.getStringID(), name, otherName);
	}
	
	public void addTranslation( long guild, String name, String otherName ) {
		addTranslation( String.valueOf(guild), name, otherName);
	}
	
	public void addTranslation( String guild, String name, String otherName ) {
		// TODO make this prevent duplicate entries
		String translator_id = "";
		
		try {
			translator_id = server.query( "SELECT translator_id FROM guild WHERE guild_id = " + guild + ";" ).getString("translator_id");
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

	public void addReminder( IGuild guild, String time, Reminder remind ) {
		addReminder( guild.getStringID(), time, remind.message, remind.author, remind.channelID );
	}

	public void addReminder( IGuild guild, String time, IMessage message, IUser author, IChannel channel ) {
		addReminder( guild.getStringID(), time, message.getContent(), author.getStringID(), channel.getStringID() ); // TODO: Is it really storing ID, or the username?!?!?!
	}

	public void addReminder( IGuild guild, String time, String message, String author, String channel ) {
		addReminder( guild.getStringID(), time, message, author, channel );
	}

	public void addReminder( String guild, String time, Reminder remind ) {
		addReminder( guild, time, remind.message, remind.author, remind.channelID );
	}

	public void addReminder( String guild, String time, IMessage message, IUser author, IChannel channel ) {
		addReminder( guild, time, message.getContent(), author.getStringID(), channel.getStringID() ); // TODO: Is it really storing ID, or the username?!?!?!
	}

	public void addReminder( String guild, String time, String message, String author, String channel ) {
		String reminders_id = "";
		String reminderData_id = "";

		try {
			reminders_id = server.query( "SELECT reminders_id FROM Guild WHERE guild_id = " + guild + ";" ).getString("reminders_id");
			reminderData_id = String.valueOf( server.query( "SELECT MAX(reminderData_id) FROM Reminders" ).getLong("reminderData_id") + 1 ); // Gets the biggest reminderData_id and adds one, then makes it a string
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

		/*
		try {
			server.insert( "Reminders", new LinkedList<String>() {{ // IMPORTANT: Is a LinkedList
				add("reminders_id"); add("time"); add("reminderData_id"); 
			}}, new LinkedList<String>() {{
				add(server.query( "SELECT reminders_id FROM Guild WHERE guild_id = " + guild + ";" ).getString("reminders_id")); add(time); add(reminderData_id); // Generate reminderData_id
			}});
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try { // TODO Fix this blatant abuse of a try catch
			server.insert( "ReminderData", new LinkedList<String>() {{
				add("reminderData_id"); add("message"); add("author"); add("channelID"); 
			}}, new LinkedList<String>() {{
				add(server.query( "SELECT reminderData_id FROM Reminders WHERE reminders_id = " + server.query( "SELECT reminders_id FROM Guild WHERE guild_id = " + guild + ";" ).getString("reminders_id") ).getString("reminderData_id")); add(message); add(author); add(channel);
			}});
		} catch (SQLException e) {

			e.printStackTrace();
		}
		 */
	}

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

}

