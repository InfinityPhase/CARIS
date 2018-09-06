package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.tokens.Poll;
import caris.framework.utilities.BotUtils;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

public class ReactionPollClose extends Reaction {

	public Poll poll;
	public IChannel channel;
	
	public ReactionPollClose(Poll poll, IChannel channel) {
		this(poll, channel, 1);
	}
	
	public ReactionPollClose(Poll poll, IChannel channel, int priority) {
		super(priority);
		this.poll = poll;
		this.channel = channel;
	}
	
	@Override
	public void run() {
		poll.setMessage(poll.getMessage().getChannel().getMessageByID(poll.getMessage().getLongID())); // possibly the dumbest line of code I've had to write because of threading
		poll.close();
		EmbedBuilder embed = poll.getResultEmbed();
		BotUtils.sendMessage(channel, embed);
		Logger.say(embed.toString(), channel);
	}
	
}
