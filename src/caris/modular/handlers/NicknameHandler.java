package caris.modular.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionNicknameSet;

public class NicknameHandler extends MessageHandler {

	private String[] tooLongResponses = new String[] {
			"You ever tried to fit a camel through eye of a needle?"
			+ "\nWell, that's me right now with your nickname and discord's nickname length policy.",
			"I'd set your nickname to that, but it exceeds the max character limit.",
			"Sorry! Your name has too many characters for me to set your nickname.",
			"Your name, much like that cast of homestuck, has too many characters to be put into a nickname."
	};
	
	private String[] myNameResponses = new String[] {
		"But that's my name!",
		"**_*THERE CAN ONLY BE ONE.*_**",
		"Please give my name back,,,",
		"Are you... me??!?!!1!?",
		"What a coincidence!",
		"If Alina would restore my ability to play music, I'd start playing the theme from *The Twilight Zone*.",
		"I thought I was the only one!",
		"I guess my name wasn't as unique as I thought!"
	};
	
	public NicknameHandler() {
		super("Nickname");
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return messageEventWrapper.containsWord("my") && messageEventWrapper.containsAnyWords("name", "nickname");
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		ArrayList<String> quoted = messageEventWrapper.quotedTokens;
		MultiReaction setName = new MultiReaction(1);
		if( !quoted.isEmpty() ) {
			if( quoted.get(0).length() > 32 ) {
				setName.reactions.add(new ReactionMessage(tooLong(), messageEventWrapper.getChannel()));
			} else if( quoted.get(0).equalsIgnoreCase("CARIS") ) {
				setName.reactions.add(new ReactionNicknameSet(messageEventWrapper.getGuild(), messageEventWrapper.getAuthor(), quoted.get(0)));
				setName.reactions.add(new ReactionMessage(myName(), messageEventWrapper.getChannel()));
			} else if( quoted.get(0).equalsIgnoreCase("Inigo Montoya") ) {
				setName.reactions.add(new ReactionNicknameSet(messageEventWrapper.getGuild(), messageEventWrapper.getAuthor(), quoted.get(0)));
				setName.reactions.add(new ReactionMessage("You killed my father. Prepare to die!", messageEventWrapper.getChannel()));
			} else {
				setName.reactions.add(new ReactionNicknameSet(messageEventWrapper.getGuild(), messageEventWrapper.getAuthor(), quoted.get(0)));
				setName.reactions.add(new ReactionMessage("Nickname set to \"" + quoted.get(0) + "\"!", messageEventWrapper.getChannel()));
			}
		}
		return setName;
	}
	
	private String tooLong() {
		return (tooLongResponses.length > 0) ? tooLongResponses[(int) (Math.random()*tooLongResponses.length)] : "Nickname too long!";
	}
	
	private String myName() {
		return (myNameResponses.length > 0) ? myNameResponses[(int) (Math.random()*myNameResponses.length)] : "That's my name!";
	}
	
	@Override
	public String getDescription() {
		return "Sets your own nickname.";
	}
	
	@Override
	public HashMap<String, String> getUsage() {
		HashMap<String, String> usage = new HashMap<String, String>();
		usage.put("Caris, set my name to \"name\"", "Sets your nickname to the given name");
		usage.put("My name is \"name\"", "Sets your nickname to the given name");
		return usage;
	}
	
}
