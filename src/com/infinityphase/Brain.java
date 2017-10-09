package com.infinityphase.CARIS;

import sx.blah.discord.api.IDiscordClient;

// Basically a HelloWorld Bot.
// NOTE: When using Gradle, refresh/restart workspace to update dependencies

public class Brain {
	
	public static void main(String[] args) {
		
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

}
