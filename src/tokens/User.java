package tokens;

import sx.blah.discord.handle.obj.IUser;

public class User {
	
	public IUser user;
	public double karma;
	public int rank;
	
	public User(IUser user) {
		this.user = user;
		karma = 0;
		rank = 0;
	}
	
}
