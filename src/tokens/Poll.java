package tokens;

import java.util.ArrayList;
import java.util.HashMap;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class Poll {
	public String name;
	public String description;
	public HashMap<String, ArrayList<String>> options;
	public boolean locked;
	public IUser author;
	
	public Poll( String name, String description, ArrayList<String> choices, IUser author ) {
		this.name = name;
		this.description = description;
		this.author = author;
		options = new HashMap<String, ArrayList<String>>();
		for( String choice : choices ) {
			options.put(choice, new ArrayList<String>());
		}
	}
}
