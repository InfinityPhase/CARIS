package modules.invokers;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class NicknameInvoker extends Invoker {

	public NicknameInvoker() {
		this( Status.ENABLED );
	}

	public NicknameInvoker( Status status ) {
		this.status = status;
		name = "cNick";
		prefix = "cNick";
		help = "**__cNick__**"  +
				"\nThis command changes your Discord nickname."  +
				"\nThis change is only visible in your current guild, and will not display in any other guilds you are a part of."  +
				"\nYou must specify a subcommand with this invoker." + 
				"\n\t\t` set <Name> `\\t\\t-\\t\\t*Change your nickname to the given Name*"  +
				"\n\t\t` reset `\\t\\t-\\t\\t*Reset your nickname to your global Discord username*" +
				"\n"  +
				"\n```cNick set caris```" +
				"\n```cNick reset```";
	}

	@Override
	public Response process(MessageReceivedEvent event) {
		tokenSetup(event);
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
		return build();
	}

}
