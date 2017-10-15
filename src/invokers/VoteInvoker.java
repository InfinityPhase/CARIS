package invokers;

import java.util.ArrayList;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Poll;
import tokens.Response;

public class VoteInvoker extends Invoker {

	public Response process(MessageReceivedEvent event) {
		setup(event);
		
		if( tokens.get(0).equals("vote") ) {
			if( tokens.size() < 2 ) {
				return build( "Syntax Error: Command not specified.");
			}
			String action = tokens.get(1);
			if( action.equals("new") ) {
				if( tokens.size() < 5 ) {
					return build( "Syntax Error: Insufficient parameters.");
				}
				String name = tokens.get(2);
				String description = tokens.get(3);
				ArrayList<String> choices = new ArrayList<String>();
				for( int f=4; f<tokens.size(); f++ ) {
					choices.add(tokens.get(f));
				}
				variables.polls.put(name, new Poll(name, description, choices));
				embed = (variables.polls.get(name).start());
			} else if( action.equals("end") ) {
				if( tokens.size() < 3 ) {
					return build( "Syntax Error: Insufficient parameters.");
				}
				String name = tokens.get(2);
				if( !variables.polls.keySet().contains(name) ) {
					return build( "Poll \"" + name + "\" does not exist.");
				}
				
				embed = variables.polls.get(name).end();
				variables.polls.remove(name);
			} else if( action.equals("cast") ) {
				if( tokens.size() < 4 ) {
					return build( "Syntax Error: Insufficient parameters.");
				}
				String name = tokens.get(2);
				if( !variables.polls.keySet().contains(name) ) {
					return build( "Poll \"" + name + "\" does not exist.");
				}
				String choice = tokens.get(3);
				if( !variables.polls.get(name).options.keySet().contains(choice) ) {
					return build( "Option \"" + choice + "\" does not exist.");
				}
				embed = variables.polls.get(name).cast(event.getAuthor(), choice);
				
			} else if( action.equals("check") ) {
				if( tokens.size() < 3 ) {
					return build( "Insufficient parameters.");
				}
				String name = tokens.get(2);
				if( !variables.polls.containsKey(name) ) {
					return build( "Poll \"" + name + "\" does not exist.");
				}
				embed = variables.polls.get(name).check();
			} else {
				return build( "Unrecognized command: \"" + action + "\".");
			}
		}
		return build(response);
	}
	
}
