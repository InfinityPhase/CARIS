package caris.framework.reactions;

import sx.blah.discord.handle.obj.IChannel;
import utilities.BotUtils;

public class ReactionMessage extends Reaction {
	
	public String message;
	public IChannel channel;
	
	public ReactionMessage( String message, IChannel channel ) {
		this.message = message;
		this.channel = channel;
	}
	
	public void execute() {
		BotUtils.sendMessage(channel, message);
	}
	
}
