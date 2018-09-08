package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.tokens.InputSources;
import sx.blah.discord.handle.obj.IChannel;

public class ReactionTrackerRemove extends Reaction {

	public IChannel channel;
	public InputSources inputSources;
	
	public ReactionTrackerRemove(IChannel channel, InputSources inputSources) {
		this(channel, inputSources, 2);
	}
	
	public ReactionTrackerRemove(IChannel channel, InputSources inputSources, int priority) {
		super(priority);
		this.channel = channel;
		this.inputSources = inputSources;
	}
	
	@Override
	public void run() {
		if( Variables.trackerSets.containsKey(channel) ) {
			Variables.trackerSets.get(channel).remove(inputSources);
		}
	}
	
}
