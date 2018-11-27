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
	
	public enum SpecialChannel {
		DEFAULT,
		LOG,
	}
	
	/* Basic Information */
	public String name;
	public IGuild guild;
	
	/* Guild Settings */
	public HashMap<String, Boolean> modules;
	public String rules;
		
	// Role Settings
	public ArrayList<Role> autoRoles;
	
	/* Indices */
	public HashMap<IUser, UserInfo> userIndex;
	public HashMap<IChannel, ChannelInfo> channelIndex;
	public HashMap<SpecialChannel, IChannel> specialChannels;
	
	/* Token Storage */
	public HashMap<String, ArrayList<String>> locations;
	public HashMap<Integer, Poll> polls;
	public HashMap<Calendar, Reminder> reminders;
	
	/* References */
	public EmojiSet emojiSet;
	
	public GuildInfo(String name, IGuild guild) {	
		this.name = name;
		this.guild = guild;
		
		modules = new HashMap<String, Boolean>();
		rules = "";

		locations = new HashMap<String, ArrayList<String>>();
		polls = new HashMap<Integer, Poll>();
		reminders = new HashMap<Calendar, Reminder>();
		
		autoRoles = new ArrayList<Role>();
				
		userIndex = new HashMap<IUser, UserInfo>();
		channelIndex = new HashMap<IChannel, ChannelInfo>();
		specialChannels = new HashMap<SpecialChannel, IChannel>();
		
		emojiSet = new EmojiSet(guild);
		init();
	}
	
	private void init() {
		for( IUser u : guild.getUsers() ) {
			addUser(u);
		}
		for( IChannel c : guild.getChannels() ) {
			addChannel(c);
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
	
	public IChannel getDefaultChannel() {
		if( specialChannels.containsKey(SpecialChannel.DEFAULT) ) {
			return specialChannels.get(SpecialChannel.DEFAULT);
		} else {
			return guild.getDefaultChannel();
		}
	}
}
