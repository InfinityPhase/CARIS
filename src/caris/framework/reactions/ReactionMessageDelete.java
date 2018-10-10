package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.MessageHistory;

public class ReactionMessageDelete extends Reaction {

	public IChannel channel;
	private IMessage message;
	private MessageHistory history;
	
	public ReactionMessageDelete( IChannel channel, IMessage message ) {
		this(channel, message, 2);
	}
	
	public ReactionMessageDelete( IChannel channel, IMessage message, int priority) {
		super(priority);
		this.channel = channel;
		this.message = message;
	}
	
	public ReactionMessageDelete( IChannel channel, MessageHistory messageLog ) {
		this(channel, messageLog, 2);
	}
	
	public ReactionMessageDelete( IChannel channel, MessageHistory messageLog, int priority ) {
		super(priority);
		this.channel = channel;
		this.history = messageLog;
	}
	
	@Override
	public void run() {
		if( message != null ) {
			Long id = message.getLongID();
			message.delete();
			Logger.print("Message (" + id + ") deleted", 2);
		} else {
			int count = history.bulkDelete().size();
			Logger.print("Deleted " + count + " messages from (" + channel.getLongID() + ") <" + channel.getName() + ">", 2);
		}
	}
	
}
