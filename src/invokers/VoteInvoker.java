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
				response = "Syntax Error: Command not specified.";
				return build();
			}
			String action = tokens.get(1);
			if( action.equals("new") ) {
				if( tokens.size() < 5 ) {
					response = "Syntax Error: Insufficient parameters.";
					return build();
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
					response =  "Syntax Error: Insufficient parameters.";
					return build();
				}
				String name = tokens.get(2);
				if( !variables.polls.keySet().contains(name) ) {
					response =  "Poll \"" + name + "\" does not exist.";
					return build();
				}
				
				embed = variables.polls.get(name).end();
				variables.polls.remove(name);
			} else if( action.equals("cast") ) {
				if( tokens.size() < 4 ) {
					response =  "Syntax Error: Insufficient parameters.";
					return build();
				}
				String name = tokens.get(2);
				if( !variables.polls.keySet().contains(name) ) {
					response =  "Poll \"" + name + "\" does not exist.";
					return build();
				}
				String choice = tokens.get(3);
				if( !variables.polls.get(name).options.keySet().contains(choice) ) {
					response =  "Option \"" + choice + "\" does not exist.";
					return build();
				}
				embed = variables.polls.get(name).cast(event.getAuthor(), choice);
				
			} else if( action.equals("add") ) {
				if( tokens.size() < 4 ) {
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String name = tokens.get(2);
				if( !variables.polls.keySet().contains(name) ) {
					response =  "Poll \"" + name + "\" does not exist.";
					return build();
				}
				if( variables.polls.get(name).locked ) {
					response = "Poll \"" + name + "\" is locked!";
					return build();
				}
				String choice = tokens.get(3);
				if( variables.polls.get(name).options.keySet().contains(choice) ) {
					response =  "Option \"" + choice + "\" already exists.";
					return build();
				}
				embed = variables.polls.get(name).add(choice);
			} else if( action.equals("del") ) {
				if( tokens.size() < 4 ) {
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String name = tokens.get(2);
				if( !variables.polls.keySet().contains(name) ) {
					response =  "Poll \"" + name + "\" does not exist.";
					return build();
				}
				if( variables.polls.get(name).locked ) {
					response = "Poll \"" + name + "\" is already locked!";
					return build();
				}
				String choice = tokens.get(3);
				if( !variables.polls.get(name).options.keySet().contains(choice) ) {
					response =  "Option \"" + choice + "\" already exists.";
					return build();
				}
				embed = variables.polls.get(name).remove(choice);
			} else if( action.equals("check") ) {
				if( tokens.size() < 3 ) {
					response =  "Insufficient parameters.";
					return build();
				}
				String name = tokens.get(2);
				if( !variables.polls.containsKey(name) ) {
					response =  "Poll \"" + name + "\" does not exist.";
					return build();
				}
				embed = variables.polls.get(name).check();
			} else if( action.equals("lock") ) {
				if( tokens.size() < 3 ) {
					response =  "Insufficient parameters.";
					return build();
				}
				String name = tokens.get(2);
				if( !variables.polls.containsKey(name) ) {
					response =  "Poll \"" + name + "\" does not exist.";
					return build();
				}
				if( variables.polls.get(name).locked ) {
					response = "Poll \"" + name + "\" is already locked!";
					return build();
				}
				variables.polls.get(name).locked = true;
				response = "Poll \"" + name + "\" locked!";
			}  else if( action.equals("unlock") ) {
				if( tokens.size() < 3 ) {
					response =  "Insufficient parameters.";
					return build();
				}
				String name = tokens.get(2);
				if( !variables.polls.containsKey(name) ) {
					response =  "Poll \"" + name + "\" does not exist.";
					return build();
				}
				if( !variables.polls.get(name).locked ) {
					response = "Poll \"" + name + "\" is already unlocked!";
					return build();
				}
				variables.polls.get(name).locked = false;
				response = "Poll \"" + name + "\" unlocked!";
			} else {
				response =  "Unrecognized command: \"" + action + "\".";
				return build();
			}
		}
		return build();
	}
	
}
