package invokers;

import java.util.Random;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class _8BallInvoker extends Invoker {
	
	/* This is a magic ID. Used to ID when we can restore states */
	private static final long serialVersionUID = -2176783238658735415L;
	
	public final String[] responses = new String[] {
		"Sure, I guess",
		"What?? No!! What are you thinking?!",
		"Hmm... Maybe.",
		"Yeah, don't count on it.",
		"Oh yeah. Absolutely. Yes. 100%. A+. Indeed.",
		"I'm a simulated bot using a random number generator to try and figure the answers out to your own life's problems. Please reconsider your actions.",
		"Affirmative. Just kidding, haha, not in a million years.",
		"What about... oh! I know: NO.",
		"I honestly do not care about your problems.",
		"Please leave me alone. I'm more than a toy to play around with.",
	};
	
	public Response process(MessageReceivedEvent event) {
		setup(event);
		Random r = new Random();
		if( tokens.get(0).equals("8ball") ) {
			if( containsIgnoreCase( messageText, " Caris " ) ) {
				response = "I refuse to answer questions about myself.";
			} else {
				int n = r.nextInt(10);
				response = responses[n];
			}	
		}
		return build();
	}
}
