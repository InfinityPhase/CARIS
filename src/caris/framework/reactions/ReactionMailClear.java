package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.UserInfo;
import caris.framework.utilities.Logger;

public class ReactionMailClear extends Reaction {

	public UserInfo userInfo;
	
	public ReactionMailClear(UserInfo userInfo) {
		this(userInfo, 1);
	}
	
	public ReactionMailClear(UserInfo userInfo, int priority) {
		super(priority);
		this.userInfo = userInfo;
	}
	
	@Override
	public void run() {
		Logger.print(userInfo.user.getName() + "#" + userInfo.user.getDiscriminator() + "'s mailbox cleared!", 3);
		userInfo.mailbox.clear();
	}
	
}
