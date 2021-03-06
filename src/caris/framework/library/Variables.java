package caris.framework.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import caris.framework.tokens.InputSources;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class Variables {
	// Dynamic global variables
	
	/* Gigantic Variable Library */
	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>();
	
	/* Global Utilities */
	public static List<String> commandPrefixes = new ArrayList<String>();
	public static List<String> commandExacts = new ArrayList<String>();
	public static List<String> toolPrefixes = new ArrayList<String>();
	
	/* Global UserData */
	public static HashMap<IUser, GlobalUserInfo> globalUserInfo = new HashMap<IUser, GlobalUserInfo>();
	
	/* Tracker Sets */
	public static HashMap<IChannel, InputSources> trackerSets = new HashMap<IChannel, InputSources>();
}
