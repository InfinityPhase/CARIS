package caris.framework.library;

import sx.blah.discord.handle.obj.IUser;

public class GlobalUserInfo {

	public IUser user;
	public int karma;
	public boolean hasGoneOffline;
	
	public GlobalUserInfo(IUser user) {
		this.user = user;
		karma = 0;
		hasGoneOffline = false;
	}
	
}
