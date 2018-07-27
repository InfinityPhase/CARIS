package modules.controllers;

import library.Variables;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import tokens.Response;

public class SayController extends Controller {
	
	public SayController() {
		this(Status.ENABLED);
	}
	
	public SayController( Status status ) {
		this.status = status;
		name = "cSay";
	}
	
	@Override
	public Response process(MessageReceivedEvent event) {
		messageSetup(event);
		if( tokens.get(0).equals("say") ) {
			if( tokens.size() < 2 ) {
				log.indent(3).log("Syntax Error. Aborting.");
				return build();
			}
			for( IGuild g : Variables.guildIndex.keySet() ) {
				for( IChannel c : g.getChannels() ) {
					if(tokens.get(1).equals(c.getStringID())) {
						recipient.add(c);
						response = message;
					}
				}
			}
		}
		return build();
	}
}
