package embedbuilders;

import java.util.ArrayList;

import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import tokens.Poll;

public class PollBuilder {
	
	public PollBuilder() {}
	
	public EmbedBuilder start(Poll p) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withTitle("**__" + p.name + "__**");
		builder.withDesc("*" + p.description + "*");
		for( String option : p.options.keySet() ) {
			builder.appendField(option, p.options.get(option).size() + " votes!", false);
		}
		builder.withFooterText("Type `.c vote cast \"" + p.name + "\" <option>" + "` to vote.");
		builder.withAuthorName("Poll created!");
		return builder;
	}
	
	public EmbedBuilder check(Poll p) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withTitle("**__" + p.name + "__**");
		builder.withDesc("*" + p.description + "*");
		for( String option : p.options.keySet() ) {
			builder.appendField(option, p.options.get(option).size() + " votes!", false);
		}
		builder.withFooterText("Type `.c vote cast \"" + p.name + "\" <option>" + "` to vote.");
		builder.withAuthorName("Poll Status:");
		return builder;
	}
	
	public EmbedBuilder end(Poll p) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withTitle("**__" + p.name + "__**");
		builder.withDesc("*" + p.description + "*");
		for( String option : p.options.keySet() ) {
			builder.appendField(option, p.options.get(option).size() + " votes!", false);
		}

		int max = 0;
		String winner = "";
		boolean multiWinner = false;
		for( String option : p.options.keySet() ) {
			int s = p.options.get(option).size();
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
			if( p.options.size() == 1 ) {
				builder.withAuthorName(p.options.get(0) + " wins by default!");
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
	public EmbedBuilder cast(Poll p, IUser user, String choice) {
		EmbedBuilder builder = new EmbedBuilder();
		boolean voted = false;
		String previous = "";
		for( String option : p.options.keySet() ) {
			if( p.options.get(option).contains(user.getName()) ) {
				p.options.get(option).remove(user.getName());
				p.options.get(choice).add(user.getName());
				voted = true;
				previous = option;
			}
		}
		if( !voted ) {
			p.options.get(choice).add(user.getName());
		}
		builder = check(p);
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
	public EmbedBuilder add(Poll p, String choice) {
		EmbedBuilder builder = new EmbedBuilder();
		p.options.put(choice, new ArrayList<String>());
		builder = check(p);
		builder.withAuthorName("Option \"" + choice + "\" added!");
		return builder;
	}
	public EmbedBuilder remove(Poll p, String choice) {
		EmbedBuilder builder = new EmbedBuilder();
		p.options.remove(choice);
		builder = check(p);
		builder.withAuthorName("Option \"" + choice + "\" removed!");
		return builder;
	}
}
