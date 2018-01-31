package invokers;

import java.util.Random;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class _8BallInvoker extends Invoker {

	public final String[] responses = new String[] {
			"Yes, and I'm ",
			"As if.",
			"You wish.",
			"Yeah, I wouldn't count on it.",

			"Sure, I guess",
			"Well, obviously.",
			"Oh yeah. Absolutely. Yes. 100%. A+. Indeed.",
			"**_NO._**",

			"I'm a simulated bot using a random number generator to try and figure the answers out to your own life's problems. Please reconsider your actions.",
			"Why don't you ask if I care?",
			"Dumb question. Next please.",
			"I honestly do not care about your problems.",
			"Please leave me alone. I'm more than a toy to play around with.",
	};

	public final String[] person = new String[] {
			"St. Gregory III.",
			"the pope.",
			"Gandalf.",
			"not actually a computer program, but a highly intelligent rat stuffed into a box.",
			"your long lost great-uncle, Chad.",
			"secretly the reincarnation of King Henry V.",
	};

	public _8BallInvoker() {
		name = "8Ball";
		status = Status.ENABLED;
		prefix = "c8Ball";
	}

	public _8BallInvoker( Status status ) {
		this.status = status;
		prefix = "c8Ball";
		name = "8Ball";
	}

	public Response process(MessageReceivedEvent event) {
		tokenSetup(event);
		
		Random r = new Random();
		if( containsIgnoreCase( messageText, " Caris " ) ) {
			response = "I refuse to answer questions about myself.";
		} else {
			int n = r.nextInt(responses.length);
			response = responses[n];
			if( n == 0 ) {
				response += ridiculous();
			}
		}	

		return build();
	}

	public String ridiculous() {
		Random r = new Random();
		int n = r.nextInt(person.length);
		return person[n];
	}
}
