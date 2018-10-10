package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.tokens.Poll;
import caris.framework.utilities.BotUtils;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

public class ReactionPollOpenCreate extends Reaction {

	public Poll poll;
	public IChannel channel;
	
	public ReactionPollOpenCreate(Poll poll, IChannel channel) {
		this(poll, channel, 1);
	}
	
	public ReactionPollOpenCreate(Poll poll, IChannel channel, int priority) {
		super(priority);
		this.poll = poll;
		this.channel = channel;
	}
	
	@Override
	public void run() {
		EmbedBuilder embed = poll.getPollEmbed();
		IMessage message = BotUtils.sendMessage(channel, embed);
		poll.setMessage(message);
		Logger.say(embed.toString(), channel);
	}
	
}
