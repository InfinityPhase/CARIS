package utilities;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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
	long id;
	String name;
	String icon	;
	String splash;
	long owner_id;	
	String region;	
	long afk_channel_id;
	int afk_timeout;
	boolean embed_enabled;
	long embed_channel_id;	
	int verification_level;	
	int default_message_notifications;	
	int explicit_content_filter;
	Cache<IRole> roles;
	Emoji[] emojis;	
	String[] features;	
	int mfa_level;	
	int application_id;
	boolean widget_enabled;
	long widget_channel_id;
	joined_at;
	boolean large;
	boolean unavailable;
	int member_count;
	voice_states;
	members;
	Cache<IChannel> channels;
	presences;

	// WTF?
	IShard shard;
	Cache<IVoiceChannel> voiceChannels;
	Cache<IUser> users;
	Cache<Guild.TimeStampHolder> joinTimes;
	Cache<ICategory> categories;

	private String endpoint = "https://discordapp.com/api/guilds/";
	private URL url;
	private HttpsURLConnection connection;
	
	public GuildLoader( String guildID ) {
		// FORMAT:
		// curl -i -H "Accept: application/json" -H "Content-Type: application/json" -H "User-Agent: DiscordBot (infinityphase.com, 1)" -H "Authorization: Bot TOKENHERE" -X GET 'https://discordapp.com/api/guilds/{guildID}'
		try {
			url = new URL( endpoint );
			connection = (HttpsURLConnection) url.openConnection();
			
			// Set params properly
			connection.setRequestMethod( "GET" );
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			// Set the headers
			connection.setRequestProperty( "Content-Type", "application/json" );
			connection.setRequestProperty( "User-Agent", "DiscordBot (infinityphase.com, 1)" );
			connection.setRequestProperty( "Authorization", "Bot " + Brain.token );
		}
	}

	public GuildLoader( Long guildID ) {

	}

	public Guild buildGuild() {
		return new Guild(shard, name, id, icon, owner_id, afk_channel_id, afk_timeout, region, verification_level, roles, channels, voiceChannels, users, joinTimes, categories);
	}

}
