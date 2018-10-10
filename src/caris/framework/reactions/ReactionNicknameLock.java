package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class ReactionNicknameLock extends Reaction {
	
	public IGuild guild;
	public IUser user;
	public String nick;
	
	public ReactionNicknameLock( IGuild guild, IUser user, String nick ) {
		this(guild, user, nick, 1);
	}
	
	public ReactionNicknameLock( IGuild guild, IUser user, String nick, int priority ) {
		super(priority);
		this.guild = guild;
		this.user = user;
		this.nick = nick;
	}
	
	@Override
	public void run() {
		Variables.guildIndex.get(guild).userIndex.get(user).nicknameLock = nick;
		if( nick.isEmpty() ) {
			Logger.print(user.getName() + "'s nickname has been unlocked.", 2);
		} else {
			Logger.print(user.getName() + "'s nickname has been locked.", 2);
		}
	}
	
}
