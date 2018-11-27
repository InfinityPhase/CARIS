package caris.framework.handlers;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionEmbed;
import caris.framework.tokens.MessageBlock;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class MessageTrackerHandler extends MessageHandler {

	public MessageTrackerHandler() {
		super("MessageTracker", Access.DEFAULT, true);
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return true;
	}
	
	@Override
	protected Reaction process(Event event) {
		MultiReaction track = new MultiReaction(-1);
		for( IChannel outputChannel : Variables.trackerSets.keySet() ) {
			for( IChannel inputChannel : Variables.trackerSets.get(outputChannel).channels ) {
				if( mrEvent.getChannel().equals(inputChannel) ) {
					track.reactions.add(new ReactionEmbed((new MessageBlock(mrEvent)).getEmbeds(), outputChannel));
				}
			}
			for( IGuild inputGuild : Variables.trackerSets.get(outputChannel).guilds ) {
				if( mrEvent.getGuild().equals(inputGuild) ) {
					track.reactions.add(new ReactionEmbed((new MessageBlock(mrEvent).getEmbeds()), outputChannel));
				}
			}
		}
		Logger.debug("Reaction produced from " + name, 1, true);
		return track;
	}
	
}
