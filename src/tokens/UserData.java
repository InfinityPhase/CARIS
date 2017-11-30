package tokens;

import java.io.Serializable;
import java.util.HashMap;

public class UserData implements Serializable {
		
	/* This is nessessary */
	private static final long serialVersionUID = 1718558994418657324L;
	// THESE ARE REQUIRED
	public int karma;
	public long id;
	
	// Extended Variables
	public HashMap< String, String > contactInfo;
	public String location;

	public String lastMessage;

		
	public UserData( long id ) {
		this(id, 0, new HashMap<String, String>(), "", "");
	}
	
	public UserData( long id, int karma, HashMap<String, String> contactInfo, String location, String lastMessage ) {
		this.id = id;
		this.karma = karma;
		this.contactInfo = contactInfo;
		this.location = location;
		this.lastMessage = lastMessage;
	}
}
