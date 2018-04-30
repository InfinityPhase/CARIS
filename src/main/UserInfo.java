package main;

import java.util.ArrayList;
import java.util.HashMap;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class UserInfo {
	
	public IMessage lastMessage; // The last message the user has sent

	public ArrayList< IGuild > guilds; // Guilds the user has been detected in
	
	public UserInfo( IUser user ) {
		this( user, user.getLongID(), 0, new HashMap<String, String>(), "", null, false, false, new HashMap<String, Object>(), new ArrayList<IGuild>() );
	}
	
	public UserInfo( IUser user, long id, int karma, HashMap<String, String> contactInfo, String location, IMessage lastMessage, boolean admin, boolean ban, HashMap<String, Object> genericInfo, ArrayList<IGuild> guilds ) {
		this.lastMessage = lastMessage;

		this.guilds = guilds;
	}
}
