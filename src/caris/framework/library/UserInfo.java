package caris.framework.library;

import java.util.ArrayList;
import java.util.HashMap;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class UserInfo {
	
	public IUser user;
	public boolean admin;
	public boolean ban;
	public long id; // Snowflake ID
	public int karma;
	public String location; // The declared location of the user
	public String rules; // The rules of the guild
	public IMessage lastMessage; // The last message the user has sent

	public HashMap< String, String > contactInfo; // Contact info about the user
	public HashMap< String, Object > genericInfo; // Whatever info
	
	public ArrayList< IGuild > guilds; // Guilds the user has been detected in
	
	public UserInfo( IUser user ) {
		this( user, user.getLongID(), 0, new HashMap<String, String>(), "", null, false, false, new HashMap<String, Object>(), new ArrayList<IGuild>() );
	}
	
	public UserInfo( IUser user, long id, int karma, HashMap<String, String> contactInfo, String location, IMessage lastMessage, boolean admin, boolean ban, HashMap<String, Object> genericInfo, ArrayList<IGuild> guilds ) {
		this.user = user;
		this.admin = admin;
		this.ban = ban;
		this.id = id;
		this.karma = karma;
		this.location = location;
		this.lastMessage = lastMessage;

		this.contactInfo = contactInfo;
		this.genericInfo = genericInfo;

		this.guilds = guilds;
	}
}
