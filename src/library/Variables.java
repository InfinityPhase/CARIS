package library;

import java.util.HashMap;

import main.Brain;
import main.GuildInfo;
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
