package library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.Brain;
import main.GuildInfo;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import utilities.Logger;

public class Variables {
	// Dynamic global variables
	
	/* Gigantic Variable Library */
	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>();
	
	/* Global Utilities */
	public static List<String> commandPrefixes = new ArrayList<String>();
	public static List<String> commandExacts = new ArrayList<String>();
	public static List<String> toolPrefixes = new ArrayList<String>();
	
	// This is the wrong way to do this
	public static List<Logger> loggers = new ArrayList<Logger>();
	
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
