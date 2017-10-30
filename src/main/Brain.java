package main;

import java.util.ArrayList;
import java.util.HashMap;

import commands.CommandHandler;
import invokers.EchoInvoker;
import invokers.LocationInvoker;
import invokers.NicknameInvoker;
import invokers.VoteInvoker;
import invokers._8BallInvoker;
import library.Constants;
import responders.LocationResponder;
import responders.MentionResponder;
import responders.NicknameResponder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import utilities.BotUtils;
import utilities.Handler;
import utilities.TokenParser;

public class Brain {

	/*	IMPORTANT NOTES
	 * 	- Responders ignore case by setting messages to lower case before parsing
	 */
	
	public static TokenParser tp = new TokenParser();

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

		IDiscordClient cli = BotUtils.getBuiltDiscordClient(token);
		if( Constants.DEBUG ) {System.out.println("Client built successfully.");}
		
		cli.getDispatcher().registerListener(new CommandHandler());
		if( Constants.DEBUG ) {System.out.println("Listener established successfully.");}
		
		// Only login after all event registering is done
		cli.login();
		if( Constants.DEBUG ) {System.out.println("Client logged in.");}

	}
	public static void init() { // add handlers to their appropriate categories here
		if( Constants.DEBUG ) {System.out.println("Initializing.");}
		invokers.add(echoInvoker);
		invokers.add(voteInvoker);
		invokers.add(_8ballInvoker);
		invokers.add(nicknameInvoker);
		responders.add(nicknameResponder);
	}
}
