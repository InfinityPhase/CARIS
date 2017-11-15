package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import embedbuilders.ModuleStatusBuilder;
import embedbuilders.PollBuilder;
import tokens.Poll;
import tokens.Reminder;
import tokens.UserData;

public class GuildInfo {
	/* Basic Information */
	public String name;
	
	/* Guild Settings */
	public HashMap<String, Boolean> modules;
	
	/* Libraries */
	public HashMap<String, Poll> polls;
	public HashMap<String, ArrayList<String>> locations;
	public HashMap<String, String> people;
	
	/* Embed Builders */
	public PollBuilder pollBuilder;
	public ModuleStatusBuilder moduleStatusBuilder;
	
	// Creates Map of Username-human to User user
	public HashMap<String, String> translator; // we might not need this if people just @ everyone else
	public HashMap<String, UserData> userIndex;
	public HashMap<Calendar, Reminder> reminders;
	
	public GuildInfo() {
		this("");
	}
	
	public GuildInfo(String name) {		
		this.name = name;

		modules = new HashMap<String, Boolean>();
		
		pollBuilder = new PollBuilder();
		moduleStatusBuilder = new ModuleStatusBuilder();
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
