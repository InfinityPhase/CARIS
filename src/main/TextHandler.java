package main;

import java.util.ArrayList;
import java.util.Arrays;

import tokens.Response;
import utilities.Handler;

public class TextHandler {
	
	public TextHandler() {};
	
	public void process(String message) {
		ArrayList<Response> responses = new ArrayList<Response>();
		// Checks if a message begins with the bot command prefix
		if (message.startsWith(Constants.PREFIX)) { // if invoked
			for( Handler h : Brain.invokers ) { // try each invocation handler
				String text = h.process(message.substring(Constants.PREFIX.length())); // process individual invocation handler
				if( !text.equals("") ) { // if this produces a result
					responses.add(new Response(text, h.getPriority())); // add it to the list of potential responses
				}
			}
		} else { // if not being invoked
			for( Handler h : Brain.responders ) { // then try each auto handler
				String text = h.process(message); // process individual handler
				if( !text.equals("") ) { // if this produces a result
					responses.add(new Response(text, h.getPriority())); // add it to the list of potential responses
				}
			}
		}
		if( responses.size() != 0 ) { // if any response exists
			Response[] options = new Response[responses.size()]; // create a static array of response options
			for( int f=0; f<responses.size(); f++ ) {
				options[f] = (responses.get(f));
			}
			Arrays.sort(options); // sort these options
			System.out.println(options[0].text);
		}
	}
	
}
