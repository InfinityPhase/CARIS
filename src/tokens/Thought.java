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
	// Title of the thought
	public String name;
	
	public Thought( List<String> text, String name ) {
		this.text = text;
		this.message = null;
		this.name = name;
	}
	
	public Thought( IMessage message, String name ) {
		this.text = null;
		this.message = message;
		this.name = name;
	}
	
	public Thought( List<String> text, IMessage message, String name ) {
		this.text = text;
		this.message = message;
		this.name = name;
	}
	
	

}
