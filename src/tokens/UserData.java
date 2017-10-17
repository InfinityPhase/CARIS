package tokens;

import java.util.HashMap;

import sx.blah.discord.handle.impl.obj.User;
import sx.blah.discord.handle.obj.IUser;

public class UserData {
	
	public String lastMessage;
	
	// THESE ARE REQUIRED
	public int karma;
	public User user;
	
	// Extended Variables
	public HashMap< String, String > contactInfo;
	public String location;
		
	public UserData ( User user) {
		this.user = user;
		karma = 0;
		new HashMap< String, String >();
		location = "";
		lastMessage = "";
	}
	
	public UserData ( IUser user ) {
		this((User) user);
	}
}
