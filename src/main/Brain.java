package main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import commands.CalendarHandler;
import commands.GuildCreate;
import commands.MessageReceived;
import commands.SayController;
import commands.SuperEvent;
import commands.UserJoin;
import controller.ChannelListController;
import controller.Controller;
import controller.ModuleController;
import controller.SaveController;
import invokers.EchoInvoker;
import invokers.FortuneInvoker;
import invokers.Invoker;
import invokers.LocationInvoker;
import invokers.NicknameInvoker;
import invokers.VoteInvoker;
import invokers._8BallInvoker;
import library.Constants;
import library.Variables;
import memories.AuthorMemory;
import memories.Memory;
import memories.TimeMemory;
import responders.LocationResponder;
import responders.MentionResponder;
import responders.NicknameResponder;
import responders.ReminderResponder;
import responders.Responder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import utilities.BotUtils;
import utilities.Logger;
import utilities.TokenParser;

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
	public static EchoInvoker echoInvoker = new EchoInvoker();
	public static LocationInvoker locationInvoker = new LocationInvoker();
	public static VoteInvoker voteInvoker = new VoteInvoker();
	public static _8BallInvoker _8ballInvoker = new _8BallInvoker();
	public static NicknameInvoker nicknameInvoker = new NicknameInvoker();
	public static FortuneInvoker fortuneInvoker = new FortuneInvoker();
	
	/* Auto Handlers */
	public static MentionResponder mentionResponder = new MentionResponder();
	public static LocationResponder locationResponder = new LocationResponder();
	public static NicknameResponder nicknameResponder = new NicknameResponder();
	public static ReminderResponder reminderResponder = new ReminderResponder();

	/* Things that think */
	public static AuthorMemory authorMemory = new AuthorMemory();
	public static TimeMemory timeMemory = new TimeMemory();

	/* Admin Controllers */
	public static ModuleController moduleController = new ModuleController();
	public static ChannelListController channelListController = new ChannelListController();
	public static SaveController saveController = new SaveController();
	public static SayController sayController = new SayController();
	
	/* Event Handlers */
	public static MessageReceived messageReceived = new MessageReceived();
	public static GuildCreate guildCreate = new GuildCreate();
	public static UserJoin userJoin = new UserJoin();
	
	
	/* Gigantic Variable Library */	
	public static CalendarHandler calendarHandler = new CalendarHandler();
	public static Calendar current = Calendar.getInstance();

	public static String token = null;

	public static void main(String[] args) {	

		init();

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

		// Get the encryption password
		if( Constants.SAVESTATE && ( System.console() != null ) ) {
			char password[] = System.console().readPassword("Save file password: ");

			if( password.length <= 0 ) {
				log.log("WARNING: Password is empty");
			}
		} else {
			log.log("WARNING: Password is empty");
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

		IDiscordClient cli = BotUtils.getBuiltDiscordClient(token);

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

		// Only login after all event registering is done
		cli.login();
		log.log("Client logged in.");

		load(cli);
		log.log("Loaded Channel Map.");

		log.log("Starting timer...");
		log.indent(1).log("Timer set to: " + Constants.SAVETIME);
		
		while( true ) {

			current = Calendar.getInstance();
			calendarHandler.check();

		}

	}

	public static void load(IDiscordClient cli) {
		for( IChannel channel : cli.getChannels() ) {
			Variables.channelMap.put(channel.getStringID(), channel);
		}
	}

	public static void init() { // add hananotherStringdlers to their appropriate categories here
		log.log("Initializing.");

		// Event Map
		eventModules.put("Message Received", messageReceived);
		eventModules.put("Guild Create", guildCreate);
		eventModules.put("User Join", userJoin);
		
		// Memory Map
		memoryModules.put("Author Memory", authorMemory);
		memoryModules.put("Time Memory", timeMemory);

		// Invoker Map
		invokerModules.put("Echo Invoker", echoInvoker);
		invokerModules.put("Vote Invoker", voteInvoker);
		invokerModules.put("8ball Invoker", _8ballInvoker);
		invokerModules.put("Nickname Invoker", nicknameInvoker);
		invokerModules.put("Fortune Invoker", fortuneInvoker);
		invokerModules.put("Location Invoker", locationInvoker);

		// Responder Map
		responderModules.put("Mention Responder", mentionResponder);
		responderModules.put("Nickname Responder", nicknameResponder);
		responderModules.put("Reminder Responder", reminderResponder);
		responderModules.put("Location Responder", locationResponder);

		// Controller Map
		controllerModules.put("Module Controller", moduleController);
		controllerModules.put("Save Controller", saveController);
		controllerModules.put("Channel List Controller", channelListController);
		controllerModules.put("Say Controller", sayController);
	}
	
}

