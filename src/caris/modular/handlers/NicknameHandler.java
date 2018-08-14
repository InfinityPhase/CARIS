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
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		ArrayList<String> quoted = TokenUtilities.parseQuoted(messageReceivedEvent.getMessage().getContent());
		MultiReaction setName = new MultiReaction();
		if( !quoted.isEmpty() ) {
			setName.reactions.add(new ReactionNicknameSet(messageReceivedEvent.getGuild(), messageReceivedEvent.getAuthor(), quoted.get(0)));
			setName.reactions.add(new ReactionMessage("Nickname set to \"" + quoted.get(0) + "\"!", messageReceivedEvent.getChannel()));
			Logger.print(messageReceivedEvent.getAuthor().getName() + "'s nickname set to \"" + quoted.get(0) + "\"", 2);
		} else {
			Logger.debug("Failed to set nickname because name was note quoted properly", 2);
		}
		return setName;
	}
	
}
