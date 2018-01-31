package utilities;

import java.util.List;

import library.Variables;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
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

	public static void sendMessage( IChannel[] channel, EmbedBuilder embed ) {
		for( IChannel c : channel ) {
			sendMessage( c, embed );
		}
	}

	public static void sendMessage( List<IChannel> channel, EmbedBuilder embed ) {
		for( IChannel c : channel ) {
			sendMessage( c, embed );
		}
	}

	public static void sendMessage( IChannel[] channel, EmbedObject embed ) {
		for( IChannel c : channel ) {
			sendMessage( c, embed );
		}
	}

	public static void sendMessage( List<IChannel> channel, EmbedObject embed ) {
		for( IChannel c : channel ) {
			sendMessage( c, embed );
		}
	}

	public static void sendMessage( List<IChannel> channels, String message ) {
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

	public static void sendMessage( IChannel channel, EmbedBuilder embed ) {
		RequestBuffer.request(() -> {
			try {
				channel.sendMessage( embed.build() );
			}
			catch (DiscordException e) {
				log.log("Message could not be sent with error: ");
				e.printStackTrace();
			}
		});
	}

	public static void sendMessage( IChannel channel, EmbedObject embed ) {
		RequestBuffer.request(() -> {
			try {
				channel.sendMessage( embed );
			}
			catch (DiscordException e) {
				log.log("Message could not be sent with error: ");
				e.printStackTrace();
			}
		});
	}

	public static void sendLog( IGuild guild, String message ) {
		// Send the message to the guild's log channel, if it exists
		if( Variables.guildIndex.get( guild ).logChannel != -1 ) {
			sendMessage( guild.getChannelByID( Variables.guildIndex.get( guild ).logChannel ), message );
		}
	}

}
