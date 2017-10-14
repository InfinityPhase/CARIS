package tokens;

import java.util.ArrayList;
import java.util.HashMap;

import sx.blah.discord.handle.obj.IUser;

public class Poll {
	public String name;
	public String description;
	public HashMap<String, ArrayList<String>> options;
	
	public Poll( String name, String description, ArrayList<String> choices ) {
		this.name = name;
		this.description = description;
		options = new HashMap<String, ArrayList<String>>();
		for( String choice : choices ) {
			options.put(choice, new ArrayList<String>());
		}
	}
	
	public String check() {
		String response = "";
		response += "__" + name + "__";
		response += "\n*" + description + "*";
		for( String option : options.keySet() ) {
			response += "\n  **" + option + "** : " + options.get(option).size(); 
		}
		response += "\nType \"==> vote cast \"" + name + "\" <option>" + "\" to vote.";
		return response;
	}
	
	public String end() {
		String response = "";
		response += "__" + name + "__";
		response += "\n*" + description + "*";
		for( String option : options.keySet() ) {
			response += "\n  **" + option + "** : " + options.get(option).size(); 
		}
		int max = 0;
		String winner = "";
		boolean multiWinner = false;
		for( String option : options.keySet() ) {
			int s = options.get(option).size();
			if( s > max ) {
				max = s;
				winner = option;
				multiWinner = false;
			} else if( s == max && max != 0 ) {
				winner += " and " + option;
				multiWinner = true;
			}
		}
		if( max == 0 ) {
			response += "\nThere are no winners.";
		} else if( multiWinner ) {
			response += "\nThe winners are " + winner + ", with " + max;
			if( max == 1 ) {
				response += " vote!";
			} else {
				response += "votes!";
			}
		} else {
			response += "\nThe winner is " + winner + ", with " + max;
			if( max == 1 ) {
				response += " vote!";
			} else {
				response += "votes!";
			}
		}
		return response;
	}
	public String cast(IUser user, String choice) {
		String response = "";
		boolean voted = false;
		for( String option : options.keySet() ) {
			if( options.get(option).contains(user.getName()) ) {
				options.get(option).remove(user.getName());
				options.get(choice).add(user.getName());
				response = "Changed vote to \"" + choice +"\"!\n";
				voted = true;
			}
		}
		if( !voted ) {
			options.get(choice).add(user.getName());
			response += "Cast vote for \"" + choice + "\"!\n"; 
		}
		response += check();
		return response;
	}
}
