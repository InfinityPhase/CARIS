package modules.constructors;

import java.util.ArrayList;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import tokens.LineSet;
import tokens.Poll;
import tokens.Response;

public class PollConstructor extends Constructor {

	public PollConstructor() {
		this( Status.ENABLED );
	}

	public PollConstructor( Status status ) {
		this.status = status;
		name = "cPoll";
		prefix = "cPoll";
		help = "**__cPoll__**"  +
				"\nThis command lets you create polls other people can vote on."  +
				"\nUse ` cPoll: <Poll Name> ` as the *Main Command*."  +
				"\nIf no subcommands are used, CARIS will display the current state of the poll."  +
				"\n"  +
				"\n\t\t` description <Text> `\t\t-\t\t*Sets the description or question the poll is asking*"  +
				"\n\t\t` option <Text> `\t\t-\t\t*Adds a votable option to the poll*"  +
				"\n\t\t` add <Text> `\t\t-\t\t*If the poll already exists, adds a new option to the poll*"  +
				"\n\t\t` remove <Text> `\t\t-\t\t*If the poll already exists, removes an option from the poll*"  +
				"\n\t\t` reset `\t\t-\t\t*Removes all votes from the poll*"  +
				"\n\t\t` end `\t\t-\t\t*Ends the poll and displays the results*"  +
				"\n"  +
				"\n```cPoll: Apples v Oranges"  +
				"\ndescription Do you prefer apples, or oranges?"  +
				"\noption Apples"  +
				"\noption Oranges```";
	}

	@Override
	public Response process(MessageReceivedEvent event) {
		multilineSetup(event);

		if( tokens.size() == 1 ) { // No arguments passed
			EmbedBuilder builder = new EmbedBuilder();
			log.indent(1).log("PollInvoker triggered.");
			builder.withTitle("**__Active Polls__**");
			for( String name : variables.polls.keySet() ) {
				Poll p = variables.polls.get(name);
				builder.appendField(p.name, p.getVotes() + " vote(s)!", false);
			}
			embed = builder;
		} else if( tokens.size() > 1 ) { // Has arguments
			log.indent(1).log("PollInvoker triggered.");
			String target = remainder(primaryLineSet.tokens.get(0), primaryLineSet.line).toLowerCase();
			if( target.isEmpty() ) {
				log.indent(2).log("SyntaxError. Aborting.");
				response = "Please enter a valid Poll name.";
				return build();
			} else if( variables.polls.containsKey(target) ) {
				Poll p = variables.polls.get(target);
				if( auxiliaryLineSets.isEmpty() ) {
					embed = variables.pollBuilder.check(p, event.getAuthor());
				} else if( !p.author.equals(event.getAuthor()) ) {
					response = "You do not have permission to edit this poll.";
					return build();
				} else {
					for( LineSet ls : auxiliaryLineSets ) {
						String command = ls.tokens.get(0);
						if( command.equalsIgnoreCase("add") ) {
							String choice = remainder(command, ls.line).toLowerCase();
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
							String choice = remainder(command, ls.line).toLowerCase();
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
									embed = variables.pollBuilder.check(p, event.getAuthor());
								}
							}
						}
					}
					if( auxiliaryLineSets.size() > 1 ) {
						embed = variables.pollBuilder.check(p, event.getAuthor());
					}
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
						text.toLowerCase();
						if( text.isEmpty() ) {
							response += "Please enter a valid option.";
						} else {
							options.add(text);
						}
					}
				}
				variables.polls.put(target, new Poll(target, description, options, event.getAuthor(), event.getGuild()));
				embed = variables.pollBuilder.start(variables.polls.get(target));
				event.getMessage().delete();
			}
		}
		return build();
	}

}
