package caris.framework.reactions;

import caris.framework.basereactions.MultiReaction;
import caris.framework.tokens.Poll;
import caris.framework.tokens.Poll.Option;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;

public class ReactionPollOpenStart extends MultiReaction {
	
	public Poll poll;
	public IChannel channel;
	
	public ReactionPollOpenStart(Poll poll, IChannel channel) {
		this(poll, channel, 2);
	}
	
	public ReactionPollOpenStart(Poll poll, IChannel channel, int priority) {
		super(2);
		this.poll = poll;
		this.channel = channel;
	}
	
	@Override
	public void run() {
		if( !poll.opened ) {
			Logger.error("Thread execution error while creating Poll.");
			return;
		} else {
			for( Option option : poll.getOptions() ) {
				reactions.add(new ReactionReactionAdd(poll.getMessage(), option.emoji));
			}
		}
		Logger.debug("Poll " + poll.ID + " initialized", 4);
		super.run();
	}
	
}
