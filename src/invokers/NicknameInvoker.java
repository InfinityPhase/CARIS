package invokers;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class NicknameInvoker extends Invoker {
	
	@Override
	public Response process(MessageReceivedEvent event) {
		System.out.println("NickInvoker");
		setup(event);
		if( tokens.get(0).equals("name") ) {
			if( tokens.size() < 2 ) {
				response = "Syntax Error: Command not specified.";
				return build();
			}
			String action = tokens.get(1);
			if( action.equals("set") ) {
				if( tokens.size() < 3 ) {
					response = "Syntax Error: Insufficient parameters.";
					return build();
				}
				String name = tokens.get(2);
				response = "Nickname set to \"" + name + "\"!";
			} else if( action.equals("reset") ) {
				response = "Nickname set to \"" + event.getAuthor().getName() + "\"!";
			} else {
				response =  "Unrecognized command: \"" + action + "\".";
				return build();
			}
		}
		return build();
	}
	
}