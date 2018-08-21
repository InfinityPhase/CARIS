package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import caris.framework.library.GuildInfo.SpecialChannel;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import utilities.BotUtils;

public class ReactionChannelAssign extends Reaction {
	
	public IGuild guild;
	public IChannel channel;
	public SpecialChannel channelType;
	
	public ReactionChannelAssign(IGuild guild, IChannel channel, SpecialChannel channelType) {
		this(guild, channel, channelType, -1);
	}
	
	public ReactionChannelAssign(IGuild guild, IChannel channel, SpecialChannel channelType, int priority) {
		super(priority);
		this.guild = guild;
		this.channel = channel;
		this.channelType = channelType;
	}
	
	@Override
	public void run() {
		Variables.guildIndex.get(guild).specialChannels.put(channelType, channel);
		switch(channelType) {
			case DEFAULT:
				BotUtils.sendMessage(channel, "This channel has been set as the default channel!");
				Logger.print("Channel (" + channel.getLongID() + ") <" + channel.getName() + "> set as default channel in guild <" + guild.getName() + "> (" + guild.getLongID() + ")", 3);
				break;
			case LOG:
				BotUtils.sendMessage(channel, "This channel has been set as the log channel!");
				Logger.print("Channel (" + channel.getLongID() + ") <" + channel.getName() + "> set as log channel in guild <" + guild.getName() + "> (" + guild.getLongID() + ")", 3);
				break;
			default:
				Logger.error("Invalid ChannelType in ReactionChannelAssign");
				break;	
		}
	}
	
}
