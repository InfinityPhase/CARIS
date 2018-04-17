package modules.controllers;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class ModuleController extends Controller {
	
	public ModuleController() {
		this(Status.ENABLED);
	}
	
	public ModuleController( Status status ) {
		this.status = status;
		name = "cModule";
	}

	@Override
	public Response process(MessageReceivedEvent event) {
		messageSetup(event);
		if( tokens.size() > 0 ) {
			if( tokens.get(0).equals("module") || tokens.get(0).equals("modules") ) {
				if( tokens.size() > 1 ) {
					if( !message.isEmpty() ) {
						if( variables.modules.keySet().contains(message) ) {
							boolean state = variables.modules.get(message);
							if( tokens.get(1).equals("enable") ) {
								if( state ) {
									response = "Module \"" + message + "\" already enabled.";
								} else {
									variables.modules.put(message, true);
									response = "Module \"" + message + "\" enabled!";
								}
							} else if( tokens.get(1).equals("disable") ) {
								if( !state ) {
									response = "Module \"" + message + "\" already disabled.";
								} else {
									variables.modules.put(message, false);
									response = "Module \"" + message + "\" disabled!";
								}
							}
						}
					}
					if( (tokens.get(1).equals("list") || tokens.get(1).equals("status") || tokens.get(1).equals("state")) && response.isEmpty() ) {
						embed = variables.moduleStatusBuilder.list(variables.name, variables.modules);
					}
				}
			}
		}
		return build();
	}
	
	@Override
	protected String format(MessageReceivedEvent event) {
		String messageText = event.getMessage().getContent();
		return messageText;
	}
}
