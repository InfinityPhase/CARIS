package caris.framework.reactions;

import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;

public class ReactionBlackboxClose extends Reaction {

	public IChannel channel;
	
	public ReactionBlackboxClose(IChannel channel) {
		this(channel, 2);
	}
	
	public ReactionBlackboxClose(IChannel channel, int priority) {
		super(priority);
		this.channel = channel;
	}
	
	@Override
	public void execute() {
		Long messageID = Variables.guildIndex.get(channel.getGuild()).channelIndex.get(channel).closeBlackbox();
		channel.getMessageHistoryTo(messageID).bulkDelete();
		Logger.print("Blackbox closed in (" + channel.getLongID() + ") <" + channel.getName() + ">", 2);
	}
	
}
