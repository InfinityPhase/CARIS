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
				log.indent(1).log("StatusController triggered.");
				if( tokens.size() > 1 ) {
					if( tokens.get(1).equals("set") ) {
						Brain.cli.changePlayingText(message);
						log.indent(2).log("Status set to \"" + message + "\".");
					} else if( tokens.get(1).equals("reset") ) {
						Brain.cli.changePlayingText(Constants.DEFAULT_PLAYING_TEXT);
						log.indent(2).log("Status reset.");
					}
				}
			}
		}
		return build();
	}
	
}
