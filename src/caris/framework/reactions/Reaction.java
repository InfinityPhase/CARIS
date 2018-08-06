package caris.framework.reactions;

public class Reaction implements Comparable<Reaction> {
	
	public int priority;
	
	public Reaction() {
		priority = 0;
	}
	
	public Reaction(int priority) {
		this.priority = priority;
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
