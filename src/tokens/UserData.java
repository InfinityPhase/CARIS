package tokens;

import sx.blah.discord.handle.impl.obj.User;
import main.Brain;
import java.util.HashMap;

public class UserData {
	
	// THESE ARE REQUIRED
	public int karma;
	public User user;
	
	// Extended Variables
	public HashMap< String, String > contactInfo = new HashMap< String, String >();
	public String location;
		
	public UserData ( User user) {
		this.user = user;
		Brain.humanToUser.put( user.getName(), user );
		karma = 0;
	}	
}
