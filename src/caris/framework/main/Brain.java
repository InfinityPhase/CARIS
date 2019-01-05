package caris.framework.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;

import caris.framework.basehandlers.Handler;
import caris.framework.events.EventManager;
import caris.framework.library.Constants;
import caris.framework.music.GuildMusicManager;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.BotUtils;
import caris.framework.utilities.Logger;
import lavaplayer.player.AudioPlayerManager;
import lavaplayer.player.DefaultAudioPlayerManager;
import lavaplayer.source.AudioSourceManagers;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;


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

	public static boolean emptyReported = true;
	public static ArrayList<Thread> threadQueue = new ArrayList<Thread>();
	
	public static HashMap<Calendar, String> countdownMilestones = new HashMap<Calendar, String>();
	
	public static ArrayList<IChannel> countdownChannels = new ArrayList<IChannel>();
	
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

		while( !cli.isReady() ) {
			// Wait to do anything else
		}

		cli.changePlayingText(Constants.INVOCATION_PREFIX + "Help");
		cli.changeUsername(Constants.NAME);
		
		while( true ) {
			iterate();
		}
	}

	public static void iterate() { // do things. nothing gets past this block.
		current = Calendar.getInstance();
		if( !threadQueue.isEmpty() ) {
			emptyReported = false;
			Logger.debug("Threads in queue: " + threadQueue.size(), true);
			threadQueue.remove(0).run();
		}
		else if( !emptyReported ) {
			Logger.debug("Thread queue empty.", true);
			emptyReported = true;
		}
		for( Calendar c : countdownMilestones.keySet() ) {
			Calendar now = Calendar.getInstance();
			if( now.after(c) ) {
				for( IChannel ch : countdownChannels ) {
					BotUtils.sendMessage(ch, countdownMilestones.get(c));
				}
				c.setTimeInMillis(Long.MAX_VALUE);
			}
		}
	}
	
	public static void init() { // add handlers to their appropriate categories here
		Logger.print("Initializing.");

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
				handlers.put( h.name.toLowerCase(), h );
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
				handlers.put( h.name.toLowerCase(), h );
			}
		}
		
		Logger.print("Loaded Handlers:", 1);
		for( String s : handlers.keySet() ) {
			Logger.print(s, 2);
		}
		
		Logger.print("Loading countdown values.");
		Calendar T30m = Calendar.getInstance();
		T30m.setTimeInMillis(1546700400000L);
		Calendar T20m = Calendar.getInstance();
		T20m.setTimeInMillis(1546701000000L);
		Calendar T10m = Calendar.getInstance();
		T10m.setTimeInMillis(1546701600000L);	
		Calendar T5m = Calendar.getInstance();
		T5m.setTimeInMillis(1546701900000L);
		Calendar T4m = Calendar.getInstance();
		T4m.setTimeInMillis(1546701960000L);
		Calendar T3m = Calendar.getInstance();
		T3m.setTimeInMillis(1546702020000L);
		Calendar T2m = Calendar.getInstance();
		T2m.setTimeInMillis(1546702080000L);
		Calendar T1m = Calendar.getInstance();
		T1m.setTimeInMillis(1546702140000L);
		Calendar T30s = Calendar.getInstance();
		T30s.setTimeInMillis(1546702170000L);
		Calendar T20s = Calendar.getInstance();
		T20s.setTimeInMillis(1546702180000L);
		Calendar T10s = Calendar.getInstance();
		T10s.setTimeInMillis(1546702190000L);
		Calendar T9s = Calendar.getInstance();
		T9s.setTimeInMillis(1546702191000L);
		Calendar T8s = Calendar.getInstance();
		T8s.setTimeInMillis(1546702192000L);
		Calendar T7s = Calendar.getInstance();
		T7s.setTimeInMillis(1546702193000L);
		Calendar T6s = Calendar.getInstance();
		T6s.setTimeInMillis(1546702194000L);
		Calendar T5s = Calendar.getInstance();
		T5s.setTimeInMillis(1546702195000L);
		Calendar T4s = Calendar.getInstance();
		T4s.setTimeInMillis(1546702196000L);
		Calendar T3s = Calendar.getInstance();
		T3s.setTimeInMillis(1546702197000L);
		Calendar T2s = Calendar.getInstance();
		T2s.setTimeInMillis(1546702198000L);
		Calendar T1s = Calendar.getInstance();
		T1s.setTimeInMillis(1546702199000L);
		Calendar T0s = Calendar.getInstance();
		T0s.setTimeInMillis(1546702200000L);
		countdownMilestones.put(T30m, "*Thirty Minutes Until Kickoff!*");
		countdownMilestones.put(T20m, "*Twenty Minutes Until Kickoff!*");
		countdownMilestones.put(T10m, "*Ten Minutes Until Kickoff!*");
		countdownMilestones.put(T5m, "*Five Minutes Until Kickoff!!*");
		countdownMilestones.put(T4m, "*Four Minutes Until Kickoff!!*");
		countdownMilestones.put(T3m, "*Three Minutes Until Kickoff!!*");
		countdownMilestones.put(T2m, "*Two Minutes Until Kickoff!!*");
		countdownMilestones.put(T1m, "*One Minute Until Kickoff!!!");
		countdownMilestones.put(T30s, "***Thirty Seconds!!!***");
		countdownMilestones.put(T20s, "***Twenty Seconds!!!***");
		countdownMilestones.put(T10s, "**__*Ten!!!*__**");
		countdownMilestones.put(T9s, "**__*Nine!!!*__**");
		countdownMilestones.put(T8s, "**__*Eight!!!*__**");
		countdownMilestones.put(T7s, "**__*Seven!!!*__**");
		countdownMilestones.put(T6s, "**__*Six!!!*__**");
		countdownMilestones.put(T5s, "**__*FIVE!!!*__**");
		countdownMilestones.put(T4s, "**__*FOUR!!!*__**");
		countdownMilestones.put(T3s, "**__*THREE!!!*__**");
		countdownMilestones.put(T2s, "**__*TWO!!!*__**");
		countdownMilestones.put(T1s, "**__*ONE!!!*__**");
		countdownMilestones.put(T0s, "**__*H A P P Y  K I C K O F F  E V E R Y O N E ! ! ! ! ! ! ! !*__**");
		Logger.print("Initialization complete.");
	}
}
