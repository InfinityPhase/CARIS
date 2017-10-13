package responders;

import java.util.ArrayList;

import library.Constants;
import main.Brain;
import utilities.Handler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class LocationResponder extends Handler {

	public LocationResponder() {}
	
	public String process(MessageReceivedEvent event) {
		if( Constants.DEBUG ) {System.out.println("\t\t\tProcessing LocationResponder.");}
		String response = "";
		String messageText = format(event);
		if( Constants.DEBUG ) {System.out.println("\t\t\tFormatted message: \"" + messageText + "\"");}
		ArrayList<String> tokens = Brain.tp.parse(event.getMessage().getContent().toLowerCase());
		
		if( tokens.contains("where's") || tokens.contains("where") ) {
			if( Constants.DEBUG ) {System.out.println("\t\t\t\tLocation query detected.");}
			for( String person : Brain.guildIndex.get(event.getChannel()).people.keySet() ) {
				if( messageText.contains(person) ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tName \"" + person + "\" detected.");}
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationResponder triggered.");}
					response = person + " is at " + Brain.guildIndex.get(event.getChannel()).people.get(person) + ".";
					break;
				}
			}
			for( String token : tokens ) {
				if( Brain.guildIndex.get(event.getChannel()).people.keySet().contains(token) ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation \"" + token + "\" detected.");}
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationResponder triggered.");}
					response = token + " is at " + Brain.guildIndex.get(event.getChannel()).people.get(token) + ".";
					break;
				}
			}
		} else if( tokens.contains("who") && tokens.contains("is") && tokens.contains("at") || tokens.contains("who's") && tokens.contains("at") ) {
			if( Constants.DEBUG ) {System.out.println("\t\t\t\tPerson query detected.");}
			for( String location : Brain.guildIndex.get(event.getChannel()).locations.keySet() ) {
				if( messageText.contains(location) ) {
					if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation \"" + location + "\" detected.");}
					if( !Brain.guildIndex.get(event.getChannel()).locations.containsKey(location) ) {
						if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocation not set.");}
						if( Constants.DEBUG ) {System.out.println("\t\t\t\t\tLocationResponder triggered.");}
						return location + " is not a set location.";
					}
					ArrayList<String> locals = Brain.guildIndex.get(event.getChannel()).locations.get(location);
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
		return response;
	}

}
