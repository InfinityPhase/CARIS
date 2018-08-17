package caris.framework.reactions;

import caris.framework.library.UserInfo;
import caris.framework.tokens.Mail;

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
	public void execute() {
		userInfo.incomingMail.add(mail);
	}
	
}
