package caris.framework.reactions;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class ReactionNicknameSet extends Reaction {

	public IGuild guild;
	public IUser user;
	public String nick;
	
	public ReactionNicknameSet( IGuild guild, IUser user, String nick ) {
		super(1);
		this.guild = guild;
		this.user = user;
		this.nick = nick;
	}
	
	public ReactionNicknameSet( IGuild guild, IUser user, String nick, int priority ) {
		super(priority);
		this.guild = guild;
		this.user = user;
		this.nick = nick;
	}
	
	@Override
	public void execute() {
		guild.setUserNickname(user, nick);
	}
}
