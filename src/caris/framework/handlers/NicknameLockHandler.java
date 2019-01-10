package caris.framework.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.library.Constants;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionNicknameLock;
import caris.framework.reactions.ReactionNicknameSet;
import sx.blah.discord.handle.obj.IUser;

public class NicknameLockHandler extends MessageHandler {

	public NicknameLockHandler() {
		super("NicknameLock", Access.ADMIN);
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return mentioned(messageEventWrapper) && messageEventWrapper.containsAllWords("name", "lock");
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		ArrayList<String> quoted = messageEventWrapper.quotedTokens;
		ArrayList<IUser> mentions = (ArrayList<IUser>) messageEventWrapper.getMessage().getMentions();
		MultiReaction lockNickname = new MultiReaction(2);
		if( !mentions.isEmpty() ) {
			if( messageEventWrapper.containsAnyWords("unlock", "remove", "undo", "delete", "dismiss", "erase", "disperse") ) {
				for( IUser user : mentions ) {
					lockNickname.reactions.add(new ReactionNicknameLock(messageEventWrapper.getGuild(), user, ""));
				}
				lockNickname.reactions.add(new ReactionMessage("Nickname(s) unlocked!", messageEventWrapper.getChannel()));
			} else {
				if( !quoted.isEmpty() ) {
					String name = quoted.get(0);
					for( IUser user : mentions ) {
						lockNickname.reactions.add(new ReactionNicknameLock(messageEventWrapper.getGuild(), user, name));
						lockNickname.reactions.add(new ReactionNicknameSet(messageEventWrapper.getGuild(), user, name));
					}
					lockNickname.reactions.add(new ReactionMessage("Nickname(s) locked!", messageEventWrapper.getChannel()));
				} else {
					for( IUser user : mentions ) {
						lockNickname.reactions.add(new ReactionNicknameLock(messageEventWrapper.getGuild(), user, user.getDisplayName(messageEventWrapper.getGuild())));
						lockNickname.reactions.add(new ReactionNicknameSet(messageEventWrapper.getGuild(), user, user.getDisplayName(messageEventWrapper.getGuild())));
					}
					lockNickname.reactions.add(new ReactionMessage("Nickname(s) locked!", messageEventWrapper.getChannel()));
				}
			}
		} else {
			lockNickname.reactions.add(new ReactionMessage("You need to mention the users you want to lock/unlock.", messageEventWrapper.getChannel()));
		}
		return lockNickname;
	}
	
	@Override
	public String getDescription() {
		return "Locks people's nicknames.";
	}
	
	@Override
	public HashMap<String, String> getUsage() {
		HashMap<String, String> usage = new HashMap<String, String>();
		usage.put(Constants.NAME + ", lock @user's name", "Locks the nickname(s) for the user(s) specified to what it is currently");
		usage.put(Constants.NAME + ", lock @user's name to \"Nickname\"", "Locks the nickname(s) for the user(s) specified to the given name");
		usage.put(Constants.NAME + ", unlock @user's name", "Unlocks the nickname(s) for the user(s) specified");
		return usage;
	}
	
}
