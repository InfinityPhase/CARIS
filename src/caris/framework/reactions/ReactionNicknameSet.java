package caris.framework.reactions;

import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class ReactionNicknameSet extends Reaction {

	public IGuild guild;
	public IUser user;
	public String nick;
	
	public ReactionNicknameSet( IGuild guild, IUser user, String nick ) {
		this(guild, user, nick, 1, false);
	}
	
	public ReactionNicknameSet( IGuild guild, IUser user, String nick, int priority ) {
		this(guild, user, nick, priority, false);
	}
	
	public ReactionNicknameSet( IGuild guild, IUser user, String nick, boolean passive ) {
		this(guild, user, nick, 1, passive);
	}
	
	public ReactionNicknameSet( IGuild guild, IUser user, String nick, int priority, boolean passive ) {
		super(priority, passive);
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
