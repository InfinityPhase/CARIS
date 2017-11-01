package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import tokens.Poll;
import tokens.UserData;

public class GuildInfo implements Serializable {

	/* Unique ID for this version of this class */
	private static final long serialVersionUID = 6924279741134225977L;

	/* Vote Libraries */
	public HashMap<String, Poll> polls;
	
	/* Location Libraries */
	public HashMap<String, ArrayList<String>> locations;
	public HashMap<String, String> people;
	
	// Creates Map of Username-human to User user
	public HashMap<String, String> translator; // we might not need this if people just @ everyone else
	public HashMap <String, UserData> userIndex;
	
	public GuildInfo() {
		polls = new HashMap<String, Poll>();
		locations = new HashMap<String, ArrayList<String>>();
		people = new HashMap<String, String>();
		translator = new HashMap<String, String>();
		userIndex = new HashMap<String, UserData>();
	}
}
