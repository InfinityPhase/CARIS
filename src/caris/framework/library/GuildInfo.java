package caris.framework.library;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import caris.framework.tokens.Poll;
import caris.framework.tokens.Reminder;
import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;


public class GuildInfo {
	
	/* Basic Information */
	public String name;
	public IGuild guild;
	
	/* Guild Settings */
	public HashMap<String, Boolean> modules;
	public String rules;
	public IChannel logChannel;
	public IChannel defaultChannel;
	
	// Role Settings
	public ArrayList<Role> autoRoles;
	
	/* Indices */
	public HashMap<IUser, UserInfo> userIndex;
	public HashMap<IChannel, ChannelInfo> channelIndex;
	
	/* Token Storage */
	public HashMap<String, ArrayList<String>> locations;
	public HashMap<String, Poll> polls;
	public HashMap<Calendar, Reminder> reminders;
	
	public GuildInfo(String name, IGuild guild) {	
		this.name = name;
		this.guild = guild;
		
		modules = new HashMap<String, Boolean>();
		rules = "";
		logChannel = null;

		locations = new HashMap<String, ArrayList<String>>();
		polls = new HashMap<String, Poll>();
		reminders = new HashMap<Calendar, Reminder>();
		
		autoRoles = new ArrayList<Role>();
				
		this.userIndex = new HashMap<IUser, UserInfo>();
		this.channelIndex = new HashMap<IChannel, ChannelInfo>();
		
		init();
	}
	
	private void init() {
		for( IUser u : guild.getUsers() ) {
			userIndex.put( u, new UserInfo(u) );
		}
		for( IChannel c : guild.getChannels() ) {
			channelIndex.put( c, new ChannelInfo(c));
		}
	}
	
	public void addUser( IUser u ) {
		userIndex.put( u, new UserInfo(u) );
		if( !Variables.globalUserInfo.containsKey(u) ) {
			Variables.globalUserInfo.put(u, new GlobalUserInfo(u));
		}
	}
	
	public void addChannel( IChannel c ) {
		channelIndex.put( c, new ChannelInfo(c));
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
