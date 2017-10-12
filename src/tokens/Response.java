package tokens;

import sx.blah.discord.handle.impl.obj.Message;
import sx.blah.discord.handle.obj.IMessage;

public class Response implements Comparable {
	// Potential responses
	public String text;
	public IMessage message;
	public int priority;
	
	
	public Response(String text, int priority) {
		this.text = text;
		this.priority = priority;
		this.message = null;
	}
	
	public Response(IMessage m, int priority) {
		this.text = m.getContent();
		this.priority = priority;
		this.message = m;
	}
	
	@Override
	public int compareTo(Object o) { // sort with higher priority first
		if( o instanceof Response ) {
			int compare = ((Response) o).priority;
			if( this.priority < compare ) {
				return 1;
			} else if( this.priority > compare ) {
				return -1;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
}
