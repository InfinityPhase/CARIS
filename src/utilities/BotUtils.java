package utilities;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class BotUtils {
	// Constants:
	
	// Actually creates the client object.
	// Magic!
	public static IDiscordClient getBuiltDiscordClient(String token) {
		// Creates a client with the given token.
		// Uses the default value for shards for ease of use
		// Here, any extra params for the bot should be attached.
		// Some examples include: ping timeout, status(invisible, online, ect.)
		return new ClientBuilder()
				.withToken(token)
				.withRecommendedShardCount()
				.build();
	}
	
	public static void sendMessage(IChannel channel, String message) {
		// Will send a string to a given channel
		// For ease of use.
		// YOu can create tons of these things, for everything.
		// Cool, huh?
		
		// Whoa, arrow notation!
		RequestBuffer.request(() -> {
			// Try/catch block to check sending the message
			try {
				// Actually send the message
				channel.sendMessage(message);
			}
			catch (DiscordException e) {
				// Reports errors without crashing
				// This is also the place to log errors
				System.err.println("Message could not be sent with error: ");
				e.printStackTrace();
			}
		});
	}

}
