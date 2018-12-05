package caris.framework.library;

import java.util.HashMap;

import sx.blah.discord.handle.obj.IUser;

public class UserInfo {
	
	public IUser user;
	public String location;
	public String lastMessage;
	
	public String nicknameLock;
		
	public HashMap< String, Object > genericInfo;
	
	public UserInfo( IUser user ) {
		this.user = user;
		location = "";
		
		nicknameLock = "";
				
		genericInfo = new HashMap<String, Object>();
	}	
}
