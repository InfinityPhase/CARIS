package modules.invokers;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import tokens.Poll;
import tokens.Response;

public class VoteInvoker extends Invoker {

	public VoteInvoker() {
		this( Status.ENABLED );
	}

	public VoteInvoker( Status status ){
		this.status = status;
		name = "cVote";
		prefix = "cVote";
		help =  "**__cVote__**"  +
				"\nThis command lets others vote on existing polls.\n"  +
				"\nUsage: ` cVote: <poll> <option 1> .. <option N>`"  +
				"\n\t` <poll> `: the name of the poll you wish to vote on." +
				"\n\t` <option 1..N> `: the choice(s) you wish to vote for.\n"  +
				"\n```cVote: color blue```";
	}

	@Override
	public Response process(MessageReceivedEvent event) {
		linesetSetup(event);

		if( command.tokens.size() == 1 ) { // No arguments passed
			EmbedBuilder builder = new EmbedBuilder();
			log.indent(1).log("VoteInvoker triggered.");
			builder.withTitle("**__Active Polls__**");
			for( String name : variables.polls.keySet() ) {
				Poll p = variables.polls.get(name);
				builder.appendField(p.name, p.getVotes() + "vote(s)!", false);
			}
			embed = builder;
		} else if( command.tokens.size() > 1 ) { // Has arguments		
			log.indent(1).log("VoteInvoker triggered.");
			if( command.tokens.size() < 2 ) {
				log.indent(2).log("Syntax Error. Aborting");
				response = "Please enter a valid Poll name.";
				return build();
			}
			String target = command.tokens.get(1).toLowerCase();
			if( !variables.polls.keySet().contains(target) ) {
				log.indent(2).log("Syntax Error. Aborting");
				response = "Please enter a valid Poll name.";
				return build();
			} else {
				Poll p = variables.polls.get(target);
				if( command.tokens.size() < 3 ) {
					embed = variables.pollBuilder.check(p, event.getAuthor());
				} else {
					String option = command.tokens.get(2).toLowerCase();
					if( !p.options.keySet().contains(option)) {
						response = "Please enter a valid option.";
					} else {
						embed = variables.pollBuilder.cast(p, event.getAuthor(), option);
					}
				}
			}
		}
		return build();
	}

}
