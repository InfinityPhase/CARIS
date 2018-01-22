package invokers;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import tokens.LineSet;
import tokens.Poll;
import tokens.Response;

public class VoteInvoker extends Invoker_Multiline {

	@Override
	public Response process(MessageReceivedEvent event) {
		multilineSetup(event);
		
		if( event.getMessage().getContent().equals("cVote") ) {
			EmbedBuilder builder = new EmbedBuilder();
			log.indent(1).log("VoteInvoker triggered.");
			builder.withTitle("**__Active Polls__**");
			for( String name : variables.polls.keySet() ) {
				Poll p = variables.polls.get(name);
				builder.appendField(p.name, p.getVotes() + "vote(s)!", false);
			}
			embed = builder;
		}
		
		if( tokens.get(0).equals("cVote:") ) {
			log.indent(1).log("VoteInvoker triggered.");
			String target = remainder(primaryLineSet.tokens.get(0), primaryLineSet.line);
			if( target.isEmpty() || !variables.polls.keySet().contains(target) ) {
				log.indent(2).log("Syntax Error. Aborting");
				response = "Please enter a valid Poll name.";
				return build();
			} else {
				Poll p = variables.polls.get(target);
				if( auxiliaryLineSets.isEmpty() ) {
					embed = variables.pollBuilder.check(p, event.getAuthor());
				} else {
					for( LineSet ls : auxiliaryLineSets ) {
						if( ls.line.isEmpty() || !variables.polls.get(target).options.keySet().contains(ls.line)) {
							response = "Please enter a valid option.";
						} else {
							embed = variables.pollBuilder.cast(p, event.getAuthor(), ls.line);
						}
					}
				}
			}
		}
		return build();
	}
	
}
