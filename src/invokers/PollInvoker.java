package invokers;

import java.util.ArrayList;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.LineSet;
import tokens.Poll;
import tokens.Response;

public class PollInvoker extends Invoker_Multiline {
	
	public Response process(MessageReceivedEvent event) {
		multilineSetup(event);
		
		if( tokens.get(0).equals("cPoll:") ) {
			log.indent(1).log("PollInvoker triggered.");
			String target = remainder(primaryLineSet.tokens.get(0), primaryLineSet.line);
			if( target.isEmpty() ) {
				log.indent(2).log("SyntaxError. Aborting.");
				response = "Please enter a valid Poll name.";
				return build();
			} else if( variables.polls.containsKey(target) ) {
				Poll p = variables.polls.get(target);
				for( LineSet ls : auxiliaryLineSets ) {
					String command = ls.tokens.get(0);
					if( command.equalsIgnoreCase("add") ) {
						String choice = remainder(command, ls.line);
						if( p.options.keySet().contains(choice) ) {
							response +=  "Option \"" + choice + "\" already exists.";
						} else if( choice.isEmpty() ) {
							response += "Please enter a valid option.";
						} else {
							if( auxiliaryLineSets.size() == 1 ) {
								embed = variables.pollBuilder.add(p, choice);
							} else {
								variables.pollBuilder.add(p, choice);
							}
						}
					} else if( command.equalsIgnoreCase("remove") || command.equals("delete") || command.equals("rem") || command.equals("del")) {
						String choice = remainder(command, ls.line);
						if( !p.options.keySet().contains(choice) ) {
							response +=  "Option \"" + choice + "\" doesn't exist.";
						} else if( choice.isEmpty() ) {
							response += "Please enter a valid option.";
						} else {
							if( auxiliaryLineSets.size() == 1 ) {
								embed = variables.pollBuilder.remove(p, choice);
							} else {
								variables.pollBuilder.remove(p, choice);
							}
						}
					} else if( command.equalsIgnoreCase("reset") ) {
						if( auxiliaryLineSets.size() == 1 ) {
							embed = variables.pollBuilder.reset(p);
						} else {
							variables.pollBuilder.reset(p);
						}
					} else if( command.equalsIgnoreCase("end") ) {
						embed = variables.pollBuilder.end(p);
						break;
					} else if( command.equalsIgnoreCase("desc") || command.equalsIgnoreCase("description") ) {
						String text = remainder(ls.tokens.get(0), ls.line);
						if( text.isEmpty() ) {
							response += "Please enter a valid description.";
						} else {
							p.description = text;
							if( auxiliaryLineSets.size() == 1 ) {
								embed = variables.pollBuilder.check(p);
							}
						}
					}
				}
				if( auxiliaryLineSets.size() > 1 ) {
					embed = variables.pollBuilder.check(p);
				}
			} else {
				String description = "";
				ArrayList<String> options = new ArrayList<String>();
				for( LineSet ls : auxiliaryLineSets ) {
					String command = ls.tokens.get(0);
					if( command.equalsIgnoreCase("desc") || command.equalsIgnoreCase("description") ) {
						String text = remainder(ls.tokens.get(0), ls.line);
						if( text.isEmpty() ) {
							response += "Please enter a valid description.";
						} else {
							description = text;
						}
					} else if( command.equalsIgnoreCase("option") ) {
						String text = remainder(ls.tokens.get(0), ls.line);
						if( text.isEmpty() ) {
							response += "Please enter a valid option.";
						} else {
							options.add(text);
						}
					}
				}
				variables.polls.put(target, new Poll(target, description, options, event.getAuthor()));
				embed = variables.pollBuilder.start(variables.polls.get(target));
			}
		}
		return build();
	}
	
}
