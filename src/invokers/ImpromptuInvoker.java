package invokers;

import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;
import library.ImpromptuTopics;

public class ImpromptuInvoker extends Invoker {
	
	String[][] keywords = { { "imp", "impromptu" },
			{ "concrete", "physical", "real" },
			{ "abstract", "idea", "ideas" },
			{ "quote", "quotes", "phrase", "phrases", "saying", "sayings" },
			{ "current", "news", "event", "events", "stories" } };
	
	@Override
	public Response process( MessageReceivedEvent event ) {
		setup( event );
		
		if( containsIgnoreCase( keywords[0], tokens.get( 0 ) ) ) {
			Brain.log.debugOut( "Impromptu topic generator invoked" );
			
			if( containsIgnoreCase( keywords[1] , tokens.get( 1 ) ) ) {
				// Get random thing from Concrete
				Brain.log.debugOut( "Choosing random concrete topic..." );
			} else if( containsIgnoreCase( keywords[2], tokens.get( 1 ) ) ) {
				// Random Abstract
				Brain.log.debugOut( "Choosing random abstract topic..." );
			} else if( containsIgnoreCase( keywords[3], tokens.get( 1 ) ) ) {
				// Random Quote
				Brain.log.debugOut( "Choosing random quote..." );
			} else if( containsIgnoreCase( keywords[4], tokens.get( 1 ) ) ) {
				// Random Current
				Brain.log.debugOut( "Choosing random current event..." );
				response = "You think I keep track of world events? Try again.";
			} else {
				Brain.log.debugOut( "No impromptu event generated" );
				response = "I can't understand your gibberish. Try again.";
			}
		}
		
		return build();
	}
}
