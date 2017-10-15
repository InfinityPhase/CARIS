package tokens;

import java.util.ArrayList;
import java.util.HashMap;

import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

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
	
	public EmbedBuilder start() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withTitle("**__" + name + "__**");
		builder.withDesc("*" + description + "*");
		for( String option : options.keySet() ) {
			builder.appendField(option, options.get(option).size() + " votes!", false);
		}
		builder.withFooterText("Type `==> vote cast \"" + name + "\" <option>" + "` to vote.");
		builder.withAuthorName("Poll created!");
		return builder;
	}
	
	public EmbedBuilder check() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withTitle("**__" + name + "__**");
		builder.withDesc("*" + description + "*");
		for( String option : options.keySet() ) {
			builder.appendField(option, options.get(option).size() + " votes!", false);
		}
		builder.withFooterText("Type `==> vote cast \"" + name + "\" <option>" + "` to vote.");
		builder.withAuthorName("Poll Status:");
		return builder;
	}
	
	public EmbedBuilder end() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withTitle("**__" + name + "__**");
		builder.withDesc("*" + description + "*");
		for( String option : options.keySet() ) {
			builder.appendField(option, options.get(option).size() + " votes!", false);
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
			if( options.size() == 1 ) {
				builder.withAuthorName(options.get(0) + " wins by default!");
			} else {
				builder.withAuthorName("Tie!");
			}
		} else if( multiWinner ) {
			builder.withAuthorName("The winners are " + winner + "!");
		} else {
			builder.withAuthorName("The winner is " + winner + "!");
		}
		return builder;
	}
	public EmbedBuilder cast(IUser user, String choice) {
		EmbedBuilder builder = new EmbedBuilder();
		boolean voted = false;
		String previous = "";
		for( String option : options.keySet() ) {
			if( options.get(option).contains(user.getName()) ) {
				options.get(option).remove(user.getName());
				options.get(choice).add(user.getName());
				voted = true;
				previous = option;
			}
		}
		if( !voted ) {
			options.get(choice).add(user.getName());
		}
		builder = check();
		if( voted ) {
			if( previous.equals(choice) ) {
				builder.withAuthorName("You have already voted for \"" + choice + "\"!");
			}
			builder.withAuthorName("Changed vote to \"" + choice +"\"!");
		} else {
			builder.withAuthorName("Cast vote for \"" + choice + "\"!"); 
		}
		return builder;
	}
	public EmbedBuilder add(String choice) {
		EmbedBuilder builder = new EmbedBuilder();
		options.put(choice, new ArrayList<String>());
		builder = check();
		builder.withAuthorName("Option \"" + choice + "\" added!");
		return builder;
	}
	public EmbedBuilder remove(String choice) {
		EmbedBuilder builder = new EmbedBuilder();
		options.remove(choice);
		builder = check();
		builder.withAuthorName("Option \"" + choice + "\" removed!");
		return builder;
	}
}
