package caris.modular.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.Handler;
import caris.framework.library.Variables;
import caris.framework.reactions.MultiReaction;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionRoleAssign;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.obj.Role;

public class UserJoinHandler extends Handler {

	public UserJoinHandler() {
		super("User Join Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return event instanceof UserJoinEvent;
	}
	
	@Override
	protected Reaction process(Event event) {
		UserJoinEvent userJoinEvent = (UserJoinEvent) event;
		ArrayList<Reaction> reactionQueue = new ArrayList<Reaction>();
		for( Role role : Variables.guildIndex.get(userJoinEvent.getGuild()).autoRoles ) {
			reactionQueue.add(new ReactionRoleAssign(userJoinEvent.getUser(), role));
		}
		return new MultiReaction(reactionQueue, true);
	}
	
}
