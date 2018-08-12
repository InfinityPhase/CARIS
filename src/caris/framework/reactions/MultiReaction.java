package caris.framework.reactions;

import java.util.ArrayList;

public class MultiReaction extends Reaction {

	public ArrayList<Reaction> reactions;
	
	public MultiReaction(ArrayList<Reaction> reactions) {
		this(reactions, 1, false);
	}
	
	public MultiReaction(ArrayList<Reaction> reactions, int priority) {
		this(reactions, priority, false);
	}
	
	public MultiReaction(ArrayList<Reaction> reactions, boolean passive) {
		this(reactions, 1, passive);
	}
	
	public MultiReaction(ArrayList<Reaction> reactions, int priority, boolean passive) {
		super(priority, passive);
		this.reactions = reactions;
	}
	
	@Override
	public void execute() {
		for( Reaction reaction : reactions ) {
			reaction.execute();
		}
	}
	
}
