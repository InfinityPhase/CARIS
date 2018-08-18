package caris.framework.reactions;

import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class ReactionChannelCreate extends Reaction {
		
	public IGuild guild;
	public IChannel channel;
	
	public ReactionChannelCreate(IGuild guild, IChannel channel) {
		this(guild, channel, -1);
	}
	
	public ReactionChannelCreate(IGuild guild, IChannel channel, int priority) {
		super(priority);
		this.guild = guild;
		this.channel = channel;
	}
	
	@Override
	public void execute() {
		Variables.guildIndex.get(guild).addChannel(channel);
		Logger.print("Channel <" + channel.getName() + "> (" + channel.getLongID() + ") added to Guild <" + guild.getName() + "> (" + guild.getLongID() + ")", 0);
	}
	
}
