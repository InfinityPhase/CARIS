package caris.framework.basereactions;

public class Reaction implements Runnable, Comparable<Reaction> {
	
	public int priority;
	
	public Reaction() {
		this(0);
	}
	
	public Reaction(int priority) {
		this.priority = priority;
	}
	
	@Override
	public void run() {}
	
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
