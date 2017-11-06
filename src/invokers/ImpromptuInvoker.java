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
				// Random Concrete
				Brain.log.debugOut( "Choosing random concrete topic..." );
				if( ImpromptuTopics.Concrete.size() != 0 ) {
					response = ImpromptuTopics.Concrete.get( rand.nextInt( ImpromptuTopics.Concrete.size() ) );
				} else {
					Brain.log.debugOut("Failed to find concrete topics");
					response = "What, you think I should tell you what is real?";
				}
			} else if( containsIgnoreCase( keywords[2], tokens.get( 1 ) ) ) {
				// Random Abstract
				Brain.log.debugOut( "Choosing random abstract topic..." );
				if( ImpromptuTopics.Abstract.size() != 0 ) {
					response = ImpromptuTopics.Abstract.get( rand.nextInt( ImpromptuTopics.Abstract.size() ) );
				} else {
					Brain.log.debugOut("Failed to find abstract topics");
					response = "As if I have time to deal with fake information.";
				}
			} else if( containsIgnoreCase( keywords[3], tokens.get( 1 ) ) ) {
				// Random Quote
				Brain.log.debugOut( "Choosing random quote..." );
				if( ImpromptuTopics.Quote.size() != 0 ) {
					response = ImpromptuTopics.Quote.get( rand.nextInt( ImpromptuTopics.Quote.size() ) );
				} else {
					Brain.log.debugOut("Failed to find quote topics");
					response = "How should I know what you strange beings say?";
				}
			} else if( containsIgnoreCase( keywords[4], tokens.get( 1 ) ) ) {
				// Random Current
				Brain.log.debugOut( "Choosing random current event..." );
				if( ImpromptuTopics.Current.size() != 0 ) {
					response = ImpromptuTopics.Current.get( rand.nextInt( ImpromptuTopics.Current.size() ) );
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
