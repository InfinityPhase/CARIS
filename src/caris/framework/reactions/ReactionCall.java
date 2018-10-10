package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class ReactionCall extends Reaction {
	
	public enum Operation {
		JOIN,
		LEAVE
	}
	
	public IVoiceChannel channel;
	public Operation operation;
	
	public ReactionCall(IVoiceChannel channel, Operation operation) {
		this(channel, operation, 2);
	}
	
	public ReactionCall(IVoiceChannel channel, Operation operation, int priority) {
		super(priority);
		this.channel = channel;
		this.operation = operation;
	}
	
	@Override
	public void run() {
		switch(operation) {
			case JOIN:
				channel.join();
				Logger.print("Joined voice channel <" + channel.getName() + "> (" + channel.getLongID() + ").", 3);
				break;
			case LEAVE:
				channel.leave();
				Logger.print("Left voice channel <" + channel.getName() + "> (" + channel.getLongID() + ").", 3);
				break;
			default:
				Logger.error("Invalid operation in ReactionCall");
				break;
		}
	}
	
}
