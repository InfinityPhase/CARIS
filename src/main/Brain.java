package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import library.Constants;
import commands.*;
import controller.*;
import invokers.*;
import library.Variables;
import library.TestCereal;
import memories.*;
import responders.*;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import utilities.BotUtils;
import utilities.Logger;
import utilities.TokenParser;

public class Brain implements Serializable {

	/*	IMPORTANT NOTES
	 * 	- Responders ignore case by setting messages to lower case before parsing
	 *  - Constants cannot be altered; Use for... Constants
	 *  - Variables should be mutable; Use for changing values
	 *  - When a module is altered, update the ID with Eclipse
	 */

	/* Allows for versioning of the class Brain */
	private static final long serialVersionUID = 653133840606620696L;

	public static TokenParser tp = new TokenParser();
	public static SimpleDateFormat sdf = new SimpleDateFormat();
	public static Logger log = new Logger().setDefaultIndent(0).build();

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
	public static ChannelListInvoker channelListInvoker = new ChannelListInvoker();

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
	public static MessageReceived commandHandler = new MessageReceived();
	
	/* Gigantic Variable Library */
	public static SaveController saveController = new SaveController();
	
	public static CalendarHandler calendarHandler = new CalendarHandler();
	public static Calendar current = Calendar.getInstance();
	
	public static String token = null;

	// Because we tend to alter the Constants.SAVESTATE variable
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		TestCereal testCereal = new TestCereal(9001, "Love and Hate");

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

		// Gets token from arguments

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

		IDiscordClient cli = BotUtils.getBuiltDiscordClient(token);
		
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
		log.log("Client logged in.");
		
		load(cli);
		log.log("Loaded Channel Map.");

		// This should run last, after everything else
		// Designed to run tasks based on time
		long startTime = System.currentTimeMillis();
		
		
		while( true ) {

			current = Calendar.getInstance();
			calendarHandler.check();

			if( ( ( System.currentTimeMillis() - startTime ) >= Constants.SAVETIME ) && Constants.SAVESTATE ) {

				log.log("Saving CARIS State...");

				File fileName = new File( ( Constants.PREPENDDATE ? sdf.format( Calendar.getInstance().getTime() ) + "_" : "" ) + Constants.SAVEFILE + Constants.SAVEEXTENTION );
				
				saveState(fileName, testCereal);

				/*ObjectWriter out = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, Visibility.ANY).writer().withDefaultPrettyPrinter();

				// TODO: create array of things that need to be stored
				// Include the invokers map, responders map, and anything else

				for( Invoker h : invokerModules.values() ) {
					try {
						out.writeValue( fileName, h );
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				for( Responder h : responderModules.values() ) {
					try {
						out.writeValue( fileName, h );
					} catch (IOException e) {
						e.printStackTrace();
					}
				}*/

				// Reset the timer
				startTime = System.currentTimeMillis();
			}
		}

	}
	
	public static void load(IDiscordClient cli) {
		for( IChannel channel : cli.getChannels() ) {
			Variables.channelMap.put(channel.getStringID(), channel);
		}
	}
	
	public static void init() { // add handlers to their appropriate categories here
		log.log("Initializing.");
		
		// Event Map
		eventModules.put("Command Handler", commandHandler);
		
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
		invokerModules.put("Channel List Invoker", channelListInvoker);
		
		// Responder Map
		responderModules.put("Mention Responder", mentionResponder);
		responderModules.put("Nickname Responder", nicknameResponder);
		responderModules.put("Reminder Responder", reminderResponder);
		responderModules.put("Location Responder", locationResponder);
		
		// Controller Map
		controllerModules.put("Module Controller", moduleController);
		controllerModules.put("Save Controller", saveController);
	}

	static void saveState( File fileOut, Object testCereal ) {
		// Perfect world, we pass the maps of things here, iterate over. Should work.
		try( ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( fileOut ) ); ){
			// Open JSON file
			// Clear contents if exists
			// Stream data of objects to PGP
			// Save stream to JSON file

			log.log("Test after out, before write");
			out.writeObject( testCereal );
			out.defaultWriteObject();

			log.log("Test after write");

			out.flush();

			out.flush();
		} catch (IOException e) {
			log.log( "Failed to write to CARIS save file: " + fileOut.getPath() );
			e.printStackTrace();
		}
	}
}
