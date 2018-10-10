package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class ReactionMailClear extends Reaction {
	
	public IGuild guild;
	public IUser user;
	
	public ReactionMailClear( IGuild guild, IUser user ) {
		this(guild, user, 1);
	}
	
	public ReactionMailClear( IGuild guild, IUser user, int priority ) {
		super(priority);
		this.guild = guild;
		this.user = user;
	}
	
	@Override
	public void run() {
		Variables.guildIndex.get(guild).userIndex.get(user).mailbox.clear();
	}
	
}
