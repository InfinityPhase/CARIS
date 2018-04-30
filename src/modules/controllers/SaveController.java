package modules.controllers;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class SaveController extends Controller {
	
	public SaveController() {
		this(Status.ENABLED);
	}
	
	public SaveController( Status status ) {
		this.status = status;
		name = "cSave";
	}
	
	@Override
	public Response process(MessageReceivedEvent event) {
		tokenSetup(event);
		if( tokens.size() > 0 ) {
			if( tokens.get(0).equals("save") ) {
				// TODO Implement database backup
				response = "Data saved.";
			}
		}
		return build();
	}
}
