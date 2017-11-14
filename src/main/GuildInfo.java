package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import tokens.Poll;
import tokens.Reminder;
import tokens.UserData;
import utilities.Handler;

public class GuildInfo {
	/* Guild Settings */
	public HashMap<String, Boolean> modules;
	
	/* Vote Libraries */
	public HashMap<String, Poll> polls;
	
	/* Location Libraries */
	public HashMap<String, ArrayList<String>> locations;
	public HashMap<String, String> people;
	
	// Creates Map of Username-human to User user
	public HashMap<String, String> translator; // we might not need this if people just @ everyone else
	public HashMap<String, UserData> userIndex;
	public HashMap<Calendar, Reminder> reminders;
	
	public GuildInfo() {
		modules = new HashMap<String, Boolean>();
		
		polls = new HashMap<String, Poll>();
		locations = new HashMap<String, ArrayList<String>>();
		people = new HashMap<String, String>();
		translator = new HashMap<String, String>();
		userIndex = new HashMap<String, UserData>();
		reminders = new HashMap<Calendar, Reminder>();
		
		init();
	}
	
	private void init() {
		for( String s : Brain.invokerModules.keySet() ) {
			modules.put(s, true);
		}
		for( String s :  Brain.responderModules.keySet() ) {
			modules.put(s, true);
		}
	}
}
