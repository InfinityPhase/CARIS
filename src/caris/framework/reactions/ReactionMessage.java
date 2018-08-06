package caris.framework.reactions;

import sx.blah.discord.handle.obj.IChannel;

import caris.framework.utilities.BotUtils;

public class ReactionMessage extends Reaction {
	
	public String message;
	public IChannel channel;
	
	public ReactionMessage( String message, IChannel channel ) {
		super(1);
		this.message = message;
		this.channel = channel;
	}
	
	public ReactionMessage( String message, IChannel channel, int priority ) {
		super(priority);
		this.message = message;
		this.channel = channel;
	}
	
	@Override
	public void execute() {
		BotUtils.sendMessage(channel, message);
	}
	
}
