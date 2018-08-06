package caris.framework.tokens;

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
	public IGuild guild;
	
	public Poll( String name, String description, ArrayList<String> choices, IUser author, IGuild guild ) {
		this.name = name;
		this.description = description;
		this.author = author;
		this.guild = guild;
		options = new HashMap<String, ArrayList<String>>();
		for( String choice : choices ) {
			options.put(choice, new ArrayList<String>());
		}
	}
	
	public int getVotes() {
		int count = 0;
		for( String key : options.keySet() ) {
			count += options.get(key).size();
		}
		return count;
	}
}
