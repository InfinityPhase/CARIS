package invokers;

import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;
import library.ImpromptuTopics;

import java.util.Random;

public class ImpromptuInvoker extends Invoker {

	String[][] keywords = { { "imp", "impromptu" },
			{ "concrete", "physical", "real" },
			{ "abstract", "idea", "ideas" },
			{ "quote", "quotes", "phrase", "phrases", "saying", "sayings" },
			{ "current", "news", "event", "events", "stories" } };
	Random rand = new Random();

	@Override
	public Response process( MessageReceivedEvent event ) {
		setup( event );

		if( containsIgnoreCase( keywords[0], tokens.get( 0 ) ) ) {
			Brain.log.debugOut( "Impromptu topic generator invoked" );

			if( containsIgnoreCase( keywords[1] , tokens.get( 1 ) ) ) {
				// Get random thing from Concrete
				Brain.log.debugOut( "Choosing random concrete topic..." );
				if( ImpromptuTopics.Concrete.length != 0 ) {
				response = ImpromptuTopics.Concrete[ rand.nextInt( ImpromptuTopics.Concrete.length ) ];
				} else {
					Brain.log.debugOut("Failed to find concrete topics");
					response = "What, you think I should tell you what is real?";
				}
			} else if( containsIgnoreCase( keywords[2], tokens.get( 1 ) ) ) {
				// Random Abstract
				Brain.log.debugOut( "Choosing random abstract topic..." );
				if( ImpromptuTopics.Abstract.length != 0 ) {
					
				response = ImpromptuTopics.Abstract[ rand.nextInt( ImpromptuTopics.Abstract.length ) ];
				} else {
					Brain.log.debugOut("Failed to find abstract topics");
					response = "As if I have time to deal with fake information.";
				}
			} else if( containsIgnoreCase( keywords[3], tokens.get( 1 ) ) ) {
				// Random Quote
				Brain.log.debugOut( "Choosing random quote..." );
				if( ImpromptuTopics.Quote.length != 0 ) {
				response = ImpromptuTopics.Quote[ rand.nextInt( ImpromptuTopics.Quote.length ) ];
				} else {
					Brain.log.debugOut("Failed to find quote topics");
					response = "How should I know what you strange beings say?";
				}
			} else if( containsIgnoreCase( keywords[4], tokens.get( 1 ) ) ) {
				// Random Current
				Brain.log.debugOut( "Choosing random current event..." );
				if( ImpromptuTopics.Current.length != 0 ) {
					response = ImpromptuTopics.Current[ rand.nextInt( ImpromptuTopics.Current.length ) ];
				} else {
					Brain.log.debugOut("Failed to find current event topics");
					response = "You think I keep track of world events? Try again.";
				}
			} else {
				Brain.log.debugOut( "No impromptu event generated" );
				response = "I can't understand your gibberish. Try again.";
			}
		}

		return build();
	}
}
