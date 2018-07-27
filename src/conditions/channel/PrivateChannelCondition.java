package conditions.channel;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.obj.IChannel;

public class PrivateChannelCondition implements ChannelCondition {
	
	@Override
	public List<IChannel> channels(List<IChannel> possibleChannels) {
		List<IChannel> result = new ArrayList<>();
		for( IChannel c : possibleChannels ) {
			if( c.isPrivate() ) {
				result.add(c);
			}
		}
		
		return result;
	}

}
