package caris.framework.tokens;

import caris.framework.embedbuilders.Builder;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

public class MessageBlock extends Builder {

	public MessageReceivedEvent mrEvent;
	
	public MessageBlock(MessageReceivedEvent mrEvent) {
		this.mrEvent = mrEvent;
	}
	
	@Override
	protected void buildEmbeds() {
		embeds.add(new EmbedBuilder());
		embeds.get(0).withAuthorIcon(mrEvent.getAuthor().getAvatarURL());
		embeds.get(0).withAuthorName(mrEvent.getAuthor().getDisplayName(mrEvent.getGuild()) + " (" + mrEvent.getAuthor().getName() + "#" + mrEvent.getAuthor().getDiscriminator() + ")");
		embeds.get(0).withTitle(mrEvent.getGuild().getName() + " | " + mrEvent.getChannel().getName());
		embeds.get(0).withDescription(mrEvent.getMessage().getFormattedContent());
		embeds.get(0).withImage(mrEvent.getGuild().getIconURL());
		embeds.get(0).withFooterText(mrEvent.getMessageID()+"");
	}
	
}
