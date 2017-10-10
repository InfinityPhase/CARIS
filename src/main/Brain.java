package main;


import java.util.ArrayList;

import commands.CommandHandler;
import handlers.EchoHandler;
import handlers.Handler;
import handlers.MentionHandler;
import sx.blah.discord.api.IDiscordClient;
import utilities.BotUtils;

// Basically a HelloWorld Bot.
// NOTE: When using Gradle, refresh/restart workspace to update dependencies

public class Brain {
	
	public static ArrayList<Handler> invokedHandlers = new ArrayList<Handler>();
	public static ArrayList<Handler> autoHandlers = new ArrayList<Handler>();
	
	/* Invoked Handlers */
	public static EchoHandler echoHandler = new EchoHandler();
	
	/* Auto Handlers */
	public static MentionHandler mentionHandler = new MentionHandler();
	
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
		invokedHandlers.add(echoHandler);
		autoHandlers.add(mentionHandler);
	}

}
