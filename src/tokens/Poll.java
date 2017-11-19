package tokens;

import java.util.ArrayList;
import java.util.HashMap;

public class Poll {
	public String name;
	public String description;
	public HashMap<String, ArrayList<String>> options;
	public boolean locked;
	
	public Poll( String name, String description, ArrayList<String> choices ) {
		this.name = name;
		this.description = description;
		options = new HashMap<String, ArrayList<String>>();
		for( String choice : choices ) {
			options.put(choice, new ArrayList<String>());
		}
	}
}
