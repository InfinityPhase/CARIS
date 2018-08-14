package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.InvokedHandler;
import caris.framework.library.Variables;
import caris.framework.reactions.MultiReaction;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionMessage;
import caris.framework.reactions.ReactionNicknameSet;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class NicknameLockHandler extends InvokedHandler {

	public NicknameLockHandler() {
		super("NicknameLock Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		if( !(event instanceof MessageReceivedEvent) ) {
			return false;
		}
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		return isAdmin(messageReceivedEvent) && ((StringUtilities.containsIgnoreCase(messageReceivedEvent.getMessage().getContent(), "name"))) && StringUtilities.containsIgnoreCase(messageReceivedEvent.getMessage().getContent(), "lock");
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.debug("NicknameLock detected", 2);
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		ArrayList<String> tokens = TokenUtilities.parseTokens(messageReceivedEvent.getMessage().getContent());
		ArrayList<String> quoted = TokenUtilities.parseQuoted(messageReceivedEvent.getMessage().getContent());
		ArrayList<IUser> users = new ArrayList<IUser>();
		MultiReaction lockNickname = new MultiReaction(1);
		for( IUser user : messageReceivedEvent.getGuild().getUsers() ) {
			for( String token : tokens ) {
				if( StringUtilities.equalsIgnoreCase(user.mention(), token) ) {
					users.add(user);
					tokens.remove(token);
				}
			}
		}
		if( !users.isEmpty() ) {
			if( StringUtilities.containsIgnoreCase(messageReceivedEvent.getMessage().getContent(), "remove") || StringUtilities.containsIgnoreCase(messageReceivedEvent.getMessage().getContent(), "undo") || StringUtilities.containsIgnoreCase(messageReceivedEvent.getMessage().getContent(), "delete") || StringUtilities.containsIgnoreCase(messageReceivedEvent.getMessage().getContent(), "dismiss") || StringUtilities.containsIgnoreCase(messageReceivedEvent.getMessage().getContent(), "erase") || StringUtilities.containsIgnoreCase(messageReceivedEvent.getMessage().getContent(), "disperse") ) {
				for( IUser user : users ) {
					Variables.guildIndex.get(messageReceivedEvent.getGuild()).userIndex.get(user).nicknameLock = "";
					Logger.print(user.getName() + "'s nickname has been unlocked.", 2);
				}
				lockNickname.reactions.add(new ReactionMessage("Nicknames unlocked!", messageReceivedEvent.getChannel()));
			} else {
				if( !quoted.isEmpty() ) {
					String name = quoted.get(0);
					for( IUser user : users ) {
						Variables.guildIndex.get(messageReceivedEvent.getGuild()).userIndex.get(user).nicknameLock = name;
						lockNickname.reactions.add(new ReactionNicknameSet(messageReceivedEvent.getGuild(), user, name));
						Logger.print(user.getName() + "'s nickname has been locked.", 2);
					}
					lockNickname.reactions.add(new ReactionMessage("Nicknames locked!", messageReceivedEvent.getChannel()));
				} else {
					lockNickname.reactions.add(new ReactionMessage("You need to specify a nickname. Did you use quotes?", messageReceivedEvent.getChannel()));
					Logger.debug("Failed to lock nickname because no nickname was specified.", 2);
				}
			}
		} else {
			lockNickname.reactions.add(new ReactionMessage("You need to mention the users you want to lock/unlock.", messageReceivedEvent.getChannel()));
			Logger.debug("Failed to update nickname because no users were specified.", 2);
		}
		Logger.debug("Reaction produced from " + name, 1, true);
		return lockNickname;
	}
	
}
