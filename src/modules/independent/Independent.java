package modules.independent;

import java.util.ArrayList;
import java.util.List;

import conditions.channel.ChannelCondition;
import conditions.status.StatusCondition;
import main.Brain;
import modules.Module;
import sx.blah.discord.handle.obj.IChannel;
import tokens.Response;

public class Independent extends Module {
	List<StatusCondition> statusConditions; // Controls when to send the message
	List<ChannelCondition> channelConditions; // Controls what type of channel will get the message

	public Independent() {
		statusConditions = new ArrayList<>();
	}

	public Response process() {
		if( checkConditions() ) {
			setup();
			recipient.addAll( getChannels( Brain.cli.getChannels( true) ) );
			run();
		}
		
		return build();
	}

	void run() {
		// Skips the check
	}

	protected boolean checkConditions() {
		for( StatusCondition c : statusConditions ) {
			if( !c.check() ) {
				return false;
			}
		}

		return true;
	}
	
	public List<IChannel> getChannels( List<IChannel> allChannels ) {
		for( ChannelCondition c : channelConditions ) {
			allChannels = c.channels( allChannels );
		}
		
		return allChannels;
	}
}
