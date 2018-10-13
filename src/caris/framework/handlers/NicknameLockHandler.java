package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Constants;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionNicknameLock;
import caris.framework.reactions.ReactionNicknameSet;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.obj.IUser;

public class NicknameLockHandler extends MessageHandler {

	public NicknameLockHandler() {
		super("NicknameLock", Access.ADMIN, false);
		description = "Locks people's nicknames.";
		usage.put(Constants.NAME + ", lock @user's name", "Locks the nickname(s) for the user(s) specified to what it is currently");
		usage.put(Constants.NAME + ", lock @user's name to \"Nickname\"", "Locks the nickname(s) for the user(s) specified to the given name");
		usage.put(Constants.NAME + ", unlock @user's name", "Unlocks the nickname(s) for the user(s) specified");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return isMentioned() && StringUtilities.containsAllOfIgnoreCase(message, "name", "lock");
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.debug("NicknameLock detected", 2);
		ArrayList<String> quoted = TokenUtilities.parseQuoted(message);
		ArrayList<IUser> mentions = (ArrayList<IUser>) mrEvent.getMessage().getMentions(); 
		MultiReaction lockNickname = new MultiReaction(2);
		if( !mentions.isEmpty() ) {
			if( StringUtilities.containsAnyOfIgnoreCase(message, "unlock", "remove", "undo", "delete", "dismiss", "erase", "disperse") ) {
				for( IUser user : mentions ) {
					lockNickname.reactions.add(new ReactionNicknameLock(mrEvent.getGuild(), user, ""));
				}
				lockNickname.reactions.add(new ReactionMessage("Nickname(s) unlocked!", mrEvent.getChannel()));
			} else {
				if( !quoted.isEmpty() ) {
					String name = quoted.get(0);
					for( IUser user : mentions ) {
						lockNickname.reactions.add(new ReactionNicknameLock(mrEvent.getGuild(), user, name));
						lockNickname.reactions.add(new ReactionNicknameSet(mrEvent.getGuild(), user, name));
					}
					lockNickname.reactions.add(new ReactionMessage("Nickname(s) locked!", mrEvent.getChannel()));
				} else {
					for( IUser user : mentions ) {
						lockNickname.reactions.add(new ReactionNicknameLock(mrEvent.getGuild(), user, user.getDisplayName(mrEvent.getGuild())));
						lockNickname.reactions.add(new ReactionNicknameSet(mrEvent.getGuild(), user, user.getDisplayName(mrEvent.getGuild())));
					}
					lockNickname.reactions.add(new ReactionMessage("Nickname(s) locked!", mrEvent.getChannel()));
				}
			}
		} else {
			lockNickname.reactions.add(new ReactionMessage("You need to mention the users you want to lock/unlock.", mrEvent.getChannel()));
			Logger.debug("Failed to update nickname because no users were specified.", 2);
		}
		Logger.debug("Reaction produced from " + name, 1, true);
		return lockNickname;
	}
	
}
