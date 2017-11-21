package datawrappers;

import org.json.*;

import sx.blah.discord.api.IShard;
import sx.blah.discord.api.internal.DiscordClientImpl;
import sx.blah.discord.handle.impl.obj.Guild;
import sx.blah.discord.handle.obj.ICategory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.cache.Cache;

public class SuperGuild extends Guild {
	
	public SuperGuild(IShard shard, String name, long id, String icon, long ownerID, long afkChannel, int afkTimeout, String region, int verification) {
		super(shard, name, id, icon, ownerID, afkChannel, afkTimeout, region, verification,
				new Cache<>((DiscordClientImpl) shard.getClient(), IRole.class), new Cache<>((DiscordClientImpl) shard.getClient(), IChannel.class),
				new Cache<>((DiscordClientImpl) shard.getClient(), IVoiceChannel.class), new Cache<>((DiscordClientImpl) shard.getClient(), IUser.class),
				new Cache<>((DiscordClientImpl) shard.getClient(), TimeStampHolder.class), new Cache<>((DiscordClientImpl) shard.getClient(), ICategory.class));
	}

	public SuperGuild(IShard shard, String name, long id, String icon, long ownerID, long afkChannel, int afkTimeout,
				 String region, int verification, Cache<IRole> roles, Cache<IChannel> channels,
				 Cache<IVoiceChannel> voiceChannels, Cache<IUser> users, Cache<TimeStampHolder> joinTimes, Cache<ICategory> categories) {
		super(shard, name, id, icon, ownerID, afkChannel, afkTimeout,
				 region, verification, roles, channels,
				 voiceChannels, users, joinTimes, categories);
	}
	
	public String toString() {
		String ret = "";
		JSONArray guild = new JSONArray();
		return ret;
	}
}
