package tokens;

public class Response implements Comparable {
	// Potential responses
	public String text;
	public int priority;
	public Response(String text, int priority) {
		this.text = text;
		this.priority = priority;
	}
	@Override
	public int compareTo(Object o) { // sort with higher priority first
		if( o instanceof Response ) {
			int compare = ((Response) o).priority;
			if( this.priority < compare ) {
				return 1;
			} else if( this.priority > compare ) {
				return 0;
			} else {
				return -1;
			}
		} else {
			return 0;
		}
	}
}
