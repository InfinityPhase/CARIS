package caris.framework.reactions;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

import caris.framework.utilities.BotUtils;

public class ReactionEmbed extends Reaction {

	public EmbedBuilder embed;
	public IChannel channel;
	
	public ReactionEmbed(EmbedBuilder embed, IChannel channel) {
		this(embed, channel, 2, false);
	}
	
	public ReactionEmbed(EmbedBuilder embed, IChannel channel, int priority) {
		this(embed, channel, priority, false);
	}
	
	public ReactionEmbed(EmbedBuilder embed, IChannel channel, boolean passive) {
		this(embed, channel, 2, passive);
	}
	
	public ReactionEmbed(EmbedBuilder embed, IChannel channel, int priority, boolean passive) {
		super(priority, passive);
		this.embed = embed;
		this.channel = channel;
	}
	
	@Override
	public void execute() {
		BotUtils.sendMessage(channel, embed);
	}
}
