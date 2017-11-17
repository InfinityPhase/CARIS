package utilities;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import com.vdurmont.emoji.Emoji;

import main.Brain;
import sx.blah.discord.api.IShard;
import sx.blah.discord.handle.impl.obj.Guild;
import sx.blah.discord.handle.obj.ICategory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.cache.Cache;

public class GuildLoader {

	// From the discord api website
	Emoji[] emojis;	
	String[] features;	
	int mfa_level;	
	int application_id;
	boolean widget_enabled;
	long widget_channel_id;
	boolean large;
	boolean unavailable;
	int member_count;

	// WTF?
	IShard shard;
	Cache<IVoiceChannel> voiceChannels;
	Cache<IUser> users;
	Cache<Guild.TimeStampHolder> joinTimes;
	Cache<ICategory> categories;

	private String endpoint = "https://discordapp.com/api/guilds/";
	private URL url;
	private HttpsURLConnection connection;
	private JSONObject rawGuild;

	public GuildLoader( String guildID ) {
		// FORMAT:
		// curl -i -H "Accept: application/json" -H "Content-Type: application/json" -H "User-Agent: DiscordBot (example.com, 1)" -H "Authorization: Bot TOKENHERE" -X GET 'https://discordapp.com/api/guilds/{guildID}'

		url = new URL( endpoint + guildID );
		connection = (HttpsURLConnection) url.openConnection();

		// Set params properly
		connection.setRequestMethod( "GET" );
		connection.setDoInput(true);
		connection.setDoOutput(true);

		// Set the headers
		connection.setRequestProperty( "Content-Type", "application/json" );
		connection.setRequestProperty( "User-Agent", "DiscordBot (infinityphase.com, 1)" );
		connection.setRequestProperty( "Authorization", "Bot " + Brain.token );

		rawGuild = new JSONObject( connection.getResponseMessage() );

	}

	public GuildLoader( Long guildID ) {
		url = new URL( endpoint + guildID );
		connection = (HttpsURLConnection) url.openConnection();

		// Set params properly
		connection.setRequestMethod( "GET" );
		connection.setDoInput(true);
		connection.setDoOutput(true);

		// Set the headers
		connection.setRequestProperty( "Content-Type", "application/json" );
		connection.setRequestProperty( "User-Agent", "DiscordBot (infinityphase.com, 1)" );
		connection.setRequestProperty( "Authorization", "Bot " + Brain.token );

		rawGuild = new JSONObject( connection.getResponseMessage() );
	}

	public Guild buildGuild() {
		return new Guild(shard, rawGuild.getString("name"), rawGuild.getLong("id"), rawGuild.getString("icon"), rawGuild.getLong("owner_id"), rawGuild.getLong("afk_channel_id"), rawGuild.getInt("afk_timeout"), rawGuild.getString("region"), rawGuild.getInt("verification_level"), (Cache<IRole>) rawGuild.get("roles"), (Cache<IChannel>) rawGuild.get("channels"), (Cache<IVoiceChannel>) rawGuild.get("voiceChannels"), (Cache<IUser>) rawGuild.get("users"), (Cache<Guild.TimeStampHolder>) rawGuild.get("joinTimes"), (Cache<ICategory>) rawGuild.get("categories") );
	}

}
