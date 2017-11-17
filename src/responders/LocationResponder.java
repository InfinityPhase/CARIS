package responders;

import java.util.ArrayList;

import library.Constants;
import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class LocationResponder extends Responder {

	@Override
	public Response process(MessageReceivedEvent event) {
		setup(event);

		if( hasIgnoreCase(tokens, "where's") || containsIgnoreCase(tokens, "where") || hasIgnoreCase(tokens, "wheres") ) {
			Brain.log.debugOut("Location query detected.", 4);
			for( String person : variables.people.keySet() ) {
				if( containsIgnoreCase(messageText, person) ) {
					Brain.log.debugOut("Name \"" + person + "\" detected.", 5);
					Brain.log.debugOut("LocationResponder triggered.", 5);
					response = person + " is at " + variables.people.get(person) + ".";
					break;
				}
			}
		} else if( hasIgnoreCase(tokens, "who") && hasIgnoreCase(tokens, "is") && hasIgnoreCase(tokens, "at") || (hasIgnoreCase(tokens, "whos") || hasIgnoreCase(tokens, "who's")) && hasIgnoreCase(tokens, "at") ) {
			Brain.log.debugOut("Person query detected.", 4);
			for( String location : variables.locations.keySet() ) {
				if( containsIgnoreCase(messageText, location) ) {
					Brain.log.debugOut("Location \"" + location + "\" detected.", 5);
					if( !hasIgnoreCase(variables.locations.keySet(), location) ) {
						Brain.log.debugOut("Location not set.", 5);
						Brain.log.debugOut("LocationResponder triggered.", 5);
						response = location + " is not a set location.";
						return build();
					}
					ArrayList<String> locals = variables.locations.get(location);
					if( locals.size() == 0 ) {
						Brain.log.debugOut("Location \t" + location + "\" empty.", 5);
						Brain.log.debugOut("LocationResponder triggered.", 5);
						response = "No one is at " + location + ".";
					} else {
						Brain.log.debugOut("People detected at location.", 5);
						response = "The following people are at " + location + ": ";
						for( int f=0; f<locals.size(); f++ ) {
							response += locals.get(f);
							if( f < locals.size()-1 ) {
								response += ", ";
							} else {
								response += ".";
							}
						}
						Brain.log.debugOut("LocaationResponder triggered.", 5);
					}
					break;
				}
			}
		} else { 
			Brain.log.debugOut("LocationResponder unactivated.", 4);
		}
		Brain.log.debugOut("LocationResponder processed.", 3);
		return build();
	}

}
