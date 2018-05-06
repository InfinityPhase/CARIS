package modules.responders;

import java.util.ArrayList;

import library.Constants;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;
import utilities.Logger.level;

public class EfficentLocationResponder extends Responder {
	
	public EfficentLocationResponder() {
		this( Status.ENABLED );
	}
	
	public EfficentLocationResponder( Status status ) {
		this.status = status;
		name = "cL";
		prefix = "cL";
		help = "\n**__Efficent Location Responder__**"  +
				"\nBy saying your location in a special channel, CARIS will automagically set your location to the name specified."  +
				"\nThere is no formatting, the only argument should be the location of the user." +
				"\nCARIS will not respond when this is used, to maintain the efficency of the channel.\n" +
				"\n*\"Home\"*"  +
				"\n*\"School\"*";
	}
	
	@Override
	public Response process( MessageReceivedEvent event ) {
		tokenSetup( event );
		String person = event.getAuthor().getDisplayName( event.getGuild() ); // Need to find a better way of doing this...
		String location = removePunctuation( event.getMessage().getContent().toLowerCase() );
		
		// Yes, we use string based channel matching. This way, we can name any channel, and it will still work.
		if( event.getChannel().getName().equalsIgnoreCase( Constants.LOCATION_CHANNEL_NAME ) ) {
			log.log("Message detected in " + Constants.LOCATION_CHANNEL_NAME + " channel");
			log.log("Updating location for user " + event.getAuthor().getName() + " : " + event.getAuthor().getLongID());
			
			// Remove previous location for user
			log.indent(1).log("Removing previous locations...");
			for( String l : variables.locations.keySet() ) {
				if( variables.locations.get(l).contains( person ) ) {
					log.indent(2).level( level.INFO ).log("Location: " + l );
					variables.locations.get(l).remove( person );
				}
			}
			
			// Create location if it does not exist
			if( !variables.locations.containsKey( location ) ) {
				variables.locations.put( location, new ArrayList<String>() );
			}
			
			// Place the person in the given location
			variables.locations.get(location).add(person);
			
			log.indent(0).log("Location set to: " + removePunctuation( event.getMessage().getContent().toLowerCase() ) );
			
			
		}
		
		return build();
	}
	
	private String removePunctuation( String message ) {
		// Matches everything but letters, numbers, and whitespace
		// Unicode compatible.
		return message.replaceAll("[^\\p{L}\\p{N}\\p{Z}]", "");
	}

}
