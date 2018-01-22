package invokers;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class HelpInvoker extends Invoker_Multiline {
	
	@Override
	 public Response process(MessageReceivedEvent event) {
		multilineSetup(event);
		
		
		
		return build();
	}
	
}
