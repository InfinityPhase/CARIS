package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import commands.CalendarHandler;
import commands.CommandHandler;
import controller.Controller;
import controller.ModuleController;
import invokers.DebugInvoker;
import invokers.EchoInvoker;
import invokers.FortuneInvoker;
import invokers.Invoker;
import invokers.LocationInvoker;
import invokers.NicknameInvoker;
import invokers.VoteInvoker;
import invokers._8BallInvoker;
import library.Constants;
import responders.LocationResponder;
import responders.MentionResponder;
import responders.NicknameResponder;
import responders.ReminderResponder;
import responders.Responder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
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
	public static Logger log = new Logger();
	public static SimpleDateFormat sdf = new SimpleDateFormat();

	public static HashMap<String, Invoker> invokerModules = new HashMap<String, Invoker>();
	public static HashMap<String, Responder> responderModules = new HashMap<String, Responder>();
	public static HashMap<String, Controller> controllerModules = new HashMap<String, Controller>();

	/* Invoked Handlers */
	public static EchoInvoker echoInvoker = new EchoInvoker();
	public static LocationInvoker locationInvoker = new LocationInvoker();
	public static VoteInvoker voteInvoker = new VoteInvoker();
	public static _8BallInvoker _8ballInvoker = new _8BallInvoker();
	public static NicknameInvoker nicknameInvoker = new NicknameInvoker();

	public static DebugInvoker debugInvoker = new DebugInvoker();

	public static FortuneInvoker fortuneInvoker = new FortuneInvoker();

	/* Auto Handlers */
	public static MentionResponder mentionResponder = new MentionResponder();
	public static LocationResponder locationResponder = new LocationResponder();
	public static NicknameResponder nicknameResponder = new NicknameResponder();
	public static ReminderResponder reminderResponder = new ReminderResponder();

	/* Admin Controllers */
	public static ModuleController moduleController = new ModuleController();

	/* Gigantic Variable Library */
	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>();

	public static CalendarHandler calendarHandler = new CalendarHandler();
	public static Calendar current = Calendar.getInstance();
	
	public static String token = null;

	// Because we tend to alter the Constants.SAVESTATE variable
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		init();

		if( args.length <= 0 && System.console() != null ) {
			token = System.console().readPassword("Bot Token: ").toString();
			if( token.length() <= 0 ) {
				log.debugOut("Tokens must be longer than 0 characters.");
				System.exit(1);
			}
		} else {
			if( args.length > 0 ) {
				token = args[0];
			} else {
				log.debugOut("Please pass the TOKEN as the first argument.");
				log.debugOut("# java -jar SimpleResponder.jar TOKEN");
				System.exit(1);
			}
		}

		// Gets token from arguments

		// Get the encryption password
		if( Constants.SAVESTATE && ( System.console() != null ) ) {
			char password[] = System.console().readPassword("Save file password: ");

			if( password.length <= 0 ) {
				Slog.debugOut("WARNING: Password is empty");
			}
		} else {
			log.debugOut("WARNING: Password is empty");
			char password[] = {};
		}

		IDiscordClient cli = BotUtils.getBuiltDiscordClient(token);

		log.debugOut("Client built successfully.");

		cli.getDispatcher().registerListener( new CommandHandler() );
		log.debugOut("Listener established successfully.");

		// Only login after all event registering is done
		cli.login();
		log.debugOut("Client logged in.");

		// This should run last, after everything else
		// Designed to run tasks based on time
		long startTime = System.currentTimeMillis();
		while( true ) {

			current = Calendar.getInstance();
			calendarHandler.check();

			if( ( ( System.currentTimeMillis() - startTime ) >= Constants.SAVETIME ) && Constants.SAVESTATE ) {

				if( Constants.DEBUG ) { System.out.println("Saving CARIS State..."); }

				File fileName = new File( ( Constants.PREPENDDATE ? sdf.format( Calendar.getInstance().getTime() ) + "_" : "" ) + Constants.SAVEFILE + Constants.SAVEEXTENTION );

				ObjectWriter out = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, Visibility.ANY).writer().withDefaultPrettyPrinter();

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
				}

				// Reset the timer
				startTime = System.currentTimeMillis();
			}
		}

	}


	public static void init() { // add handlers to their appropriate categories here
		log.debugOut("Initializing.");
		invokerModules.put("Echo Invoker", echoInvoker);
		invokerModules.put("Vote Invoker", voteInvoker);
		invokerModules.put("8ball Invoker", _8ballInvoker);
		invokerModules.put("Nickname Invoker", nicknameInvoker);
		invokerModules.put("Fortune Invoker", fortuneInvoker);
		responderModules.put("Mention Responder", mentionResponder);
		responderModules.put("Nickname Responder", nicknameResponder);
		responderModules.put("Reminder Responder", reminderResponder);
		controllerModules.put("Module Controller", moduleController);
	}

	static void saveState( File fileOut ) {
		try( ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( fileOut ) ); ){
			// Open JSON file
			// Clear contents if exists
			// Stream data of objects to PGP
			// Save stream to JSON file

			log.debugOut("Test after out, before write");

			out.defaultWriteObject();

			log.debugOut("Test after write");

			out.flush();

			out.flush();
		} catch (IOException e) {
			log.debugOut( "Failed to write to CARIS save file: " + fileOut.getPath() );
			e.printStackTrace();
		}
	}
}
