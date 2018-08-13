package caris.framework.reactions;

import java.util.ArrayList;

public class MultiReaction extends Reaction {

	public ArrayList<Reaction> reactions;
	
	public MultiReaction(ArrayList<Reaction> reactions) {
		this(reactions, 1);
	}
	
	public MultiReaction(ArrayList<Reaction> reactions, int priority) {
		super(priority);
		this.reactions = reactions;
	}
	
	@Override
	public void execute() {
		for( Reaction reaction : reactions ) {
			reaction.execute();
		}
	}
	
}
