package caris.framework.main;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;

import caris.framework.events.EventHandler;
import caris.framework.handlers.Handler;
import commands.CalendarHandler;
import commands.IndependentHandler;
import lavaplayer.player.AudioPlayerManager;
import lavaplayer.player.DefaultAudioPlayerManager;
import lavaplayer.source.AudioSourceManagers;
import library.Constants;
import memories.AuthorMemory;
import memories.Memory;
import memories.TimeMemory;
import music.GuildMusicManager;
import sx.blah.discord.api.IDiscordClient;
import utilities.BotUtils;
import utilities.Logger;
import utilities.TokenParser;

public class Brain {

	/*	IMPORTANT NOTES
	 * 	- Responders ignore case by setting messages to lower case before parsing
	 */

	public static TokenParser tp = new TokenParser();
	public static Logger log = new Logger().setDefaultIndent(0).build();

	public static Map<String, Handler> handlers = new HashMap<String, Handler>();
	public static Map<String, Memory> memoryModules = new HashMap<String, Memory>();
	
	/* Things that think */
	public static AuthorMemory authorMemory = new AuthorMemory();
	public static TimeMemory timeMemory = new TimeMemory();

	/* Event Handlers */
	public static EventHandler eventHandler = new EventHandler();

	/* Gigantic Variable Library */	
	public static Calendar current = Calendar.getInstance();

	/* Music Stuff */
	public static AudioPlayerManager playerManager;
	public static Map<Long, GuildMusicManager> musicManagers;

	public static IDiscordClient cli = null;

	public static void main(String[] args) {

		init();

		if (!(args.length >= 1)) {
			log.log("Please pass the TOKEN as the first argument.");
			log.log("# java -jar SimpleResponder.jar TOKEN");
			System.exit(0);
		}

		// Gets token from arguments
		String token = args[0];

		cli = BotUtils.getBuiltDiscordClient(token);
		log.log("Client built successfully.");

		for( String s : handlers.keySet() ) {
			Handler e = handlers.get( s );
			cli.getDispatcher().registerListener( e );
		}

		for( String s : memoryModules.keySet() ) {
			Memory m = memoryModules.get( s );
			cli.getDispatcher().registerListener( m );
		}

		log.log("Listener established successfully.");

		// Only login after all event registering is done
		cli.login();
		cli.changePlayingText(Constants.DEFAULT_PLAYING_TEXT);

		log.log("Client logged in.");

		log.log("Loaded Channel Map.");

		while( !cli.isReady() ) {
			// Wait to do anything else
		}

		while( true ) {
			current = Calendar.getInstance();
			CalendarHandler.check();
			IndependentHandler.check();
		}
	}

	public static void init() { // add handlers to their appropriate categories here
		log.log("Initializing.");

		// Build Season Time

		// Music
		musicManagers = new HashMap<>();

		playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);

		// Memory Map
		memoryModules.put("Author Memory", authorMemory);
		memoryModules.put("Time Memory", timeMemory);

		// Load Responder modules
		log.indent(1).log("Loading Handlers...");
		Reflections reflect = new Reflections("caris.framework.handlers");
		for( Class<?> c : reflect.getSubTypesOf( caris.framework.handlers.Handler.class ) ) {
			Handler h = null;
			try {
				h = (Handler) c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			if( h != null ) {
				log.indent(2).log("Adding " + h.name + " to the Handler Map");
				handlers.put( h.name, h );
			}
		}
		
		log.indent(2).log("Loaded Handlers:");
		for( String s : handlers.keySet() ) {
			log.indent(3).log(s);
		}
	}

	private static boolean contains( String s, String[] array ) {
		for( int i = 0; i < array.length; i++ ) {
			if( array[i].equalsIgnoreCase(s) ) {
				return true;
			}
		}
		return false;
	}
}
