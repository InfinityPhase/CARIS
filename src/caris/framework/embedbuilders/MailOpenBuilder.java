package caris.framework.embedbuilders;

import caris.framework.tokens.Mail;
import sx.blah.discord.util.EmbedBuilder;

public class MailOpenBuilder extends Builder {
	
	public Mail mail;
	
	public MailOpenBuilder(Mail mail) {
		super();
		this.mail = mail;
	}
	
	@Override
	public void buildEmbeds() {
		embeds.add(new EmbedBuilder());
		embeds.get(0).withTitle(":envelope: From: " + mail.user.getName() + "#" + mail.user.getDiscriminator());
		embeds.get(0).withDescription(mail.message);
		embeds.get(0).withFooterText("Type `==> mail send <@user> <\"message\">` to send mail to other users!");
	}
	
}
