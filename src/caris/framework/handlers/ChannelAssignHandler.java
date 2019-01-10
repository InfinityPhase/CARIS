package caris.framework.handlers;

import java.util.HashMap;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.library.Constants;
import caris.framework.library.GuildInfo.SpecialChannel;
import caris.framework.reactions.ReactionChannelAssign;
import caris.framework.reactions.ReactionMessage;

public class ChannelAssignHandler extends MessageHandler {

	public ChannelAssignHandler() {
		super("ChannelAssign", Access.ADMIN);
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return mentioned(messageEventWrapper) && (messageEventWrapper.containsAnyWords("remove", "unset") || messageEventWrapper.containsAnyWords("set", "make")) && (messageEventWrapper.containsWord("default") || messageEventWrapper.containsWord("log"));
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		MultiReaction channelAssign = new MultiReaction(2);
		if( messageEventWrapper.containsAnyWords("remove", "unset") ) {
			if( messageEventWrapper.containsAnyWords("default") ) {
				channelAssign.reactions.add(new ReactionChannelAssign(messageEventWrapper.getGuild(), null, SpecialChannel.DEFAULT));
				channelAssign.reactions.add(new ReactionMessage("Default Channel removed successfully!", messageEventWrapper.getChannel()));
			} else if( messageEventWrapper.containsWord("log") ) {
				channelAssign.reactions.add(new ReactionChannelAssign(messageEventWrapper.getGuild(), null, SpecialChannel.LOG));
				channelAssign.reactions.add(new ReactionMessage("Log Channel removed successfully!", messageEventWrapper.getChannel()));
			} 
		} else {
			if( messageEventWrapper.containsWord("default") ) {
				channelAssign.reactions.add(new ReactionChannelAssign(messageEventWrapper.getGuild(), messageEventWrapper.getChannel(), SpecialChannel.DEFAULT));
				channelAssign.reactions.add(new ReactionMessage("Default Channel set successfully!", messageEventWrapper.getChannel()));
			} else if( messageEventWrapper.containsWord("log") ) {
				channelAssign.reactions.add(new ReactionChannelAssign(messageEventWrapper.getGuild(), messageEventWrapper.getChannel(), SpecialChannel.LOG));
				channelAssign.reactions.add(new ReactionMessage("Log Channel set successfully!", messageEventWrapper.getChannel()));
			}
		}
		return channelAssign;
	}

	@Override
	public String getDescription() {
		return "Assigns roles to certain channels.";
	}

	@Override
	public HashMap<String, String> getUsage() {
		HashMap<String, String> usage = new HashMap<String, String>();
		usage.put(Constants.NAME + ", set this as the default channel", "Sets the current channel as the default channel for welcome and mail messages");
		usage.put(Constants.NAME + ", unset this as the default channel", "Unsets the current channel as the default channel");
		return usage;
	}
}
