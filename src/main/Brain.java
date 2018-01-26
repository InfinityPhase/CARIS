package main;

import java.util.Calendar;
import java.util.HashMap;
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
	 */

	public static TokenParser tp = new TokenParser();
	public static Logger log = new Logger().setDefaultIndent(0).build();

	public static Map<String, SuperEvent> eventModules = new HashMap<String, SuperEvent>();
	public static Map<String, Memory> memoryModules = new HashMap<String, Memory>();
	public static HashMap<String, Invoker> invokerModules = new HashMap<String, Invoker>();
	public static HashMap<String, Responder> responderModules = new HashMap<String, Responder>();
	public static HashMap<String, Controller> controllerModules = new HashMap<String, Controller>();

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

		for( String s : eventModules.keySet() ) {
			SuperEvent e = eventModules.get( s );
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

		// Invoker Map
		Reflections invokerReflect = new Reflections("invokers");
		for( Class<?> c : invokerReflect.getSubTypesOf( invokers.Invoker.class ) ) {
			Invoker i = null;
			try {
				i = (Invoker) c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
						
			if( ( i != null ) && ( i.status == Status.ENABLED ) && !contains( i.name, Constants.DISABLED ) ) { // Java does not short-circut, so this is safe
				log.indent(2).log("Adding " + i.name + " to the invokerModule map");
				invokerModules.put( i.name + " Invoker",  i);
				Variables.commandPrefixes.add( i.prefix );
			}
		}
		
		log.indent(2).log("Loaded CommandPrefixes:");
		for( String s : Variables.commandPrefixes ) {
			log.indent(3).log(s); // I THINK THIS IS RELATED TODO NOTE AUGH WTF
		}

		// Responder Map
		responderModules.put("Mention Responder", mentionResponder);
		responderModules.put("Nickname Responder", nicknameResponder);
		responderModules.put("Reminder Responder", reminderResponder);
		responderModules.put("Location Responder", locationResponder);
		responderModules.put("Help Responder", helpResponder);

		// Controller Map
		controllerModules.put("Module Controller", moduleController);
		controllerModules.put("Save Controller", saveController);
		controllerModules.put("Channel List Controller", channelListController);
		controllerModules.put("Say Controller", sayController);
		controllerModules.put("Status Controller", statusController);
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
