package invokers;

import java.util.ArrayList;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Poll;
import tokens.Response;

public class VoteInvoker extends Invoker {

	@Override
	public Response process(MessageReceivedEvent event) {
		setup(event);
		
		if( tokens.get(0).equals("vote") || tokens.get(0).equals("poll") ) {
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
				embed = variables.pollBuilder.start(variables.polls.get(name));
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
				Poll p = variables.polls.get(name);
				embed = variables.pollBuilder.end(p);
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
				Poll p = variables.polls.get(name);
				String choice = tokens.get(3);
				if( !p.options.keySet().contains(choice) ) {
					response =  "Option \"" + choice + "\" does not exist.";
					return build();
				}
				embed = variables.pollBuilder.cast(p, event.getAuthor(), choice);
				
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
				Poll p = variables.polls.get(name);
				if( p.locked ) {
					response = "Poll \"" + name + "\" is locked!";
					return build();
				}
				String choice = tokens.get(3);
				if( p.options.keySet().contains(choice) ) {
					response =  "Option \"" + choice + "\" already exists.";
					return build();
				}
				embed = variables.pollBuilder.add(p, choice);
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
				Poll p = variables.polls.get(name);
				if( p.locked ) {
					response = "Poll \"" + name + "\" is already locked!";
					return build();
				}
				String choice = tokens.get(3);
				if( !p.options.keySet().contains(choice) ) {
					response =  "Option \"" + choice + "\" already exists.";
					return build();
				}
				embed = variables.pollBuilder.remove(p, choice);
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
				Poll p = variables.polls.get(name);
				embed = variables.pollBuilder.check(p);
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
				Poll p = variables.polls.get(name);
				if( p.locked ) {
					response = "Poll \"" + name + "\" is already locked!";
					return build();
				}
				p.locked = true;
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
				Poll p = variables.polls.get(name);
				if( !p.locked ) {
					response = "Poll \"" + name + "\" is already unlocked!";
					return build();
				}
				p.locked = false;
				response = "Poll \"" + name + "\" unlocked!";
			} else {
				response =  "Unrecognized command: \"" + action + "\".";
				return build();
			}
		}
		return build();
	}
	
}
