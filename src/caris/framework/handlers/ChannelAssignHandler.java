package caris.framework.handlers;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.library.GuildInfo.SpecialChannel;
import caris.framework.reactions.ReactionChannelAssign;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.api.events.Event;

public class ChannelAssignHandler extends MessageHandler {

	public ChannelAssignHandler() {
		super("ChannelAssign Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return isElevated() && isMentioned() && (StringUtilities.containsAnyOfIgnoreCase(message, "remove", "unset") || StringUtilities.containsAnyOfIgnoreCase(message, "set", "make")) && (StringUtilities.containsIgnoreCase(message, "default") || StringUtilities.containsIgnoreCase(message, "log") && isDeveloper());
	}
	
	@Override
	protected Reaction process(Event event) {
		MultiReaction channelAssign = new MultiReaction(2);
		if( StringUtilities.containsAnyOfIgnoreCase(message, "remove", "unset") ) {
			if( StringUtilities.containsIgnoreCase(message, "default") ) {
				channelAssign.reactions.add(new ReactionChannelAssign(mrEvent.getGuild(), null, SpecialChannel.DEFAULT));
				channelAssign.reactions.add(new ReactionMessage("Default Channel removed successfully!", mrEvent.getChannel()));
			} else if( StringUtilities.containsIgnoreCase(message, "log") ) {
				channelAssign.reactions.add(new ReactionChannelAssign(mrEvent.getGuild(), null, SpecialChannel.LOG));
				channelAssign.reactions.add(new ReactionMessage("Log Channel removed successfully!", mrEvent.getChannel()));
			}
		} else {
			if( StringUtilities.containsIgnoreCase(message, "default") ) {
				channelAssign.reactions.add(new ReactionChannelAssign(mrEvent.getGuild(), mrEvent.getChannel(), SpecialChannel.DEFAULT));
				channelAssign.reactions.add(new ReactionMessage("Default Channel set successfully!", mrEvent.getChannel()));
			} else if( StringUtilities.containsIgnoreCase(message, "log") ) {
				channelAssign.reactions.add(new ReactionChannelAssign(mrEvent.getGuild(), mrEvent.getChannel(), SpecialChannel.LOG));
				channelAssign.reactions.add(new ReactionMessage("Log Channel set successfully!", mrEvent.getChannel()));
			}
		}
		Logger.debug("Reaction produced from " + name, 1, true);
		return channelAssign;
	}
}
