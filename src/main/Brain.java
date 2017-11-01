package main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.io.Console;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import commands.CommandHandler;
import invokers.EchoInvoker;
import invokers.LocationInvoker;
import invokers.NicknameInvoker;
import invokers.VoteInvoker;
import invokers._8BallInvoker;
import library.Constants;
import library.Variables;
import responders.LocationResponder;
import responders.MentionResponder;
import responders.NicknameResponder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
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
	public static SimpleDateFormat sdf = new SimpleDateFormat( Constants.DATEFORMAT );
	public static Console console = System.console();

	public static ArrayList<Handler> invokers = new ArrayList<Handler>();
	public static ArrayList<Handler> responders = new ArrayList<Handler>();

	/* Invoked Handlers */
	public static EchoInvoker echoInvoker = new EchoInvoker();
	public static LocationInvoker locationInvoker = new LocationInvoker();
	public static VoteInvoker voteInvoker = new VoteInvoker();
	public static _8BallInvoker _8ballInvoker = new _8BallInvoker();
	public static NicknameInvoker nicknameInvoker = new NicknameInvoker();

	/* Auto Handlers */
	public static MentionResponder mentionResponder = new MentionResponder();
	public static LocationResponder locationResponder = new LocationResponder();
	public static NicknameResponder nicknameResponder = new NicknameResponder();

	/* Gigantic Variable Library */
	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>();

	public static void main(String[] args) {

		init();

		if (!(args.length >= 1)) {
			System.out.println("Please pass the TOKEN as the first argument.");
			System.out.println("# java -jar SimpleResponder.jar TOKEN");
			System.exit(0);
		}
		
		// Gets token from arguments
		String token = args[0];

		// Get the encryption password
		if( Constants.SAVESTATE ) {
			char password[] = console.readPassword("Save file password: ");

			if( password.length <= 0 ) {
				System.out.println("WARNING: Password is empty");
			}
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
			// I'm sure that this won't cause a slowdown...
			// Heh heh... heh.
			if( ( ( System.currentTimeMillis() - startTime ) >= Constants.SAVETIME ) && Constants.SAVESTATE ) {
				if( Constants.DEBUG ) { System.out.println("Saving CARIS State..."); }

				String fileName = ( Constants.PREPENDDATE ? sdf.format( Calendar.getInstance().getTime() ) : "" ) + Constants.SAVEFILE + Constants.SAVEEXTENTION;

				// Read file:
				// try( BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream( new File( fileName ) ), Constants.ENCODING ) ); ) {} catch( EXCEPTION e ) {}
				try( ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( fileName ) ); ) {
					// Open JSON file
					// Clear contents if exists
					// Stream data of objects to PGP
					// Save stream to JSON file
//					for( Handler h : invokers ) {
//						out.writeObject( h );
//					} for( Handler h : responders ) {
//						out.writeObject( h );
//					}
					
					out.defaultWriteObject();
					
				} catch (IOException e) {
					System.out.println( "Failed to open CARIS save file: " + fileName );
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
		responders.add(mentionResponder);
		responders.add(locationResponder);
		responders.add(nicknameResponder);
	}
}
