package caris.framework.reactions;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

import caris.framework.utilities.BotUtils;
import caris.framework.utilities.Logger;

public class ReactionEmbed extends Reaction {

	public EmbedBuilder embed;
	public IChannel channel;
	
	public ReactionEmbed(EmbedBuilder embed, IChannel channel) {
		this(embed, channel, 2);
	}
	
	public ReactionEmbed(EmbedBuilder embed, IChannel channel, int priority) {
		super(priority);
		this.embed = embed;
		this.channel = channel;
	}
	
	@Override
	public void execute() {
		Logger.say(embed.toString(), channel);
		BotUtils.sendMessage(channel, embed);
	}
}
