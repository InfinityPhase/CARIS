package invokers;


import java.util.ArrayList;

import library.Variables;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.LineSet;
import tokens.Response;
import utilities.Handler;

public class Invoker extends Handler {
	// Base Invoker class.

	/* For multiline */
	protected ArrayList<LineSet> lineSets;
	protected LineSet primaryLineSet;
	protected ArrayList<LineSet> auxiliaryLineSets;

	@Override
	protected void tokenSetup(MessageReceivedEvent event) {
		super.tokenSetup(event);
	}

	@Override
	protected void messageSetup(MessageReceivedEvent event) {
		super.messageSetup(event);
	}

	/* For Multiline */
	@Override
	public Response process(MessageReceivedEvent event) {
		if( containsIgnoreCase( "\n", event.getMessage().getContent() ) ) {
			multilineSetup(event);
			log.indent(20).log("THIS IS A MULTILINE");
		} else { // Is a single line invocation
			conditionalSetup(event);
			log.indent(20).log("THIS IS A SINGLELINE");
		}
		return build();
	}

	protected void multilineSetup(MessageReceivedEvent event) {
		response = "";
		message = "";
		embed = null;
		messageText = format(event);
		lineSets = multilineHandle(event);
		primaryLineSet = lineSets.get(0);
		auxiliaryLineSets = new ArrayList<LineSet>();
		for( int f=1; f<lineSets.size(); f++ ) {
			auxiliaryLineSets.add(lineSets.get(f));
		}
		tokens = tokenize(event);
		variables = Variables.guildIndex.get(event.getGuild());
	}

	protected ArrayList<LineSet> multilineHandle(MessageReceivedEvent event) {
		ArrayList<LineSet> tempLineSets = new ArrayList<LineSet>();
		ArrayList<String> tempLines = new ArrayList<String>();
		ArrayList<ArrayList<String>> tempTokenArray = new ArrayList<ArrayList<String>>();
		tempLines = multilineFormat(event);
		tempTokenArray = multilineTokenize(tempLines);
		for( int f=0; f<tempLines.size(); f++ ) {
			LineSet ls = new LineSet(tempLines.get(f), tempTokenArray.get(f));
			tempLineSets.add(ls);
		}
		return tempLineSets;
	}

	protected ArrayList<String> multilineFormat(MessageReceivedEvent event) {
		ArrayList<String> tempLines = new ArrayList<String>();
		String baseText = event.getMessage().getContent();
		while( baseText.contains("\n") ) {
			int index = baseText.indexOf("\n");
			if( index != -1 ) {
				String line = baseText.substring(0, index);
				baseText = baseText.substring(index+1);
				tempLines.add(line);
			}
		}
		tempLines.add(baseText);
		return tempLines;
	}

	protected ArrayList<ArrayList<String>> multilineTokenize(ArrayList<String> strings) {
		ArrayList<ArrayList<String>> tempTokenArray = new ArrayList<ArrayList<String>>();
		for( String s : strings ) {
			ArrayList<String> tempTokens = tokenize(s);
			tempTokenArray.add(tempTokens);
		}
		return tempTokenArray;
	}

}
