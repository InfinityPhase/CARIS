package caris.framework.reactions;

import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class ReactionJoinCall extends Reaction {
	
	public IVoiceChannel channel;
	
	public ReactionJoinCall(IVoiceChannel channel) {
		this(channel, 2);
	}
	
	public ReactionJoinCall(IVoiceChannel channel, int priority) {
		super(priority);
		this.channel = channel;
	}
	
	@Override
	public void execute() {
		channel.join();
		Logger.print("Joined voice channel <" + channel.getName() + "> (" + channel.getLongID() + ").", 3);
	}
	
}
