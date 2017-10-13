package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.obj.User;

import commands.CommandHandler;
import tokens.UserData;
import utilities.BotUtils;
import utilities.Handler;
import utilities.TokenParser;

import invokers.EchoInvoker;
import invokers.LocationInvoker;

import responders.LocationResponder;
import responders.MentionResponder;

public class Brain {

	public static TokenParser tp = new TokenParser();

	public static ArrayList<Handler> invokers = new ArrayList<Handler>();
	public static ArrayList<Handler> responders = new ArrayList<Handler>();

	/* Invoked Handlers */
	public static EchoInvoker echoInvoker = new EchoInvoker();
	public static LocationInvoker locationInvoker = new LocationInvoker();

	/* Auto Handlers */
	public static MentionResponder mentionResponder = new MentionResponder();
	public static LocationResponder locationResponder = new LocationResponder();
	
	/* Location Libraries */
	public static HashMap<String, ArrayList<String>> locations = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, String> people = new HashMap<String, String>();
	
	// Creates Map of Username-human to User user
	public static HashMap< String, String > translator = new HashMap< String, String >();
	public static HashMap < String , UserData > userIndex = new HashMap< String, UserData >();
	
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

		cli.getDispatcher().registerListener(new CommandHandler());

		// Only login after all event registering is done
		cli.login();

	}
	public static void init() { // add handlers to their appropriate categories here
		invokers.add(echoInvoker);
		invokers.add(locationInvoker);
		responders.add(mentionResponder);
		responders.add(locationResponder);
	}

}
