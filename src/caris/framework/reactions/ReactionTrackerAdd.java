package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.tokens.InputSources;
import sx.blah.discord.handle.obj.IChannel;

public class ReactionTrackerAdd extends Reaction {

	public IChannel channel;
	public InputSources inputSources;
	
	public ReactionTrackerAdd(IChannel channel, InputSources inputSources) {
		this(channel, inputSources, 2);
	}
	
	public ReactionTrackerAdd(IChannel channel, InputSources inputSources, int priority) {
		super(priority);
		this.inputSources = inputSources;
	}
	
	@Override
	public void run() {
		if( Variables.trackerSets.containsKey(channel) ) {
			Variables.trackerSets.get(channel).add(inputSources);
		} else {
			Variables.trackerSets.put(channel, inputSources);
		}
	}
	
}
