package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class ReactionLeaveCall extends Reaction {

	public IVoiceChannel channel;
	
	public ReactionLeaveCall(IVoiceChannel channel) {
		this(channel, 2);
	}
	
	public ReactionLeaveCall(IVoiceChannel channel, int priority) {
		super(priority);
		this.channel = channel;
	}
	
	@Override
	public void run() {
		channel.leave();
		Logger.print("Left voice channel <" + channel.getName() + "> (" + channel.getLongID() + ").", 3);
	}
	
}
