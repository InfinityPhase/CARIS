package main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.reflections.Reflections;

import commands.CalendarHandler;
import commands.GuildCreate;
import commands.MessageReceived;
import commands.SuperEvent;
import commands.UserJoin;
import controller.ChannelListController;
import controller.Controller;
import controller.ModuleController;
import controller.SaveController;
import controller.SayController;
import controller.StatusController;
import invokers.EchoInvoker;
import invokers.EmbedInvoker;
import invokers.HelpInvoker;
//import invokers.FortuneInvoker;
import invokers.Invoker;
import invokers.LocationInvoker;
import invokers.PollInvoker;
//import invokers.MusicInvoker;
//import invokers.NicknameInvoker;
import invokers.VoteInvoker;
import invokers._8BallInvoker;
import lavaplayer.player.AudioPlayerManager;
import lavaplayer.player.DefaultAudioPlayerManager;
import lavaplayer.source.AudioSourceManagers;
import library.Constants;
import library.Variables;
import memories.AuthorMemory;
import memories.Memory;
import memories.TimeMemory;
import music.GuildMusicManager;
import responders.HelpResponder;
import responders.LocationResponder;
import responders.MentionResponder;
import responders.NicknameResponder;
import responders.ReminderResponder;
import responders.Responder;
import sx.blah.discord.api.IDiscordClient;
import utilities.BotUtils;
import utilities.Logger;
import utilities.TokenParser;
import utilities.Handler.Status;

public class Brain {

	/*	IMPORTANT NOTES
	 * 	- Responders ignore case by setting messages to lower case before parsing
	 *  - Constants cannot be altered; Use for... Constants
	 *  - Variables should be mutable; Use for changing values
	 *  - When a module is altered, update the ID with Eclipse
	 */

	public static TokenParser tp = new TokenParser();
	public static SimpleDateFormat sdf = new SimpleDateFormat( Constants.DATEFORMAT );
	public static Logger log = new Logger().setDefaultIndent(0).build();

	public static Map<String, SuperEvent> eventModules = new HashMap<String, SuperEvent>();
	public static Map<String, Memory> memoryModules = new HashMap<String, Memory>();
	public static HashMap<String, Invoker> invokerModules = new HashMap<String, Invoker>();
	public static HashMap<String, Responder> responderModules = new HashMap<String, Responder>();
	public static HashMap<String, Controller> controllerModules = new HashMap<String, Controller>();
	public static List<Memory> libraryVariables = new ArrayList<Memory>();

	/* Invoked Handlers */
	public static _8BallInvoker _8ballInvoker = new _8BallInvoker();
	public static EchoInvoker echoInvoker = new EchoInvoker();
	public static LocationInvoker locationInvoker = new LocationInvoker();
	public static VoteInvoker voteInvoker = new VoteInvoker();
	public static PollInvoker pollInvoker = new PollInvoker();
	public static EmbedInvoker embedInvoker = new EmbedInvoker();
	public static HelpInvoker helpInvoker = new HelpInvoker();
	//public static NicknameInvoker nicknameInvoker = new NicknameInvoker();
	//public static FortuneInvoker fortuneInvoker = new FortuneInvoker();
	//public static MusicInvoker musicInvoker = new MusicInvoker();

	/* Auto Handlers */
	public static MentionResponder mentionResponder = new MentionResponder();
	public static LocationResponder locationResponder = new LocationResponder();
	public static NicknameResponder nicknameResponder = new NicknameResponder();
	public static ReminderResponder reminderResponder = new ReminderResponder();
	public static HelpResponder helpResponder = new HelpResponder();

	/* Things that think */
	public static AuthorMemory authorMemory = new AuthorMemory();
	public static TimeMemory timeMemory = new TimeMemory();

	/* Admin Controllers */
	public static ModuleController moduleController = new ModuleController();
	public static ChannelListController channelListController = new ChannelListController();
	public static SaveController saveController = new SaveController();
	public static SayController sayController = new SayController();
	public static StatusController statusController = new StatusController();

	/* Event Handlers */
	public static MessageReceived messageReceived = new MessageReceived();
	public static GuildCreate guildCreate = new GuildCreate();
	public static UserJoin userJoin = new UserJoin();

	/* Gigantic Variable Library */	
	public static CalendarHandler calendarHandler = new CalendarHandler();
	public static Calendar current = Calendar.getInstance();

	public static String token = null;

	/* Music Stuff */
	public static AudioPlayerManager playerManager;
	public static Map<Long, GuildMusicManager> musicManagers;

	public static IDiscordClient cli = null;

	public static void main(String[] args) {

		init();

		Variables.init();

		if( args.length <= 0 && System.console() != null ) {
			token = System.console().readPassword("Bot Token: ").toString();
			if( token.length() <= 0 ) {
				log.log("Tokens must be longer than 0 characters.");
				System.exit(1);
			}
		} else {
			if( args.length > 0 ) {
				token = args[0];
			} else {
				log.log("Please pass the TOKEN as the first argument.");
				log.log("# java -jar SimpleResponder.jar TOKEN");
				System.exit(1);
			}
		}

		// Gets token from arguments
		String token = args[0];

		// Get the encryption password
		if( Constants.SAVESTATE && ( System.console() != null ) ) {
			char password[] = System.console().readPassword("Save file password: ");

			if( password.length <= 0 ) {
				log.log("WARNING: Password is empty");
			}
		} else {
			log.log("WARNING: Password is empty");
			@SuppressWarnings("unused")
			char password[] = {};
		}

		if( System.console() != null ) {
			log.log("Requesting save file...");
			String temp = "";
			temp = System.console().readLine("Save file: ");

			if( !temp.isEmpty() ) {
				log.indent(1).log("Not implemented.");
			} else {
				log.indent(1).log("No save file given.");
			}

		}

		cli = BotUtils.getBuiltDiscordClient(token);

		log.log("Client built successfully.");

		for( String s : eventModules.keySet() ) {
			SuperEvent e = eventModules.get( s );
			cli.getDispatcher().registerListener( e );
		}

		log.log("Listener established successfully.");

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

		log.log("Starting timer...");
		log.indent(1).log("Timer set to: " + Constants.SAVETIME);

		while( !cli.isReady() ) {
			// Wait to do anything else
		}

		while( true ) {

			current = Calendar.getInstance();
			calendarHandler.check();

		}

	}

	public static void init() { // add handlers to their appropriate categories here
		log.log("Initializing.");

		// Build Season Time
		Constants.kickoff.set(2018, Calendar.JANUARY, 6, 7, 0, 0);

		// Music
		musicManagers = new HashMap<>();

		playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);

		// Event Map
		eventModules.put("Message Received", messageReceived);
		eventModules.put("Guild Create", guildCreate);
		eventModules.put("User Join", userJoin);

		// Memory Map
		memoryModules.put("Author Memory", authorMemory);
		memoryModules.put("Time Memory", timeMemory);

		// Load Invoker modules
		log.indent(1).log("Loading Invoker Modules...");
		Reflections reflect = new Reflections("invokers");
		for( Class<?> c : reflect.getSubTypesOf( invokers.Invoker.class ) ) {
			Invoker i = null;
			try {
				i = (Invoker) c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			if( ( i != null ) && ( i.status == Status.ENABLED ) && !contains( i.name, Constants.DISABLED_INVOKERS ) ) { // Java does not short-circut, so this is safe
				log.indent(2).log("Adding " + i.name + " to the invokerModule map");
				invokerModules.put( i.name + " Invoker",  i);
				Variables.commandPrefixes.add( i.prefix );
			}
		}

		log.indent(2).log("Loaded Invokers:");
		for( String s : invokerModules.keySet() ) {
			log.indent(3).log(s);
		}

		log.indent(2).log("Loaded CommandPrefixes:");
		for( String s : Variables.commandPrefixes ) {
			log.indent(3).log(s);
		}

		// Load Responder modules
		log.indent(1).log("Loading Responder Modules...");
		reflect = new Reflections("responders");
		for( Class<?> c : reflect.getSubTypesOf( responders.Responder.class ) ) {
			Responder r = null;
			try {
				r = (Responder) c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			if( ( r != null ) && ( r.status == Status.ENABLED ) && !contains( r.name, Constants.DISABLED_INVOKERS ) ) {
				log.indent(2).log("Adding " + r.name + " to the responderModule map");
				responderModules.put( r.name + " Responder", r );
			}
		}

		log.indent(2).log("Loaded Responders:");
		for( String s : responderModules.keySet() ) {
			log.indent(3).log(s);
		}

		// Load Command modules
		log.indent(1).log("Loading Command Modules...");
		reflect = new Reflections("controller");
		for( Class<?> c : reflect.getSubTypesOf( controller.Controller.class ) ) {
			Controller t = null;
			try {
				t = (Controller) c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			if( ( t != null ) && ( t.status == Status.ENABLED ) && !contains( t.name, Constants.DISABLED_INVOKERS ) ) {
				log.indent(2).log("Adding " + t.name + " to the controllerModules map");
				controllerModules.put( t.name + " Controller", t );
			}
		}

		log.indent(2).log("Loaded Controllers:");
		for( String s : controllerModules.keySet() ) {
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

