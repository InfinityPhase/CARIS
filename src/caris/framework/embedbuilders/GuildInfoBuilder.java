package caris.framework.embedbuilders;

import caris.framework.library.GuildInfo;
import caris.framework.library.Variables;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class GuildInfoBuilder extends Builder {
	
	private GuildInfo guildInfo;
	private IGuild guild;
	
	public GuildInfoBuilder(IGuild guild) {
		super();
		this.guild = guild;
		this.guildInfo = Variables.guildIndex.get(guild);
	}
	
	@Override
	protected void buildEmbed() {
		builder.withAuthorName(guildInfo.name);
		builder.withTitle("*ID: " + guild.getStringID() + "*");
		String channels = "";
		for( IChannel channel : guild.getChannels() ) {
			channels += "**[" + channel.getName() + "]** *(" + channel.getLongID() + ")*\n";
		}
		builder.appendField("**__Channels__**", channels, false);
		String users = "";
		for ( IUser user : guild.getUsers() ) {
			users += "**[" + user.getName() + "#" + user.getDiscriminator() + "]** *<" + user.getDisplayName(guild) + "> (" + user.getLongID() + ")*\n";
		}
		builder.appendField("**__Users__**", users, false);
	}
	
}
