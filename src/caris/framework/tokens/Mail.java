package caris.framework.tokens;

import sx.blah.discord.handle.obj.IUser;

public class Mail {
	
	public IUser user;
	public String message;
	
	public Mail(IUser user, String message) {
		this.user = user;
		this.message = message;
	}
	
	public String getPreview() {
		return (message.length() < 16) ? message : message.substring(0, 16);
	}
	
}
