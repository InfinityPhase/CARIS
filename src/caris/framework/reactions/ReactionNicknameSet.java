package caris.framework.reactions;

import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class ReactionNicknameSet extends Reaction {

	public IGuild guild;
	public IUser user;
	public String nick;
	
	public ReactionNicknameSet( IGuild guild, IUser user, String nick ) {
		this(guild, user, nick, 1);
	}
	
	public ReactionNicknameSet( IGuild guild, IUser user, String nick, int priority ) {
		super(priority);
		this.guild = guild;
		this.user = user;
		this.nick = nick;
	}
	
	@Override
	public void execute() {
		Logger.print(user.getName() + "'s nickname in Guild (" + guild.getLongID() + ") <" + guild.getName() + "> set to \"" + nick + "\"", 1);
		guild.setUserNickname(user, nick);
	}
}
