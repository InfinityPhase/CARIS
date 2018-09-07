package caris.framework.tokens;

import java.util.ArrayList;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class InputSources {

	public ArrayList<IChannel> channels;
	public ArrayList<IGuild> guilds;
	
	public InputSources() {
		this.channels = new ArrayList<IChannel>();
		this.guilds = new ArrayList<IGuild>();
	}
	
}
