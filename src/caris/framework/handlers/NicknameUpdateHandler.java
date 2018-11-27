package caris.framework.handlers;

import caris.framework.basehandlers.Handler;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionNicknameSet;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.member.NicknameChangedEvent;

public class NicknameUpdateHandler extends Handler {

	public NicknameUpdateHandler() {
		super("NicknameUpdate", false);
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return event instanceof NicknameChangedEvent;
	}
	
	@Override
	protected Reaction process(Event event) {
		NicknameChangedEvent nicknameChangedEvent = (NicknameChangedEvent) event;
		String nicknameLock = Variables.guildIndex.get(nicknameChangedEvent.getGuild()).userIndex.get(nicknameChangedEvent.getUser()).nicknameLock;
		if( !nicknameLock.isEmpty() ) {
			Logger.debug("Reaction produced from " + name, 1, true);
			return new ReactionNicknameSet(nicknameChangedEvent.getGuild(), nicknameChangedEvent.getUser(), nicknameLock, -1);
		} else {
			return null;
		}
	}
	
}
