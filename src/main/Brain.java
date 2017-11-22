package main;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import commands.CalendarHandler;
import commands.MessageReceived;
import commands.SuperEvent;

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
import library.Variables;
import memories.AuthorMemory;
import memories.Memory;
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
	 */
	
	public static TokenParser tp = new TokenParser();
	public static Logger log = new Logger();

	public static Map<String, SuperEvent> eventModules = new HashMap<String, SuperEvent>();
	public static Map<String, Memory> memoryModules = new HashMap<String, Memory>();
	public static HashMap<String, Invoker> invokerModules = new HashMap<String, Invoker>();
	public static HashMap<String, Responder> responderModules = new HashMap<String, Responder>();
	public static HashMap<String, Controller> controllerModules = new HashMap<String, Controller>();
	
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
	
	/* Admin Controllers */
	public static ModuleController moduleController = new ModuleController();
	public static MessageReceived commandHandler = new MessageReceived();
	
	/* Gigantic Variable Library */
	public static SaveController saveController = new SaveController();
	
	public static CalendarHandler calendarHandler = new CalendarHandler();
	public static Calendar current = Calendar.getInstance();
	
	public static void main(String[] args) {

		init();

		if (!(args.length >= 1)) {
			log.out("Please pass the TOKEN as the first argument.");
			log.out("# java -jar SimpleResponder.jar TOKEN");
			System.exit(0);
		}
		
		// Gets token from arguments
		String token = args[0];

		IDiscordClient cli = BotUtils.getBuiltDiscordClient(token);
		log.debugOut("Client built successfully.");
		
		for( String s : eventModules.keySet() ) {
			SuperEvent e = eventModules.get( s );
			cli.getDispatcher().registerListener( e );
		}
		
		for( String s : memoryModules.keySet() ) {
			Memory m = memoryModules.get( s );
			cli.getDispatcher().registerListener( m );
		}

		log.debugOut("Listener established successfully.");
		
		// Only login after all event registering is done
		cli.login();
		log.debugOut("Client logged in.");
		
		load(cli);
		log.debugOut("Loaded Channel Map.");
		
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
	
	public static void init() { // add handlers to their appropriate categories here
		log.debugOut("Initializing.");
		
		// Event Map
		eventModules.put("Command Handler", commandHandler);
		
		// Memory Map
		memoryModules.put("Author Memory", authorMemory);

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
	}
}
