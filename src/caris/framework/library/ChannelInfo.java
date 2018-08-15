package caris.framework.library;

import java.util.ArrayList;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class ChannelInfo {
	
	/* Basic Info */
	public String name;
	public IChannel channel;
	
	/* Channel Settings */
	public boolean blacklisted;
	
	/* History */
	public ArrayList<IMessage> messageHistory;
	
	public ChannelInfo( String name, IChannel channel ) {
		this.name = name;
		this.channel = channel;
		
		blacklisted = false;
		
		messageHistory = new ArrayList<IMessage>();
	}
	
}
