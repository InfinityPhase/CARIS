package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionEmbed;
import caris.framework.tokens.MessageBlock;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class MessageTrackerHandler extends GeneralHandler {

	public MessageTrackerHandler() {
		super("MessageTracker", true);
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return event instanceof MessageReceivedEvent;
	}
	
	@Override
	protected Reaction process(Event event) {
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		MultiReaction track = new MultiReaction(-1);
		for( IChannel outputChannel : Variables.trackerSets.keySet() ) {
			for( IChannel inputChannel : Variables.trackerSets.get(outputChannel).channels ) {
				if( messageReceivedEvent.getChannel().equals(inputChannel) ) {
					track.reactions.add(new ReactionEmbed((new MessageBlock(messageReceivedEvent)).getEmbeds(), outputChannel));
				}
			}
			for( IGuild inputGuild : Variables.trackerSets.get(outputChannel).guilds ) {
				if( messageReceivedEvent.getGuild().equals(inputGuild) ) {
					track.reactions.add(new ReactionEmbed((new MessageBlock(messageReceivedEvent).getEmbeds()), outputChannel));
				}
			}
		}
		return track;
	}
	
	@Override
	public String getDescription() {
		return "Tracks messages and sends them to tracking channels.";
	}
	
}
