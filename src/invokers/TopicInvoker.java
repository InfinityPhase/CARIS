package invokers;

import java.util.List;
import java.util.Random;

import library.ImpromptuTopics;
import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class TopicInvoker extends Invoker {

	String[][] keywords = { { "imp", "impromptu" },
			{ "concrete", "physical", "real" },
			{ "abstract", "idea", "ideas" },
			{ "quote", "quotes", "phrase", "phrases", "saying", "sayings" },
			{ "current", "news", "event", "events", "stories" } };
	
	String[][] commands = { { "clear", "remove" },
			{ "reset" },
			{ "add", "new", "create" } };
	
	String[] wordlists = { "concrete", "abstract", "quote", "current" };
	
	Random rand = new Random();

	@Override
	public Response process( MessageReceivedEvent event ) {
		setup( event );

		/* Gets things from the wordlist */
		Brain.log.debugOut( "Checking for impromptu topics...", 10);
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
			} else if( containsIgnoreCase( commands[0], tokens.get( 1 ) ) ) {
				// Clear wordlist
				if( !containsIgnoreCase( wordlists, tokens.get( 2 ) ) ) {
					// No matching list found
					Brain.log.debugOut("No matching topic list found");
					response = "I can't find that topic.";
				} else if( containsIgnoreCase( wordlists[0], tokens.get( 2 ) ) ) {
					// Concrete list
					ImpromptuTopics.Concrete.clear();
					response = "I cleared the list for the Concrete topic.";
				} else if( containsIgnoreCase( wordlists[1], tokens.get( 2 ) ) ) {
					// Abstract list
					ImpromptuTopics.Abstract.clear();
					response = "I cleared the list for the Abstract topic.";
				} else if( containsIgnoreCase( wordlists[2], tokens.get( 2 ) ) ) {
					// Quote list
					ImpromptuTopics.Quote.clear();
					response = "I cleared the list for the Quote topic.";
				} else if( containsIgnoreCase( wordlists[3], tokens.get( 2 ) ) ) {
					// Current list
					ImpromptuTopics.Current.clear();
					response = "I cleared the list for the Current topic.";
				}
				
			} else if( containsIgnoreCase( commands[1], tokens.get( 1 ) ) ) {
				// Reset wordlist
				if( !containsIgnoreCase( wordlists, tokens.get( 2 ) ) ) {
					// No matching list found
					Brain.log.debugOut("No matching topic list found");
					response = "I can't find that topic.";
				} else if( containsIgnoreCase( wordlists[0], tokens.get( 2 ) ) ) {
					// Concrete list
					resetList(ImpromptuTopics.Concrete, ImpromptuTopics.Concrete_File);
					response = "I reset the topic list for Concrete back to standard.";
				} else if( containsIgnoreCase( wordlists[1], tokens.get( 2 ) ) ) {
					// Abstract list
					resetList(ImpromptuTopics.Abstract, ImpromptuTopics.Abstract_File);
					response = "I reset the topic list for Abstract back to standard.";
				} else if( containsIgnoreCase( wordlists[2], tokens.get( 2 ) ) ) {
					// Quote list
					resetList(ImpromptuTopics.Quote, ImpromptuTopics.Quote_File);
					response = "I reset the topic list for Quote back to standard.";
				} else if( containsIgnoreCase( wordlists[3], tokens.get( 2 ) ) ) {
					// Current list
					resetList(ImpromptuTopics.Current, ImpromptuTopics.Current_File);
					response = "I reset the topic list for Current back to standard.";
				}
				
			} else if( containsIgnoreCase( commands[2], tokens.get( 1 ) ) ) {
				// Add value to wordlist
				if( !containsIgnoreCase( wordlists, tokens.get( 2 ) ) ) {
					// No matching list found
					Brain.log.debugOut("No matching topic list found");
					response = "I can't find that topic.";
				} else if( containsIgnoreCase( wordlists[0], tokens.get( 2 ) ) ) {
					// Concrete list
					ImpromptuTopics.Concrete.add( tokens.get( 3 ) );
					response = "I added the topic " + tokens.get( 3 ) + " to the list Concrete.";
				} else if( containsIgnoreCase( wordlists[1], tokens.get( 2 ) ) ) {
					// Abstract list
					ImpromptuTopics.Abstract.add( tokens.get( 3 ) );
					response = "I added the topic " + tokens.get( 3 ) + " to the list Abstract.";
				} else if( containsIgnoreCase( wordlists[2], tokens.get( 2 ) ) ) {
					// Quote list
					ImpromptuTopics.Quote.add( tokens.get( 3 ) );
					response = "I added the topic " + tokens.get( 3 ) + " to the list Quote.";
				} else if( containsIgnoreCase( wordlists[3], tokens.get( 2 ) ) ) {
					// Current list
					ImpromptuTopics.Current.add( tokens.get( 3 ) );
					response = "I added the topic " + tokens.get( 3 ) + " to the list Current.";
				} 
			}
			
			// AUGH. THE REPETITION OF IF STATEMENTS...
			
			else {
				Brain.log.debugOut( "No impromptu event generated" );
				response = "I can't understand your gibberish. Try again.";
			}
		}

		return build();
	}
	
	private void resetList( List<String> list, String file ) {
		list.clear();
		if( !Brain.files.readerExists( file ) ) {
			Brain.files.newReader(file, file);
		}
		String[] content = Brain.files.readAll( file );
		for( String l : content ) {
			list.add( l );
		}
	}
}
