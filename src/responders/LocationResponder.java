package responders;

import java.util.ArrayList;

import library.Constants;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class LocationResponder extends Responder {
	
	/* This is a magic ID. Used to ID when we can restore states */
	private static final long serialVersionUID = 699216121747600155L;

	@Override
	public Response process(MessageReceivedEvent event) {
		setup(event);
		
		if( containsIgnoreCase(tokens, "where's") || containsIgnoreCase(tokens, "where") || containsIgnoreCase(tokens, "wheres") ) {
			if( Constants.DEBUG ) {System.out.println("\t\t\t\tLocation query detected.");}
			for( String person : variables.people.keySet() ) {
				if( containsIgnoreCase(messageText, person) ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tName \"" + person + "\" detected.");}
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationResponder triggered.");}
					response = person + " is at " + variables.people.get(person) + ".";
					break;
				}
			}
		} else if( containsIgnoreCase(tokens, "who") && containsIgnoreCase(tokens, "is") && containsIgnoreCase(tokens, "at") || (containsIgnoreCase(tokens, "whos") || containsIgnoreCase(tokens, "who's")) && containsIgnoreCase(tokens, "at") ) {
			if( Constants.DEBUG ) {System.out.println("\t\t\t\tPerson query detected.");}
			for( String location : variables.locations.keySet() ) {
				if( containsIgnoreCase(messageText, location) ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation \"" + location + "\" detected.");}
					if( !containsIgnoreCase(variables.locations.keySet(), location) ) {
						if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation not set.");}
						if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationResponder triggered.");}
						response = location + " is not a set location.";
						return build();
					}
					ArrayList<String> locals = variables.locations.get(location);
					if( locals.size() == 0 ) {
						if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation \t" + location + "\" empty.");}
						if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationResponder triggered.");}
						response = "No one is at " + location + ".";
					} else {
						if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tPeople detected at location.");}
						response = "The following people are at " + location + ": ";
						for( int f=0; f<locals.size(); f++ ) {
							response += locals.get(f);
							if( f < locals.size()-1 ) {
								response += ", ";
							} else {
								response += ".";
							}
						}
						if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocaationResponder triggered.");}
					}
					break;
				}
			}
		} else if( Constants.DEBUG ) {System.out.println("\t\t\t\tLocationResponder unactivated.");}
		if( Constants.DEBUG ) {System.out.println("\t\t\tLocationResponder processed.");}
		return build();
	}

}
