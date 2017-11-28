package utilities;

import java.util.List;

import library.Variables;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;
import utilities.Logger.level;

public class BotUtils {
	private static Logger log = new Logger().setDefaultIndent(0).setDefaultLevel( level.INFO );

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
		// Yeah, its long, but it works
		if( !Variables.guildIndex.get( channel.getGuild() ).blacklist.contains( channel ) && ( Variables.guildIndex.get( channel.getGuild() ).whitelist.isEmpty() || Variables.guildIndex.get( channel.getGuild() ).whitelist.contains( channel ) ) ) {
			RequestBuffer.request(() -> {
				try {
					channel.sendMessage(message);
				}
				catch (DiscordException e) {
					log.log("Message could not be sent with error: ");
					e.printStackTrace();
				}
			});
		}
	}

	public static void sendMessage( List<IChannel> channel, String message ) {
		for( IChannel c : channel ) {
			// Channel is not in the blacklist
			if( !Variables.guildIndex.get( c.getGuild() ).blacklist.contains( c ) ) {
				if( Variables.guildIndex.get( c.getGuild() ).whitelist.isEmpty() || !Variables.guildIndex.get( c.getGuild() ).whitelist.contains( c ) ) {
					forceSendMessage( c, message );
				}
			}
		}
	}

	public static void forceSendMessage( IChannel channel, String message ) {
		RequestBuffer.request(() -> {
			try {
				channel.sendMessage(message);
			}
			catch (DiscordException e) {
				log.log("Message could not be sent with error: ");
				e.printStackTrace();
			}
		});
	}

	public static void forceSendMessage( List<IChannel> channels, String message ) {
		for( IChannel c : channels ) {
			RequestBuffer.request(() -> {
				try {
					c.sendMessage(message);
				}
				catch (DiscordException e) {
					log.log("Message could not be sent with error: ");
					e.printStackTrace();
				}
			});
		}
	}

	public static void sendLog( IGuild guild, String message ) {
		// Send the message to the guild's log channel, if it exists
		if( Variables.guildIndex.get( guild ).logChannel != -1 ) {
			forceSendMessage( guild.getChannelByID( Variables.guildIndex.get( guild ).logChannel ), message );
		}
	}

}
