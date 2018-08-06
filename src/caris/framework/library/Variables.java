package caris.framework.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import caris.framework.main.Brain;
import caris.framework.library.GuildInfo;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import utilities.DataSaver;

public class Variables {
	// Dynamic global variables
	
	/* Gigantic Variable Library */
	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>();
	
	/* Global Utilities */
	public static DataSaver ds = new DataSaver();
	public static List<String> commandPrefixes = new ArrayList<String>();
	public static List<String> commandExacts = new ArrayList<String>();
	public static List<String> toolPrefixes = new ArrayList<String>();
	
	public static IChannel getChannel( String channel ) {
		return getChannel( new Long( channel ) );
	}
	
	public static IChannel getChannel( long channel ) {
		return Brain.cli.getChannelByID( channel );
	}
	
	public static IUser getUser( long user ) {
		return Brain.cli.getUserByID( user );
	}
	
	public static IUser getUser( String user ) {
		return Brain.cli.getUserByID( new Long( user) );
	}
}
