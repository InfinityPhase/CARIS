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
			Brain.log.debugOut("\t\t\t\tLocation query detected.");
			for( String person : variables.people.keySet() ) {
				if( containsIgnoreCase(messageText, person) ) {
					Brain.log.debugOut("\t\t\t\t\tName \"" + person + "\" detected.");
					Brain.log.debugOut("\t\t\t\t\tLocationResponder triggered.");
					response = person + " is at " + variables.people.get(person) + ".";
					break;
				}
			}
		} else if( hasIgnoreCase(tokens, "who") && hasIgnoreCase(tokens, "is") && hasIgnoreCase(tokens, "at") || (hasIgnoreCase(tokens, "whos") || hasIgnoreCase(tokens, "who's")) && hasIgnoreCase(tokens, "at") ) {
			Brain.log.debugOut("\t\t\t\tPerson query detected.");
			for( String location : variables.locations.keySet() ) {
				if( containsIgnoreCase(messageText, location) ) {
					Brain.log.debugOut("\t\t\t\t\tLocation \"" + location + "\" detected.");
					if( !hasIgnoreCase(variables.locations.keySet(), location) ) {
						Brain.log.debugOut("\t\t\t\t\tLocation not set.");
						Brain.log.debugOut("\t\t\t\t\tLocationResponder triggered.");
						response = location + " is not a set location.";
						return build();
					}
					ArrayList<String> locals = variables.locations.get(location);
					if( locals.size() == 0 ) {
						Brain.log.debugOut("\t\t\t\t\tLocation \t" + location + "\" empty.");
						Brain.log.debugOut("\t\t\t\t\tLocationResponder triggered.");
						response = "No one is at " + location + ".";
					} else {
						Brain.log.debugOut("\t\t\t\t\tPeople detected at location.");
						response = "The following people are at " + location + ": ";
						for( int f=0; f<locals.size(); f++ ) {
							response += locals.get(f);
							if( f < locals.size()-1 ) {
								response += ", ";
							} else {
								response += ".";
							}
						}
						Brain.log.debugOut("\t\t\t\t\tLocaationResponder triggered.");
					}
					break;
				}
			}
		} else { 
			Brain.log.debugOut("\t\t\t\tLocationResponder unactivated.");
		}
		Brain.log.debugOut("\t\t\tLocationResponder processed.");
		return build();
	}

}
