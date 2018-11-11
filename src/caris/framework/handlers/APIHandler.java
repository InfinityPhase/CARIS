package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.Logger;
import caris.framework.utilities.TheBlueAllianceAPI;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;

public class APIHandler extends MessageHandler {
	
	public APIHandler() {
		super("API", Access.DEFAULT, false);
		description = "Retrieves the raw JSON body from an API endpoint.";
		usage.put(getKeyword() + " \"path\"", "Prints the results of the specified path from the TBA endpoint.");
	}
	
	@Override
	public boolean isTriggered(Event event) {
		return isInvoked();
	}
	
	@Override
	public Reaction process(Event event) {
		Logger.debug("API detected", 2);
		ArrayList<String> quoted = TokenUtilities.parseQuoted(message);
		if( quoted.size() > 0 ) {
			Logger.debug("Response produced from " + name, 1, true);
			return new ReactionMessage("" + TheBlueAllianceAPI.getJSON(quoted.get(0)), mrEvent.getChannel(), 1);
		} else {
			Logger.debug("Operation failed because no path specified", 2);
			Logger.debug("Response produced from " + name, 1, true);
			return new ReactionMessage("You need to specify the path! Did you put them in quotes?", mrEvent.getChannel(), 1);
		}
	}
	
	

}
