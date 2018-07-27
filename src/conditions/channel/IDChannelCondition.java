package conditions.channel;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.obj.IChannel;

public class IDChannelCondition implements ChannelCondition {
	private String id;
	
	public IDChannelCondition( String id ) {
		this.id = id;
	}

	@Override
	public List<IChannel> channels( List<IChannel> possibleChannels ) {
		List<IChannel> channel = new ArrayList<>();
		for( IChannel c : possibleChannels ) {
			if( c.getStringID().equals(id) ) {
				channel.add(c); // We know only one will match
				break;
			}
		}
		
		return channel;
	}

	

}
