package caris.framework.reactions;

public class Reaction implements Comparable<Reaction> {
	
	public int priority;
	public boolean passive;
	
	public Reaction() {
		this(0, false);
	}
	
	public Reaction(int priority) {
		this(priority, false);
	}
	
	public Reaction(boolean passive) {
		this(0, passive);
	}
	
	public Reaction(int priority, boolean passive) {
		this.priority = priority;
		this.passive = passive;
	}
	
	public void execute() {}
	
	@Override
	public int compareTo(Reaction r) {
		int compare = r.priority;
		if( this.priority < compare ) {
			return 1;
		} else if( this.priority > compare ) {
			return -1;
		} else {
			return 0;
		}
	}
}
