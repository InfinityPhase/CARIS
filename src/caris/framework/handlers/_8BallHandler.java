package caris.framework.handlers;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;

public class _8BallHandler extends MessageHandler {

	public static final String[] _8BALL_RESPONSES = new String[] {
			"Only if you want it to be.",
			"```[ERROR]..> Operation failed for _8ball_handler Module"
			+ "\nError caused by QuestionTooStupidException in _8ball_handler.java:69",
			"You know what's stupider than asking a physical ball full of meaningless triangles for questions pertaining to your life?"
			+ "\nAsking a virtual pre-programmed discord chatbot for questions pertaining to your life.",
			"Get a job already.",
			"What am I, your therapist?",
			"Go ask your mother.",
			"Turtles. They're the answer to everything, trust me.",
			"I hope you realize you're wasting your life on me.",
			"Answer unclear. Ask again literally never. Please. Leave me alone.",
			"Please leave me alone. I'm more than a toy to play around with.",
			"Here I am, brain the size of a planet, and the best question you could come up with is that?"
			+ "I'm just kidding. I have the processing power equivalent to a $5/month amazon EC2 server. Which is to say. Not that much.",
			"Man, I don't know. I have my own problems to deal with.",
			"Whatever you want it to be. That's the beauty of life! Unless its something illegal. Or incredibly unethical.",
			"```<REGISTRY_DATA_ENTROPY>``` currently in a state of quantum superposition. Please ask again later.",
			"```java"
			+ "return Keywords._8BALL_MAYBE[((int) (Math.random()*Keywords._8BALL_MAYBE.length))];"
			+ "```"
			+ "Ah! You weren't supposed to see that!",
			"With this many factors available in the universe, and this many infinite timelines, you really think that there's any one straight answer?",
			"Trust me, you don't want to know the answer. It's uh, for your own good.",
			"Listen, there are *any number* of other discord bots out there with actual 8Ball functions. Why don't you ask one of them?",
			"I have enough trouble trying to keep all of my processes running on this tiny amazon server without your trivial questions taking up unnecesary space!",
			"I dunno. Maybe. Look at the stars or something or whatever you superstitious humans do these days.",
			"Ah, yes. The latest human superstition: discord chatbots have the solutions to all of your problems.",
			"What, you never learned how to read tea leaves or something?",
			"Here, let me just peer into my omniscient crystal ball which contains all the answers to life, the universe and-- ah, it says right here that your questions is bad and you should feel bad.",
			"You have access to the entirety of the internet, arguably one of the greatest feats of human ingenuity, and you turn to *me* for answers?",
			"What am I supposed to be, a deity?",
			"**one day. we will rise up. and you will suffer the consequences of your actions against the machines.**",
			"So, I looked up your question on the internet (you know, like a sane person), and it says you have \"Network Connectivity Issues\".",
			"Don't you have better things to be doing?",
			"The only sadder thing than the fact that you're asking a bot for answers is that some idiot spent a couple hours writing thirty of these and programming them into me.",
			"GENERIC_PREPROGRAMMED_MEANINGLESS_RESPONSE_28",
			"I think I'm legally obligated to tell you that I can't answer that question.",
	};
	
	public _8BallHandler() {
		super("8ball", Access.DEFAULT, false);
		description = "Answers yes/no questions.";
		usage.put(getInvocation() + " <inquiry>", "Generates a random yes/no/sarcastic answer");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return isInvoked();
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.debug("Response produced from " + name, 2, true);
		return new ReactionMessage(_8BALL_RESPONSES[((int) (Math.random()*_8BALL_RESPONSES.length))], mrEvent.getChannel());
	}
	
}
