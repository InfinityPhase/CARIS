package caris.framework.reactions;

import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.MessageHistory;

public class ReactionPurgeMessages extends Reaction {

	public IChannel channel;
	public MessageHistory history;
	
	public ReactionPurgeMessages( IChannel channel, MessageHistory messageLog ) {
		this(channel, messageLog, 2);
	}
	
	public ReactionPurgeMessages( IChannel channel, MessageHistory messageLog, int priority ) {
		super(priority);
		this.channel = channel;
		this.history = messageLog;
	}
	
	@Override
	public void execute() {
		int count = history.bulkDelete().size();
		Logger.print("Deleted " + count + " messages from (" + channel.getLongID() + ") <" + channel.getName() + ">", 2);
	}
	
}
