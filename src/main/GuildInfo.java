package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import embedbuilders.ModuleStatusBuilder;
import embedbuilders.PollBuilder;
import library.Constants;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import tokens.Poll;
import tokens.Reminder;

public class GuildInfo {
	/* Basic Information */
	public String name;
	public IGuild guild;
	
	/* Guild Settings */
	public HashMap<String, Boolean> modules;
	//public HashMap<String, Macro> macros;
	public String rules;
	
	/* Libraries */
	public HashMap<String, Poll> polls;
	public HashMap<String, ArrayList<String>> locations;
	public HashMap<String, String> people;
	
	/* Embed Builders */
	public PollBuilder pollBuilder;
	public ModuleStatusBuilder moduleStatusBuilder;
	
	// Creates Map of Username-human to User user
	public HashMap<String, String> translator; // we might not need this if people just @ everyone else // Yeah right.
	public HashMap<IUser, UserInfo> userIndex;
	public HashMap<Calendar, Reminder> reminders;
	
	// Channel settings
	public long logChannel; // Perhaps should be the actual channel? Probably?
	public List< IChannel > blacklist;
	public List< IChannel > whitelist;
	public HashMap< IChannel, HashMap< String, Object > > settings; // For extendibility, can set a channel to have any number of things
	
	public GuildInfo() {
		this("", null);
	}
	
	// Until IGuild can be initialized or retrieved without declaration, this constructor cannot be used.
	/*public GuildInfo(String name) {	
		this(name, null, new HashMap<String, Boolean>(), new HashMap<String, Poll>(), 
				new HashMap<String, ArrayList<String>>(), new HashMap<String, String>(), 
				new HashMap<String, String>(), new HashMap<String, UserInfo>(), 
				new HashMap<Calendar, Reminder>(), new ArrayList<IChannel>(),
				new ArrayList<IChannel>(), -1,
				new HashMap< IChannel, HashMap< String, Object > >() );
	}*/
	
	public GuildInfo(String name, IGuild guild) {	
		this(name, guild, new HashMap<String, Boolean>(), "", new HashMap<String, Poll>(), 
				new HashMap<String, ArrayList<String>>(), new HashMap<String, String>(), 
				new HashMap<String, String>(), new HashMap<IUser, UserInfo>(), 
				new HashMap<Calendar, Reminder>(), new ArrayList<IChannel>(),
				new ArrayList<IChannel>(), -1,
				new HashMap< IChannel, HashMap<String, Object> >() );
	}
	
	public GuildInfo(String name, IGuild guild, HashMap<String, Boolean> modules, String rules, HashMap<String, Poll> polls, 
			HashMap<String, ArrayList<String>> locations, HashMap<String, String> people, 
			HashMap<String, String> translator, HashMap<IUser, UserInfo> userIndex, 
			HashMap<Calendar, Reminder> reminders, ArrayList<IChannel> blacklist,
			ArrayList<IChannel> whitelist, long logChannel,
			HashMap<IChannel, HashMap<String, Object>> settings ) {
		
		this.name = name;
		this.guild = guild;
		this.modules = modules;
		//this.macros = macros;
		this.rules = rules;
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
			modules.put(s, checkDisabled(s));
		}
		for( String s :  Brain.responderModules.keySet() ) {
			modules.put(s, checkDisabled(s));
		}
		for( IUser u : guild.getUsers() ) {
			userIndex.put( u, new UserInfo(u) );
		}
	}
	
	public void addUser( IUser u ) {
		userIndex.put( u, new UserInfo(u) );
	}
	
	public boolean checkDisabled(String module) {
		for( String s : Constants.DEFAULT_DISBABLED ) {
			if( s.equals(module) ) {
				return false;
			}
		}
		return true;
	}
}
