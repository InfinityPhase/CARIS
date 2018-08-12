package caris.framework.reactions;

import sx.blah.discord.handle.obj.IChannel;

import caris.framework.utilities.BotUtils;

public class ReactionMessage extends Reaction {
	
	public String message;
	public IChannel channel;
	
	public ReactionMessage( String message, IChannel channel ) {
		this(message, channel, 1, false);
	}
	
	public ReactionMessage( String message, IChannel channel, int priority ) {
		this(message, channel, priority, false);
	}
	
	public ReactionMessage( String message, IChannel channel, boolean passive ) {
		this(message, channel, 1, passive);
	}
	
	public ReactionMessage( String message, IChannel channel, int priority, boolean passive ) {
		super(priority, passive);
		this.message = message;
		this.channel = channel;
	}
	
	@Override
	public void execute() {
		BotUtils.sendMessage(channel, message);
	}
	
}
