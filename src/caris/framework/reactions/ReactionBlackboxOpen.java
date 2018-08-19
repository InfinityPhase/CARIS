package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;

public class ReactionBlackboxOpen extends Reaction {

	public IChannel channel;
	public Long messageID;
	
	public ReactionBlackboxOpen(IChannel channel, Long messageID) {
		this(channel, messageID, 2);
	}
	
	public ReactionBlackboxOpen(IChannel channel, Long messageID, int priority) {
		super(priority);
		this.channel = channel;
		this.messageID = messageID;
	}
	
	@Override
	public void run() {
		Variables.guildIndex.get(channel.getGuild()).channelIndex.get(channel).openBlackbox(messageID);
		Logger.print("Blackbox opened in (" + channel.getLongID() + ") <" + channel.getName() + ">", 2);
	}
	
}
