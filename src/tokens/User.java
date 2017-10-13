package tokens;

import sx.blah.discord.handle.obj.User;

import java.util.HashMap;

public class Users {
	
	// THESE ARE REQUIRED
	public int karma;
	public User user;
	
	// Extended Variables
	public Map< String, String > contactInfo = new HashMap< String, String >();
	public String location;
		
	public Users (User user) {
		this.user = user;
		userHuman = user.getDisplayName()
		karma = 0;
	}	
}
