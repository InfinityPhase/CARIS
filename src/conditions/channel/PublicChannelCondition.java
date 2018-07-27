package conditions.channel;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.obj.IChannel;

public class PublicChannelCondition implements ChannelCondition {

	@Override
	public List<IChannel> channels(List<IChannel> possibleChannels) {
		List<IChannel> remove = new ArrayList<>();
		for( IChannel c : possibleChannels ) {
			if( c.isPrivate() ) {
				remove.add(c);
			}
		}
		
		possibleChannels.removeAll( remove );
		return possibleChannels;
	}

}
