package main;

import java.util.ArrayList;
import java.util.HashMap;

import tokens.UserData;

public class ChannelInfo {
	/* Location Libraries */
	public HashMap<String, ArrayList<String>> locations;
	public HashMap<String, String> people;
	
	// Creates Map of Username-human to User user
	public HashMap<String, String> translator;
	public HashMap <String, UserData> userIndex;
	
	public ChannelInfo() {
		locations = new HashMap<String, ArrayList<String>>();
		people = new HashMap<String, String>();
		translator = new HashMap<String, String>();
		userIndex = new HashMap<String, UserData>();
	}
}
