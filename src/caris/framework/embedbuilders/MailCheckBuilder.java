package caris.framework.embedbuilders;

import caris.framework.library.UserInfo;
import caris.framework.tokens.Mail;
import sx.blah.discord.util.EmbedBuilder;

public class MailCheckBuilder extends Builder {
	
	public UserInfo userInfo;
	
	public MailCheckBuilder(UserInfo userInfo) {
		super();
		this.userInfo = userInfo;
	}
	
	@Override
	public void buildEmbeds() {
		if( userInfo.mailbox.isEmpty() ) {
			embeds.add(new EmbedBuilder());
			embeds.get(0).withAuthorName(userInfo.user.getName() + "#" + userInfo.user.getDiscriminator() + "'s Mailbox");
			embeds.get(0).withTitle(":mailbox_with_no_mail: *Your mailbox is empty!*");
			embeds.get(0).withDescription("Type `==> mail send <@user> <\"message\">` to send mail to other users!");
		}
		for( Mail mail : userInfo.mailbox.getMail() ) {
			if( !mail.read ) {
				embeds.addAll(new MailOpenBuilder(mail).getEmbeds());
				mail.read();
			}
		}
	}
	
}
