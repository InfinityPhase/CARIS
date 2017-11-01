package library;

import java.util.HashMap;

import main.GuildInfo;
import sx.blah.discord.handle.obj.IGuild;

public class Variables {
	// Dynamic global variables
	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>();
}
