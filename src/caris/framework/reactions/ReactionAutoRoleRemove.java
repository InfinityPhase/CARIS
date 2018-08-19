package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;

public class ReactionAutoRoleRemove extends Reaction {
	
	public IGuild guild;
	public IRole role;
	
	public ReactionAutoRoleRemove(IGuild guild, IRole role) {
		this(guild, role, 2);
	}
	
	public ReactionAutoRoleRemove(IGuild guild, IRole role, int priority) {
		super(priority);
		this.guild = guild;
		this.role = role;
	}
	
	@Override
	public void run() {
		Variables.guildIndex.get(guild).autoRoles.remove((Role) role);
		Logger.print("Removed role " + role + " from AutoRole list in Guild " + guild.getName(), 2 );
	}
	
}
