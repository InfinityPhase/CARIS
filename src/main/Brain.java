package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;

import commands.CommandHandler;
import library.Variables;
import invokers.DebugInvoker;
import invokers.EchoInvoker;
import invokers.LocationInvoker;
import invokers.NicknameInvoker;
import invokers.VoteInvoker;
import invokers._8BallInvoker;
import library.Constants;
import library.TestCereal;
import responders.LocationResponder;
import responders.MentionResponder;
import responders.NicknameResponder;
import utilities.BotUtils;
import utilities.Handler;
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
	transient public static SimpleDateFormat sdf = new SimpleDateFormat( Constants.DATEFORMAT );

	// TESTING
	public static TestCereal testClass = new TestCereal( 9000, "SO ANNOYING" );

	public static ArrayList<Handler> invokers = new ArrayList<Handler>();
	public static ArrayList<Handler> responders = new ArrayList<Handler>();

	/* Invoked Handlers */
	public static EchoInvoker echoInvoker = new EchoInvoker();
	public static LocationInvoker locationInvoker = new LocationInvoker();
	public static VoteInvoker voteInvoker = new VoteInvoker();
	public static _8BallInvoker _8ballInvoker = new _8BallInvoker();
	public static NicknameInvoker nicknameInvoker = new NicknameInvoker();
	public static DebugInvoker debugInvoker = new DebugInvoker();

	/* Auto Handlers */
	public static MentionResponder mentionResponder = new MentionResponder();
	public static LocationResponder locationResponder = new LocationResponder();
	public static NicknameResponder nicknameResponder = new NicknameResponder();

	/* Gigantic Variable Library */
	//	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>();

	public static void main(String[] args) {
		
		testClass.putPhrases("String", "Value");
		testClass.putPhrases("A", "Z");
		testClass.putPhrases("Big", "Small");
		testClass.putNumbers(4);
		testClass.putNumbers(6);
		testClass.putNumbers(8);


		init();

		String token = null;

		if( args.length <= 0 && System.console() != null ) {
			token = System.console().readPassword("Bot Token: ").toString();
			if( token.length() <= 0 ) {
				System.out.println("Tokens must be longer than 0 characters.");
				System.exit(1);
			}
		} else{
			if( args.length > 0 ) {
				token = args[0];
			} else {
				System.out.println("Please pass the TOKEN as the first argument.");
				System.out.println("# java -jar SimpleResponder.jar TOKEN");
				System.exit(1);
			}
		}

		// Gets token from arguments

		// Get the encryption password
		if( Constants.SAVESTATE && ( System.console() != null ) ) {
			char password[] = System.console().readPassword("Save file password: ");

			if( password.length <= 0 ) {
				System.out.println("WARNING: Password is empty");
			}
		} else {
			System.out.println("WARNING: Password is empty");
			char password[] = {};
		}

		IDiscordClient cli = BotUtils.getBuiltDiscordClient(token);
		if( Constants.DEBUG ) {System.out.println("Client built successfully.");}

		cli.getDispatcher().registerListener(new CommandHandler());
		if( Constants.DEBUG ) {System.out.println("Listener established successfully.");}

		// Only login after all event registering is done
		cli.login();
		if( Constants.DEBUG ) {System.out.println("Client logged in.");}

		// This should run last, after everything else
		// Designed to run tasks based on time
		long startTime = System.currentTimeMillis();
		while( true ) {
			if( ( ( System.currentTimeMillis() - startTime ) >= Constants.SAVETIME ) && Constants.SAVESTATE ) {

				if( Constants.DEBUG ) { System.out.println("Saving CARIS State..."); }

				File fileName = new File( ( Constants.PREPENDDATE ? sdf.format( Calendar.getInstance().getTime() ) + "_" : "" ) + Constants.SAVEFILE + Constants.SAVEEXTENTION );

				ObjectWriter out = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, Visibility.ANY).writer().withDefaultPrettyPrinter();

				// TODO: create array of things that need to be stored
				// Include the invokers map, responders map, and anything else

//				for( Handler h : invokers ) {
//					try {
//						out.writeValue( fileName, h );
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//
//				for( Handler h : responders ) {
//					try {
//						out.writeValue( fileName, h );
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				}
				
				try {
					out.writeValue(fileName, testClass);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Reset the timer
				startTime = System.currentTimeMillis();
			}
		}

	}
	public static void init() { // add handlers to their appropriate categories here
		if( Constants.DEBUG ) {System.out.println("Initializing.");}
		invokers.add(echoInvoker);
		invokers.add(voteInvoker);
		invokers.add(_8ballInvoker);
		invokers.add(nicknameInvoker);
		invokers.add(debugInvoker);
		responders.add(mentionResponder);
		responders.add(locationResponder);
		responders.add(nicknameResponder);
	}

	static void saveState( File fileOut ) {
		try( ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( fileOut ) ); ){
			// Open JSON file
			// Clear contents if exists
			// Stream data of objects to PGP
			// Save stream to JSON file

			System.out.println("Test after out, before write");

			out.defaultWriteObject();

			System.out.println("Test after write");

			out.flush();

			out.flush();
		} catch (IOException e) {
			System.out.println( "Failed to write to CARIS save file: " + fileOut.getPath() );
			e.printStackTrace();
		}
	}
}
