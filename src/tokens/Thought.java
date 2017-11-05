package tokens;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.obj.IMessage;

public class Thought {
	// Implement multiline responses using ArrayList
	// Global config order of thoughts
	// STUFF
	
	// What you want to say
	public List< String > text = new ArrayList< String >();
	// The origional message (Optional)
	public IMessage message;
	
	public Thought( List<String> text ) {
		this.text = text;
		this.message = null;
	}
	
	public Thought( IMessage message ) {
		this.text = null;
		this.message = message;
	}
	
	public Thought( List<String> text, IMessage message ) {
		this.text = text;
		this.message = message;
	}
	
	

}
