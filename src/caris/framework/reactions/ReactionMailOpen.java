package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.tokens.Mail;
import caris.framework.utilities.Logger;

public class ReactionMailOpen extends Reaction{
	
	public Mail mail;
	
	public ReactionMailOpen(Mail mail) {
		this(mail, 1);
	}
	
	public ReactionMailOpen(Mail mail, int priority) {
		super(priority);
		this.mail = mail;
	}
	
	@Override
	public void run() {
		Logger.print("Mail marked as read", 3);
		mail.read();
	}
	
}
