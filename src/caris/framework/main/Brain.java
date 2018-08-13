package caris.framework.main;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;

import caris.framework.basehandlers.Handler;
import caris.framework.events.EventManager;
import caris.framework.library.Constants;
import caris.framework.music.GuildMusicManager;
import caris.framework.utilities.BotUtils;
import caris.framework.utilities.Logger;
import lavaplayer.player.AudioPlayerManager;
import lavaplayer.player.DefaultAudioPlayerManager;
import lavaplayer.source.AudioSourceManagers;
import sx.blah.discord.api.IDiscordClient;


public class Brain {

	public static Map<String, Handler> handlers = new HashMap<String, Handler>();

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
			Logger.error("Please pass the TOKEN as the first argument.");
			Logger.error("# java -jar SimpleResponder.jar TOKEN");
			System.exit(0);
		}

		// Gets token from arguments
		String token = args[0];

		cli = BotUtils.getBuiltDiscordClient(token);
		Logger.print("Client built successfully.");

		cli.getDispatcher().registerListener( eventManager );
		Logger.print("Listener established successfully.");

		// Only login after all event registering is done
		cli.login();
		Logger.print("Client logged in.");
		Logger.print("Loaded Channel Map.");

		cli.changePlayingText(Constants.DEFAULT_PLAYING_TEXT);

		while( !cli.isReady() ) {
			// Wait to do anything else
		}

		while( true ) {
			current = Calendar.getInstance();
		}
	}

	public static void init() { // add handlers to their appropriate categories here
		Logger.print("Initializing.");

		// Build Season Time

		// Music
		musicManagers = new HashMap<>();

		playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
		
		// Load Responder modules
		Logger.print("Loading Handlers...", 1);
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
				Logger.print("Adding " + h.name + " to the Handler Map", 2);
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
				Logger.print("Adding " + h.name + " to the Handler Map", 2);
				handlers.put( h.name, h );
			}
		}
		
		Logger.print("Loaded Handlers:", 1);
		for( String s : handlers.keySet() ) {
			Logger.print(s, 2);
		}
		Logger.print("Initialization complete.");
	}
}
