package responders;

import java.util.ArrayList;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class LocationResponder extends Responder {

	@Override
	public Response process(MessageReceivedEvent event) {
		setup(event);

		if( hasIgnoreCase(tokens, "where's") || containsIgnoreCase(tokens, "where") || hasIgnoreCase(tokens, "wheres") ) {
			log.indent(2).log("Location query detected.");
			for( String person : variables.people.keySet() ) {
				if( containsIgnoreCase(messageText, person) ) {
					log.indent(3).log("Name \"" + person + "\" detected.");
					log.indent(3).log("LocationResponder triggered.");
					response = person + " is at " + variables.people.get(person) + ".";
					break;
				}
			}
		} else if( hasIgnoreCase(tokens, "who") && hasIgnoreCase(tokens, "is") && hasIgnoreCase(tokens, "at") || (hasIgnoreCase(tokens, "whos") || hasIgnoreCase(tokens, "who's")) && hasIgnoreCase(tokens, "at") ) {
			log.indent(2).log("Person query detected.");
			for( String location : variables.locations.keySet() ) {
				if( containsIgnoreCase(messageText, location) ) {
					log.indent(3).log("Location \"" + location + "\" detected.");
					if( !hasIgnoreCase(variables.locations.keySet(), location) ) {
						log.indent(3).log("Location not set.");
						log.indent(3).log("LocationResponder triggered.");
						response = location + " is not a set location.";
						return build();
					}
					ArrayList<String> locals = variables.locations.get(location);
					if( locals.size() == 0 ) {
						log.indent(3).log("Location \t" + location + "\" empty.");
						log.indent(3).log("LocationResponder triggered.");
						response = "No one is at " + location + ".";
					} else {
						log.indent(3).log("People detected at location.");
						response = "The following people are at " + location + ": ";
						for( int f=0; f<locals.size(); f++ ) {
							response += locals.get(f);
							if( f < locals.size()-1 ) {
								response += ", ";
							} else {
								response += ".";
							}
						}
						log.indent(3).log("LocaationResponder triggered.");
					}
					break;
				}
			}
		} else { 
			log.indent(2).log("LocationResponder unactivated.");
		}
		log.indent(1).log("LocationResponder processed.");
		return build();
	}

}
