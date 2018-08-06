package caris.framework.reactions;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;
import utilities.BotUtils;

public class ReactionEmbed extends Reaction {

	public EmbedBuilder embed;
	public IChannel channel;
	
	public ReactionEmbed(EmbedBuilder embed, IChannel channel) {
		super(2);
		this.embed = embed;
		this.channel = channel;
	}
	
	public ReactionEmbed(EmbedBuilder embed, IChannel channel, int priority) {
		super(priority);
		this.embed = embed;
		this.channel = channel;
	}
	
	@Override
	public void execute() {
		BotUtils.sendMessage(channel, embed);
	}
}
