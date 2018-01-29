package responders;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class HelpResponder extends Responder {
	
	public HelpResponder() {
		this(Status.ENABLED);
	}
	
	public HelpResponder( Status status ) {
		this.status = status;
		name = "Help";
	}
	
	@Override
	public Response process(MessageReceivedEvent event) {
		tokenSetup(event);
		
		if( tokens.contains("caris") && tokens.contains("how") && (tokens.contains("use") || tokens.contains("work")) ) {
			response += "**__Help__**";
			response += "\nCaris is controlled using two types of commands: *Invokers*, and *Responders*.";
			response += "\n*Invokers* are commands that you specifically activate, while *Responders* utilize \"natural language processing\" to jump in at the right time.";
			response += "\n";
			
			response += "\n__Invokers__";
			response += "\nA very simple example of an *Invoker* is **cEcho**.";
			response += "\nTo use it, simply type ` cEcho: <message> `, and CARIS will repeat what you put as the ` <message> `.";
			response += "\nTo see a list of *Invokers*, type ` cHelp: Invoker `.";

			response += "\n";
			
			response += "\n__Responders__";
			response += "\nA simple responder would be CARIS's **Mention Responder**, which causes her to respond whenever someone says her name.";
			response += "\nTo see a list of *Responders*, type ` cHelp: Responder `.";
			response += "\n";
		}
		
		return build();
	}
	
}
