package caris.framework.main;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;

import caris.framework.basehandlers.Handler;
import caris.framework.events.EventManager;
import caris.framework.library.Constants;
import caris.framework.memories.AuthorMemory;
import caris.framework.memories.Memory;
import caris.framework.memories.TimeMemory;
import caris.framework.music.GuildMusicManager;
import caris.framework.utilities.BotUtils;
import caris.framework.utilities.Logger;
import lavaplayer.player.AudioPlayerManager;
import lavaplayer.player.DefaultAudioPlayerManager;
import lavaplayer.source.AudioSourceManagers;
import sx.blah.discord.api.IDiscordClient;


public class Brain {

	public static Logger log = new Logger().setDefaultIndent(0).build();

	public static Map<String, Handler> handlers = new HashMap<String, Handler>();
	public static Map<String, Memory> memoryModules = new HashMap<String, Memory>();
	
	/* Things that think */
	public static AuthorMemory authorMemory = new AuthorMemory();
	public static TimeMemory timeMemory = new TimeMemory();

	/* Event Handlers */
	public static EventManager eventManager = new EventManager();

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

		cli.getDispatcher().registerListener( eventManager );

		// TODO: Incorporate these back into the new framework
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
		for( Class<?> c : reflect.getSubTypesOf( caris.framework.basehandlers.Handler.class ) ) {
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
		reflect = new Reflections("caris.modular.handlers");
		for( Class<?> c : reflect.getSubTypesOf( caris.framework.basehandlers.Handler.class ) ) {
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
}
