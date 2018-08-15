package caris.framework.library;

import sx.blah.discord.handle.obj.IChannel;

public class ChannelInfo {
	
	/* Basic Info */
	public String name;
	public IChannel channel;
	
	/* Channel Settings */
	public boolean blacklisted;
	
	public boolean blackboxActive;
	public Long blackboxStart;
	
	public ChannelInfo( IChannel channel ) {
		this.name = channel.getName();
		this.channel = channel;
		
		blacklisted = false;
		
		blackboxActive = false;
		blackboxStart = -1L;
	}
	
	public boolean openBlackbox(Long id) {
		if( !blackboxActive ) {
			blackboxStart = id;
			blackboxActive = true;
			return true;
		} else {
			return false;
		}
	}
	
	public Long closeBlackbox() {
		if( blackboxActive ) {
			blackboxActive = false;
			return blackboxStart;
		} else {
			return -1L;
		}
	}
	
	public boolean cancelBlackbox() {
		if( blackboxActive ) {
			blackboxActive = false;
			blackboxStart = -1L;
			return true;
		} else {
			return false;
		}
	}
	
}
