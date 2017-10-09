import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandHandler {
	// Deals with events
	
	// You know what? Ignore this crap. I don't care right now.
	// Go to https://github.com/decyg/d4jexamplebot/blob/master/src/main/java/com/github/decyg/CommandHandler.java
	// When you do.
	/*
	// Maps command strings to the command implementations
	// So, a command becomes functional
	private static Map<String, Command> commandMap = new HashMap<>();
	
	// Hum. We now deviate from the tutorial, because I do my own thing
	// Also, I don't want to add more dependencies. Eh.
	
	// Populate commandMap with functionality
	// From tutorial:
	// "Might be better practise to do this from an instantiated objects constructor"
	static {
		// TODO: STUFF HERE
	}
	*/
	
	@EventSubscriber
	public void onMessageRecieved(MessageReceivedEvent event) {
		
		// Checks if a message begins with the bot command prefix
		if (event.getMessage().getContent().startsWith(BotUtils.BOT_PREFIX)) {
			// Do... Stuff.
			BotUtils.sendMessage(event.getChannel(), "Hey! I'm a bot, and I live!");
		}
	}
}
