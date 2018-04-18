package modules.invokers;

import library.Variables;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class RulesInvoker extends Invoker {
	public RulesInvoker() {
		this( Status.ENABLED );
	}

	public RulesInvoker( Status status ) {
		log.log("Initializing Rules Invoker");
		this.status = status;
		name = "cRules";
		prefix = "cRules";
		help = "\n__cRules__"  +
				"\nThis command allows you to see the server rules.\n" +
				"\n```cRules```";
	}
	
	@Override
	public Response process(MessageReceivedEvent event) {
		linesetSetup(event);
		if( Variables.guildIndex.get(event.getGuild()).rules.isEmpty() ) {
			response = "There are no rules set for this server. Anarchy!";
		} else {
			response = "**__Server Rules__**\n" + Variables.guildIndex.get(event.getGuild()).rules;
		}
		return build();
	}
}
