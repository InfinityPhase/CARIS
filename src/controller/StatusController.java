package controller;

import library.Constants;
import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class StatusController extends Controller {
	
	@Override
	public Response process(MessageReceivedEvent event) {
		messageSetup(event);
		
		if( tokens.size() > 0 ) {
			if( tokens.get(0).equals("status") ) {
				if( tokens.size() > 1 ) {
					if( !message.isEmpty() ) {
						if( variables.modules.keySet().contains(message) ) {
							if( tokens.get(1).equals("set") ) {
								Brain.cli.changePlayingText(messageText);
							} else if( tokens.get(1).equals("reset") ) {
								Brain.cli.changePlayingText(Constants.DEFAULT_PLAYING_TEXT);
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
	
}
