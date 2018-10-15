package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class ReactionHear extends Reaction {

	public String message;
	public IUser user;
	public IChannel channel;
	
	public ReactionHear(String message, IUser user, IChannel channel) {
		this(message, user, channel, -1);
	}
	
	public ReactionHear(String message, IUser user, IChannel channel, int priority) {
		super(priority);
		this.message = message;
		this.user = user;
		this.channel = channel;
		this.priority = priority;
	}
	
	@Override
	public void run() {
		if( !user.isBot() && !channel.isPrivate() ) {
			Variables.guildIndex.get(channel.getGuild()).userIndex.get(user).lastMessage = message;			
		}
		Logger.hear(message, user, channel);
	}
}
