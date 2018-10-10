package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class ReactionMessageDelete extends Reaction {

	public IChannel channel;
	private IMessage message;
	private int quantity;
	
	public ReactionMessageDelete( IChannel channel, IMessage message ) {
		this(channel, message, 2);
	}
	
	public ReactionMessageDelete( IChannel channel, IMessage message, int priority) {
		super(priority);
		this.channel = channel;
		this.message = message;
	}
	
	public ReactionMessageDelete( IChannel channel, int quantity ) {
		this(channel, quantity, 2);
	}
	
	public ReactionMessageDelete( IChannel channel, int quantity, int priority ) {
		super(priority);
		this.channel = channel;
		this.quantity = quantity;
	}
	
	@Override
	public void run() {
		if( message != null ) {
			Long id = message.getLongID();
			message.delete();
			Logger.print("Message (" + id + ") deleted", 2);
		} else {
			channel.bulkDelete(Variables.guildIndex.get(channel.getGuild()).channelIndex.get(channel).messageHistory.getQuantity(quantity));
			Logger.print("Deleted " + quantity + " messages from (" + channel.getLongID() + ") <" + channel.getName() + ">", 2);
		}
	}
	
}
