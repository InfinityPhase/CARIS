package caris.framework.tokens;

import caris.framework.embedbuilders.Builder;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

public class MessageBlock extends Builder {

	public MessageReceivedEvent mrEvent;
	
	public MessageBlock(MessageReceivedEvent mrEvent) {
		super();
		this.mrEvent = mrEvent;
	}
	
	@Override
	protected void buildEmbeds() {
		embeds.add(new EmbedBuilder());
		embeds.get(0).withAuthorIcon(mrEvent.getGuild().getIconURL());
		embeds.get(0).withAuthorName(mrEvent.getGuild().getName() + " | " + mrEvent.getChannel().getName());
		embeds.get(0).withDescription(mrEvent.getMessage().getFormattedContent());
		embeds.get(0).appendField("__Guild ID__", mrEvent.getGuild().getLongID() + "", true);
		embeds.get(0).appendField("__Channel ID__", mrEvent.getChannel().getLongID() + "", true);
		embeds.get(0).withFooterIcon(mrEvent.getAuthor().getAvatarURL());
		embeds.get(0).withFooterText(mrEvent.getAuthor().getDisplayName(mrEvent.getGuild()) + " (" + mrEvent.getAuthor().getName() + "#" + mrEvent.getAuthor().getDiscriminator() + ")");
	}
	
}
