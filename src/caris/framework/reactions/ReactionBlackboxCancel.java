package caris.framework.reactions;

import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;

public class ReactionBlackboxCancel extends Reaction {

	public IChannel channel;
	
	public ReactionBlackboxCancel(IChannel channel) {
		this(channel, 2);
	}
	
	public ReactionBlackboxCancel(IChannel channel, int priority) {
		super(priority);
		this.channel = channel;
	}
	
	@Override
	public void execute() {
		Variables.guildIndex.get(channel.getGuild()).channelIndex.get(channel).cancelBlackbox();
		Logger.print("Blackbox cancelled in (" + channel.getLongID() + ") <" + channel.getName() + ">", 2);
	}
	
}
