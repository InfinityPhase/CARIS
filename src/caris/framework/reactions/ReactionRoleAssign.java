package caris.framework.reactions;

import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class ReactionRoleAssign extends Reaction {

	public IUser user;
	public Role role;
	public IChannel channel;
	
	public ReactionRoleAssign( IUser user, Role role, IChannel channel ) {
		super(1);
		this.user = user;
		this.role = role;
		this.channel = channel;
	}
	
	public ReactionRoleAssign( IUser user, Role role, IChannel channel, int priority ) {
		super(priority);
		this.user = user;
		this.role = role;
		this.channel = channel;
	}
	
	public ReactionRoleAssign( IUser user, Role role, IChannel channel, boolean passive ) {
		super(passive);
		this.user = user;
		this.role = role;
		this.channel = channel;
	}
	
	public ReactionRoleAssign( IUser user, Role role, IChannel channel, int priority, boolean passive ) {
		super(priority, passive);
		this.user = user;
		this.role = role;
		this.channel = channel;
	}
	
	public void execute() {
		user.addRole(role);
	}
}
