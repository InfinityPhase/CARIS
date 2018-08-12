package caris.framework.reactions;

import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.obj.IUser;

public class ReactionRoleAssign extends Reaction {

	public IUser user;
	public Role role;
	
	public ReactionRoleAssign( IUser user, Role role ) {
		this(user, role, 1, false);
	}
	
	public ReactionRoleAssign( IUser user, Role role, int priority ) {
		this(user, role, priority, false);
	}
	
	public ReactionRoleAssign( IUser user, Role role, boolean passive ) {
		this(user, role, 1, passive);
	}
	
	public ReactionRoleAssign( IUser user, Role role, int priority, boolean passive ) {
		super(priority, passive);
		this.user = user;
		this.role = role;
	}
	
	public void execute() {
		user.addRole(role);
	}
}
