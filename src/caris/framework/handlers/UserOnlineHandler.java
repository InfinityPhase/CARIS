package caris.framework.handlers;

import caris.framework.basehandlers.Handler;
import caris.framework.library.Constants;
import caris.framework.library.Variables;
import caris.framework.reactions.MultiReaction;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.Logger;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.user.PresenceUpdateEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.StatusType;

public class UserOnlineHandler extends Handler {

	public UserOnlineHandler() {
		super("UserOnline Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		if( !(event instanceof PresenceUpdateEvent) ) {
			return false;
		}
		PresenceUpdateEvent presenceUpdateEvent = (PresenceUpdateEvent) event;
		return presenceUpdateEvent.getNewPresence().getStatus().equals(StatusType.ONLINE) && presenceUpdateEvent.getOldPresence().getStatus().equals(StatusType.OFFLINE);
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.debug("PresenceUpdate detected", 2);
		PresenceUpdateEvent presenceUpdateEvent = (PresenceUpdateEvent) event;
		Logger.print(" User [" + presenceUpdateEvent.getUser().getName() + "#" + presenceUpdateEvent.getUser().getDiscriminator() + "]" + "(" + presenceUpdateEvent.getUser().getLongID() + ") has come online.");
		MultiReaction userOnline = new MultiReaction(-1);
		for( IGuild guild : Variables.guildIndex.keySet() ) {
			if( guild.getUsers().contains(presenceUpdateEvent.getUser()) ) {
				if( !Variables.guildIndex.get(guild).userIndex.get(presenceUpdateEvent.getUser()).mailbox.isEmpty() ) {
					userOnline.reactions.add(new ReactionMessage("Welcome back, " + presenceUpdateEvent.getUser().mention() + "! You have incoming mail!"
							+ "\nType `" + Constants.INVOCATION_PREFIX + " mailbox check` to read it!", guild.getDefaultChannel()));
				}
			}
		}
		Logger.print("Reaction produced from " + name, 1);
		return userOnline;
	}
	
}
