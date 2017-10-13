package main;

import java.util.ArrayList;
import java.util.HashMap;

import tokens.UserData;

public class ChannelInfo {
	/* Location Libraries */
	public static HashMap<String, ArrayList<String>> locations;
	public static HashMap<String, String> people;
	
	// Creates Map of Username-human to User user
	public static HashMap<String, String> translator;
	public static HashMap <String, UserData> userIndex;
	
	public ChannelInfo() {
		locations = new HashMap<String, ArrayList<String>>();
		people = new HashMap<String, String>();
		translator = new HashMap<String, String>();
		userIndex = new HashMap<String, UserData>();
	}
}
