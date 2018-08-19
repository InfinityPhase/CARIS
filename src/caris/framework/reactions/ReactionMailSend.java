package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.UserInfo;
import caris.framework.tokens.Mail;
import caris.framework.utilities.Logger;

public class ReactionMailSend extends Reaction {

	public UserInfo userInfo;
	public Mail mail;
	
	public ReactionMailSend( UserInfo userInfo, Mail mail ) {
		this(userInfo, mail, 1);
	}
	
	public ReactionMailSend( UserInfo userInfo, Mail mail, int priority ) {
		super(priority);
		this.userInfo = userInfo;
		this.mail = mail;
	}
	
	@Override
	public void run() {
		Logger.print("Mail sent to <" + userInfo.user.getName() + "#" + userInfo.user.getDiscriminator() + "> (" + userInfo.user.getLongID() + ")'s mailbox.", 3);
		userInfo.mailbox.add(mail);
	}
	
}
