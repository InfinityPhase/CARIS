package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Keywords;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionTrackerAdd;
import caris.framework.reactions.ReactionTrackerRemove;
import caris.framework.tokens.InputSources;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class TrackerSetHandler extends MessageHandler {

	public TrackerSetHandler() {
		super("Tracker Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return isElevated() && isMentioned() && StringUtilities.containsAnyOfIgnoreCase(message, "track", "tracking");
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.debug("Tracker detected", 2);
		ArrayList<Long> longs = TokenUtilities.parseLongs(message);
		MultiReaction track = new MultiReaction(2);
		if( longs.isEmpty() ) {
			Logger.debug("Operation because no ID specified", 2);
			track.reactions.add(new ReactionMessage("You need to specify the ID of a channel/guild!", mrEvent.getChannel()));
		} else {
			if( StringUtilities.containsAnyOfIgnoreCase(message, Keywords.END) ) {
				if( !Variables.trackerSets.containsKey(mrEvent.getChannel()) ) {
					Logger.debug("Operation failed because no inputs being tracked", 2);
					track.reactions.add(new ReactionMessage("No inputs are currently being tracked!", mrEvent.getChannel()));
				} else {
					for( IGuild guild : Variables.trackerSets.get(mrEvent.getChannel()).guilds ) {
						if( longs.get(0).equals(guild.getLongID()) ) {
							track.reactions.add(new ReactionTrackerRemove(mrEvent.getChannel(), new InputSources(guild)));
							track.reactions.add(new ReactionMessage("Tracker for <" + guild.getName() + "> removed successfully!", mrEvent.getChannel()));
							break;
						}
					}
					for( IChannel channel : Variables.trackerSets.get(mrEvent.getChannel()).channels ) {
						if( longs.get(0).equals(channel.getLongID()) ) {
							track.reactions.add(new ReactionTrackerRemove(mrEvent.getChannel(), new InputSources(channel)));
							track.reactions.add(new ReactionMessage("Tracker for <" + channel.getName() + "> removed successfully!", mrEvent.getChannel()));
							break;
						}
					}
				}
			} else {
				for( IGuild guild : Variables.guildIndex.keySet() ) {
					if( longs.get(0).equals(guild.getLongID()) ) {
						track.reactions.add(new ReactionMessage("Tracker for <" + guild.getName() + "> set successfully!", mrEvent.getChannel()));
						track.reactions.add(new ReactionTrackerAdd(mrEvent.getChannel(), new InputSources(guild)));
						break;
					} else {
						for( IChannel channel : guild.getChannels() ) {
							if( longs.get(0).equals(channel.getLongID()) ) {
								track.reactions.add(new ReactionMessage("Tracker for <" + channel.getName() + "> set successfully!", mrEvent.getChannel()));
								track.reactions.add(new ReactionTrackerAdd(mrEvent.getChannel(), new InputSources(channel)));
								break;
							}
						}
					}
				}
			}
		}
		Logger.debug("Response produced from " + name, 1, true);
		return track;
		
	}
	
}
