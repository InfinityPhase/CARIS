package caris.framework.basereactions;

import java.util.ArrayList;

public class MultiReaction extends Reaction {

	public ArrayList<Reaction> reactions;
	
	public MultiReaction() {
		this(1);
	}
	
	public MultiReaction(int priority) {
		super(priority);
		reactions = new ArrayList<Reaction>();
	}
	
	public MultiReaction(ArrayList<Reaction> reactions) {
		this(reactions, 1);
	}
	
	public MultiReaction(ArrayList<Reaction> reactions, int priority) {
		super(priority);
		this.reactions = reactions;
	}
	
	@Override
	public void run() {
		for( Reaction reaction : reactions ) {
			reaction.run();
		}
	}
	
}