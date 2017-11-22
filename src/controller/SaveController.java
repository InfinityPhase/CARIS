package controller;

import library.Variables;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class SaveController extends Controller {
	
	@Override
	public Response process(MessageReceivedEvent event) {
		setup(event);
		if( tokens.size() > 0 ) {
			if( tokens.get(0).equals("save") ) {
				Variables.ds.save();
				response = "Data saved.";
			}
		}
		return build();
	}
}
