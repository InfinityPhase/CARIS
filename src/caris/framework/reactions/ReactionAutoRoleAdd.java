package caris.framework.reactions;

import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;

public class ReactionAutoRoleAdd extends Reaction {
	
	public IGuild guild;
	public IRole role;
	
	public ReactionAutoRoleAdd(IGuild guild, IRole role) {
		this(guild, role, 2);
	}
	
	public ReactionAutoRoleAdd(IGuild guild, IRole role, int priority) {
		super(priority);
		this.guild = guild;
		this.role = role;
	}
	
	@Override
	public void execute() {
		Variables.guildIndex.get(guild).autoRoles.add((Role) role);
		Logger.print("Added role " + role + " to AutoRole list in Guild " + guild.getName(), 2 );
	}
	
}
