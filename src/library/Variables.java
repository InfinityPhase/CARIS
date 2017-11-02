package library;

import java.util.HashMap;
import java.util.Map;

import main.GuildInfo;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.obj.IGuild;

public class Variables {
	// Dynamic global variables
	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>();
	public static Map< String, Event > debugTest = new HashMap< String, Event >();
}
