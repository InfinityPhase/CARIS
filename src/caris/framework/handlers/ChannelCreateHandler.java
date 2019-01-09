package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.reactions.ReactionChannelTrack;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelCreateEvent;

public class ChannelCreateHandler extends GeneralHandler {

	public ChannelCreateHandler() {
		super("ChannelCreate", false);
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return event instanceof ChannelCreateEvent;
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.debug("Channel creation detected", 2);
		ChannelCreateEvent channelCreateEvent = (ChannelCreateEvent) event;
		Logger.debug("Reaction produced from " + name, 1, true);
		return new ReactionChannelTrack(channelCreateEvent.getGuild(), channelCreateEvent.getChannel(), -1);
	}
	
}
