package caris.modular.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.Handler;
import caris.framework.reactions.MultiReaction;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionNicknameSet;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class NicknameHandler extends Handler {

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
		super("Nickname Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		if( !(event instanceof MessageReceivedEvent) ) {
			return false;
		}
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		ArrayList<String> tokens = TokenUtilities.parseTokens(messageReceivedEvent.getMessage().getContent());
		return StringUtilities.hasIgnoreCase( tokens, "my" ) && StringUtilities.hasIgnoreCase( tokens, "name" );
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.debug("Nickname request detected", 2);
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		ArrayList<String> quoted = TokenUtilities.parseQuoted(messageReceivedEvent.getMessage().getContent());
		MultiReaction setName = new MultiReaction(1);
		if( !quoted.isEmpty() ) {
			if( quoted.get(0).length() > 32 ) {
				setName.reactions.add(new ReactionMessage(tooLong(), messageReceivedEvent.getChannel()));
				Logger.debug("Failed to set nickname because name was too long", 2);
			} else if( quoted.get(0).equalsIgnoreCase("CARIS") ) {
				setName.reactions.add(new ReactionNicknameSet(messageReceivedEvent.getGuild(), messageReceivedEvent.getAuthor(), quoted.get(0)));
				setName.reactions.add(new ReactionMessage(myName(), messageReceivedEvent.getChannel()));
			} else if( quoted.get(0).equalsIgnoreCase("Inigo Montoya") ) {
				setName.reactions.add(new ReactionNicknameSet(messageReceivedEvent.getGuild(), messageReceivedEvent.getAuthor(), quoted.get(0)));
				setName.reactions.add(new ReactionMessage("You killed my father. Prepare to die!", messageReceivedEvent.getChannel()));
			} else {
				setName.reactions.add(new ReactionNicknameSet(messageReceivedEvent.getGuild(), messageReceivedEvent.getAuthor(), quoted.get(0)));
				setName.reactions.add(new ReactionMessage("Nickname set to \"" + quoted.get(0) + "\"!", messageReceivedEvent.getChannel()));
			}
		} else {
			Logger.debug("Failed to set nickname because name was not quoted properly", 2);
		}
		Logger.debug("Reaction produced from " + name, 1, true);
		return setName;
	}
	
	private String tooLong() {
		return (tooLongResponses.length > 0) ? tooLongResponses[(int) (Math.random()*tooLongResponses.length)] : "Nickname too long!";
	}
	
	private String myName() {
		return (myNameResponses.length > 0) ? myNameResponses[(int) (Math.random()*myNameResponses.length)] : "That's my name!";
	}
	
}
