package caris.framework.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.library.Constants;
import caris.framework.library.Keywords;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionTrackerAdd;
import caris.framework.reactions.ReactionTrackerRemove;
import caris.framework.tokens.InputSources;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class TrackerSetHandler extends MessageHandler {

	public TrackerSetHandler() {
		super("Tracker", Access.DEVELOPER);
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return messageEventWrapper.developerAuthor && mentioned(messageEventWrapper) && messageEventWrapper.containsAnyWords("track", "tracking");
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		ArrayList<Long> longs = messageEventWrapper.longTokens;
		MultiReaction track = new MultiReaction(2);
		if( longs.isEmpty() ) {
			track.reactions.add(new ReactionMessage("You need to specify the ID of a channel/guild!", messageEventWrapper.getChannel()));
		} else {
			if( messageEventWrapper.containsAnyWords(Keywords.END) ) {
				if( !Variables.trackerSets.containsKey(messageEventWrapper.getChannel()) ) {
					track.reactions.add(new ReactionMessage("No inputs are currently being tracked!", messageEventWrapper.getChannel()));
				} else {
					for( IGuild guild : Variables.trackerSets.get(messageEventWrapper.getChannel()).guilds ) {
						if( longs.get(0).equals(guild.getLongID()) ) {
							track.reactions.add(new ReactionTrackerRemove(messageEventWrapper.getChannel(), new InputSources(guild)));
							track.reactions.add(new ReactionMessage("Tracker for <" + guild.getName() + "> removed successfully!", messageEventWrapper.getChannel()));
							break;
						}
					}
					for( IChannel channel : Variables.trackerSets.get(messageEventWrapper.getChannel()).channels ) {
						if( longs.get(0).equals(channel.getLongID()) ) {
							track.reactions.add(new ReactionTrackerRemove(messageEventWrapper.getChannel(), new InputSources(channel)));
							track.reactions.add(new ReactionMessage("Tracker for <" + channel.getName() + "> removed successfully!", messageEventWrapper.getChannel()));
							break;
						}
					}
				}
			} else {
				for( IGuild guild : Variables.guildIndex.keySet() ) {
					if( longs.get(0).equals(guild.getLongID()) ) {
						track.reactions.add(new ReactionMessage("Tracker for <" + guild.getName() + "> set successfully!", messageEventWrapper.getChannel()));
						track.reactions.add(new ReactionTrackerAdd(messageEventWrapper.getChannel(), new InputSources(guild)));
						break;
					} else {
						for( IChannel channel : guild.getChannels() ) {
							if( longs.get(0).equals(channel.getLongID()) ) {
								track.reactions.add(new ReactionMessage("Tracker for <" + channel.getName() + "> set successfully!", messageEventWrapper.getChannel()));
								track.reactions.add(new ReactionTrackerAdd(messageEventWrapper.getChannel(), new InputSources(channel)));
								break;
							}
						}
					}
				}
			}
		}
		return track;
	}
	
	@Override
	public String getDescription() {
		return "Outputs all the messages sent in a given channel.";
	}
	
	@Override
	public HashMap<String, String> getUsage() {
		HashMap<String, String> usage = new HashMap<String, String>();
		usage.put(Constants.NAME + ", start tracking <Channel ID>", "Begins relaying all messages in the specified channel to the current channel");
		usage.put(Constants.NAME + ", stop tracking <Channel ID>", "Stops tracking the given channel");
		return usage;
	}
	
}
