package library;

import java.util.HashMap;

import main.GuildInfo;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import utilities.DataSaver;

public class Variables {
	// Dynamic global variables
	
	/* Gigantic Variable Library */
	public static HashMap<String, IChannel> channelMap = new HashMap<String, IChannel>();
	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>();
	
	/* Global Utilities */
	public static DataSaver ds = new DataSaver();
	
	/* Trash that will never see the light of day */
	public static long channelID = 359566654478483456L; // TEST: 384618675841531906L REAL: 359566654478483456L
	public static long guildID = 359566653987487744L; // TEST: 366853317709791232L REAL: 359566653987487744L
}
