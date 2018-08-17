package caris.framework.embedbuilders;

import caris.framework.library.Constants;
import caris.framework.library.UserInfo;
import caris.framework.tokens.Mail;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class MailCheckBuilder extends Builder {
	
	public UserInfo userInfo;
	public IUser user;
	
	public MailCheckBuilder(UserInfo userInfo) {
		super();
		this.userInfo = userInfo;
		this.user = userInfo.user;
	}
	
	@Override
	public void buildEmbeds() {
		embeds.add(new EmbedBuilder());
		embeds.get(0).withAuthorName(user.getName() + "#" + user.getDiscriminator() + "'s Mailbox");
		embeds.get(0).withDescription("Type `" + Constants.INVOCATION_PREFIX + " mailbox open <#>` to open a message!"
				+ "\n" + "Type `" + Constants.INVOCATION_PREFIX + " mailbox delete <#>` to delete a message!"
				+ "\n" + "Type `" + Constants.INVOCATION_PREFIX + " mailbox send <@user> \"message\"` to send a message!");
		if( userInfo.mailbox.isEmpty() ) {
			embeds.get(0).withTitle(":mailbox_with_no_mail: *Your mailbox is empty!*");
		} else {
			embeds.get(0).withTitle(":mailbox_with_mail: *You've got mail!*");
		}
		int count = 0;
		for( Mail mail : userInfo.mailbox.getMail() ) {
			if( count != 0 && count%25 == 0 ) {
				embeds.add(new EmbedBuilder());
				embeds.get(count/25).withAuthorName(user.getName() + "#" + user.getDiscriminator() + "'s Mailbox");
			}
			embeds.get(count/25).appendField("**" + (count+1) + ". " + mail.user.getName() + "#" + mail.user.getDiscriminator() + "**", ">> *" + mail.getPreview() + "*", false);
			count++;
		}
		for( int f=0; f<embeds.size(); f++ ) {
			embeds.get(f).withFooterText("Page " + (f+1) + " / " + embeds.size());
		}
	}
	
}
