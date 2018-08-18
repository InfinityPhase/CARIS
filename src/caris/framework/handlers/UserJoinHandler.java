package caris.framework.handlers;

import caris.framework.basehandlers.Handler;
import caris.framework.library.Variables;
import caris.framework.reactions.MultiReaction;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionRoleAssign;
import caris.framework.reactions.ReactionUserJoin;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.obj.Role;

public class UserJoinHandler extends Handler {

	public UserJoinHandler() {
		super("UserJoin Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return event instanceof UserJoinEvent;
	}
	
	@Override
	protected Reaction process(Event event) {
		UserJoinEvent userJoinEvent = (UserJoinEvent) event;
		Logger.print("New user " + userJoinEvent.getUser().getLongID() + " joined (" + userJoinEvent.getGuild().getLongID() + ") <" + userJoinEvent.getGuild().getName() + ">", 0);
		MultiReaction welcome = new MultiReaction(-1);
		welcome.reactions.add(new ReactionUserJoin(userJoinEvent.getGuild(), userJoinEvent.getUser()));
		String addedRoles = "";
		for( Role role : Variables.guildIndex.get(userJoinEvent.getGuild()).autoRoles ) {
			welcome.reactions.add(new ReactionRoleAssign(userJoinEvent.getUser(), role));
			addedRoles += role.getName() + ", ";
		}
		if( !welcome.reactions.isEmpty() ) {
			addedRoles = addedRoles.substring(0, addedRoles.length()-2);
			welcome.reactions.add(new ReactionMessage(("Welcome, " + userJoinEvent.getUser().getName() + "!" +  
					"\nYou have been given the following roles: "+ addedRoles + "!"), userJoinEvent.getGuild().getDefaultChannel()));
		} else {
			Logger.debug("Reaction produced from " + name, 1, true);
			welcome.reactions.add(new ReactionMessage(("Welcome, " + userJoinEvent.getUser().getName() + "!"), userJoinEvent.getGuild().getDefaultChannel()));
		}
		return welcome;
	}
	
}
