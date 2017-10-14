package invokers;

import java.util.ArrayList;

import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Poll;
import utilities.Handler;

public class VoteInvoker extends Handler {
	// TODO: CONSOLIDATE MY GODDAMN VARIABLE REFERENCES
	public String process(MessageReceivedEvent event) {
		String response = "";
		String messageText = format(event);
		ArrayList<String> tokens = Brain.tp.parse(event.getMessage().getContent().toLowerCase());
		tokens.remove(0);
		
		if( tokens.get(0).equals("vote") ) {
			if( tokens.size() < 2 ) {
				return "Syntax Error: Command not specified.";
			}
			String action = tokens.get(1);
			if( action.equals("new") ) {
				if( tokens.size() < 5 ) {
					return "Syntax Error: Insufficient parameters.";
				}
				String name = tokens.get(2);
				String description = tokens.get(3);
				ArrayList<String> choices = new ArrayList<String>();
				for( int f=4; f<tokens.size(); f++ ) {
					choices.add(tokens.get(f));
				}
				Brain.guildIndex.get(event.getGuild()).polls.put(name, new Poll(name, description, choices));
				response = "Poll \"" + name + "\" created.";
				response += "\n" + Brain.guildIndex.get(event.getGuild()).polls.get(name).check();
			} else if( action.equals("end") ) {
				if( tokens.size() < 3 ) {
					return "Syntax Error: Insufficient parameters.";
				}
				String name = tokens.get(2);
				if( !Brain.guildIndex.get(event.getGuild()).polls.keySet().contains(name) ) {
					return "Poll \"" + name + "\" does not exist.";
				}
				response = "Poll \"" + name + "\" ended.";
				response += "\n" + Brain.guildIndex.get(event.getGuild()).polls.get(name).end();
				Brain.guildIndex.get(event.getGuild()).polls.remove(name);
			} else if( action.equals("cast") ) {
				if( tokens.size() < 4 ) {
					return "Syntax Error: Insufficient parameters.";
				}
				String name = tokens.get(2);
				if( !Brain.guildIndex.get(event.getGuild()).polls.keySet().contains(name) ) {
					return "Poll \"" + name + "\" does not exist.";
				}
				String choice = tokens.get(3);
				if( !Brain.guildIndex.get(event.getGuild()).polls.get(name).options.keySet().contains(choice) ) {
					return "Option \"" + choice + "\" does not exist.";
				}
				response = Brain.guildIndex.get(event.getGuild()).polls.get(name).cast(event.getAuthor(), choice);
				
			} else if( action.equals("check") ) {
				if( tokens.size() < 3 ) {
					return "Insufficient parameters.";
				}
				String name = tokens.get(2);
				if( !Brain.guildIndex.get(event.getGuild()).polls.containsKey(name) ) {
					return "Poll \"" + name + "\" does not exist.";
				}
				response = Brain.guildIndex.get(event.getGuild()).polls.get(name).check();
			} else {
				return "Unrecognized command: \"" + action + "\".";
			}
		}
		return response;
	}
	
}
