package caris.framework.reactions;

import caris.framework.library.UserInfo;
import caris.framework.utilities.Logger;

public class ReactionMailDelete extends Reaction {

	public UserInfo userInfo;
	public int number;
	
	public ReactionMailDelete(UserInfo userInfo, int number) {
		this(userInfo, number, 1);
	}
	
	public ReactionMailDelete(UserInfo userInfo, int number, int priority) {
		super(priority);
		this.userInfo = userInfo;
		this.number = number;
	}
	
	@Override
	public void execute() {
		userInfo.incomingMail.remove(number);
		Logger.print("Message deleted from " + "<" + userInfo.user.getName() + "#" + userInfo.user.getDiscriminator() + "> (" + userInfo.user.getLongID() + ")'s mailbox.", 3);
	}
	
}
