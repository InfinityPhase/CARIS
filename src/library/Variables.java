package library;

import java.io.Serializable;
import java.util.HashMap;

import main.GuildInfo;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import utilities.DataSaver;

public class Variables implements Serializable{
	// Dynamic global variables
	
	/* This is nessessary */
	private static final long serialVersionUID = 5666750084753825282L;
	/* Gigantic Variable Library */
	public static HashMap<String, IChannel> channelMap = new HashMap<String, IChannel>();
	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>();
	
	/* Global Utilities */
	public static DataSaver ds = new DataSaver();

}
