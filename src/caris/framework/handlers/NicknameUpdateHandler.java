package caris.framework.handlers;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionNicknameSet;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.member.NicknameChangedEvent;

public class NicknameUpdateHandler extends GeneralHandler {

	public NicknameUpdateHandler() {
		super("NicknameUpdate");
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
			return new ReactionNicknameSet(nicknameChangedEvent.getGuild(), nicknameChangedEvent.getUser(), nicknameLock, -1);
		} else {
			return null;
		}
	}
	
	@Override
	public String getDescription() {
		return "Automatically changes back the nicknames of users who have it locked.";
	}
}
