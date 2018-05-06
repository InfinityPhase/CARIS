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
import lavaplayer.player.AudioPlayerManager;
import lavaplayer.player.DefaultAudioPlayerManager;
import lavaplayer.source.AudioSourceManagers;
import library.Constants;
import library.Variables;
import memories.AuthorMemory;
import memories.Memory;
import memories.TimeMemory;
import modules.Handler.Status;
import modules.constructors.Constructor;
import modules.controllers.Controller;
import modules.invokers.Invoker;
import modules.responders.Responder;
import modules.tools.Tool;
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

	public static Map<String, SuperEvent> eventModules = new HashMap<String, SuperEvent>();
	public static Map<String, Memory> memoryModules = new HashMap<String, Memory>();
	public static HashMap<String, Invoker> invokerModules = new HashMap<String, Invoker>();
	public static HashMap<String, Responder> responderModules = new HashMap<String, Responder>();
	public static HashMap<String, Constructor> constructorModules = new HashMap<String, Constructor>();
	public static HashMap<String, Tool> toolModules = new HashMap<String, Tool>();
	public static HashMap<String, Controller> controllerModules = new HashMap<String, Controller>();
	
	/* Things that think */
	public static AuthorMemory authorMemory = new AuthorMemory();
	public static TimeMemory timeMemory = new TimeMemory();

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

		// Load Responder modules
		log.indent(1).log("Loading Responder Modules...");
		Reflections reflect = new Reflections("modules.responders");
		for( Class<?> c : reflect.getSubTypesOf( modules.responders.Responder.class ) ) {
			Responder r = null;
			try {
				r = (Responder) c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			if( ( r != null ) && ( r.status == Status.ENABLED ) && !contains( r.name, Constants.DISABLED_RESPONDERS ) ) {
				log.indent(2).log("Adding " + r.name + " to the responderModule map");
				responderModules.put( r.name, r );
			}
		}
		
		log.indent(2).log("Loaded Responders:");
		for( String s : responderModules.keySet() ) {
			log.indent(3).log(s);
		}
		
		// Load Invoker modules
		log.indent(1).log("Loading Invoker Modules...");
		reflect = new Reflections("modules.invokers");
		for( Class<?> c : reflect.getSubTypesOf( modules.invokers.Invoker.class ) ) {
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
				invokerModules.put( i.name,  i);
				Variables.commandPrefixes.add( i.prefix );
			}
		}
		
		log.indent(2).log("Loaded Invokers:");
		for( String s : invokerModules.keySet() ) {
			log.indent(3).log(s);
		}
		
		// Load Invoker modules
		log.indent(1).log("Loading Constructor Modules...");
		reflect = new Reflections("modules.constructors");
		for( Class<?> c : reflect.getSubTypesOf( modules.constructors.Constructor.class ) ) {
			Constructor co = null;
			try {
				co = (Constructor) c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			if( ( co != null ) && ( co.status == Status.ENABLED ) && !contains( co.name, Constants.DISABLED_CONSTRUCTORS ) ) { // Java does not short-circut, so this is safe
				log.indent(2).log("Adding " + co.name + " to the constructorModule map");
				constructorModules.put( co.name,  co);
				Variables.commandPrefixes.add( co.prefix );
			}
		}
		
		log.indent(2).log("Loaded Contructors:");
		for( String s : constructorModules.keySet() ) {
			log.indent(3).log(s);
		}
		
		log.indent(2).log("Loaded CommandPrefixes:");
		for( String s : Variables.commandPrefixes ) {
			log.indent(3).log(s);
		}
		
		// Load Tool Modules
		log.indent(1).log("Loading Tool Modules...");
		reflect = new Reflections("modules.tools");
		for( Class<?> c : reflect.getSubTypesOf( modules.tools.Tool.class ) ) {
			Tool t = null;
			try {
				t = (Tool) c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			if( ( t != null ) && ( t.status == Status.ENABLED ) && !contains( t.name, Constants.DISABLED_TOOLS ) ) {
				log.indent(2).log("Adding " + t.name + " to the toolModules map");
				toolModules.put( t.name, t );
				Variables.toolPrefixes.add( t.prefix );
			}
		}
		
		log.indent(2).log("Loaded Tools:");
		for( String s : toolModules.keySet() ) {
			log.indent(3).log(s);
		}
		
		log.indent(2).log("Loaded ToolPrefixes:");
		for( String s : Variables.toolPrefixes ) {
			log.indent(3).log(s);
		}
		
		// Load Command modules
		log.indent(1).log("Loading Command Modules...");
		reflect = new Reflections("modules.controller");
		for( Class<?> c : reflect.getSubTypesOf( modules.controllers.Controller.class ) ) {
			Controller t = null;
			try {
				t = (Controller) c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			if( ( t != null ) && ( t.status == Status.ENABLED ) && !contains( t.name, Constants.DISABLED_CONTROLLERS ) ) {
				log.indent(2).log("Adding " + t.name + " to the controllerModules map");
				controllerModules.put( t.name, t );
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
