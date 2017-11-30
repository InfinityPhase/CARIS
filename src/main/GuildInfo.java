package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import embedbuilders.ModuleStatusBuilder;
import embedbuilders.PollBuilder;
import sx.blah.discord.handle.obj.IChannel;
import tokens.Poll;
import tokens.Reminder;
import tokens.UserData;

public class GuildInfo implements Serializable {

	/* Unique ID for this version of this class */
	private static final long serialVersionUID = 6924279741134225977L;
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
	public HashMap<String, String> translator; // we might not need this if people just @ everyone else // Yeah right.
	public HashMap<String, UserData> userIndex;
	public HashMap<Calendar, Reminder> reminders;
	
	// Channel settings
	public long logChannel; // Perhaps should be the actual channel? Probably?
	public List< IChannel > blacklist;
	public List< IChannel > whitelist;
	public HashMap< IChannel, HashMap< String, Object > > settings; // For extendibility, can set a channel to have any number of things
	
	public GuildInfo() {
		this("");
	}
	
	public GuildInfo(String name) {	
		this(name, new HashMap<String, Boolean>(), new HashMap<String, Poll>(), 
				new HashMap<String, ArrayList<String>>(), new HashMap<String, String>(), 
				new HashMap<String, String>(), new HashMap<String, UserData>(), 
				new HashMap<Calendar, Reminder>(), new ArrayList<IChannel>(),
				new ArrayList<IChannel>(), -1,
				new HashMap< IChannel, HashMap< String, Object > >() );
	}
	
	public GuildInfo(String name, HashMap<String, Boolean> modules, HashMap<String, Poll> polls, 
			HashMap<String, ArrayList<String>> locations, HashMap<String, String> people, 
			HashMap<String, String> translator, HashMap<String, UserData> userIndex, 
			HashMap<Calendar, Reminder> reminders, ArrayList<IChannel> blacklist,
			ArrayList<IChannel> whitelist, long logChannel,
			HashMap<IChannel, HashMap<String, Object>> settings ) {
		
		this.name = name;
		this.modules = modules;
		this.polls = polls;
		this.translator = translator;
		this.locations = locations;
		this.people = people;
		this.translator = translator;
		this.userIndex = userIndex;
		this.reminders = reminders;
		this.blacklist = blacklist;
		this.whitelist = whitelist;
		this.logChannel = logChannel;
		this.settings = settings;
		
		pollBuilder = new PollBuilder();
		moduleStatusBuilder = new ModuleStatusBuilder();
		
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
