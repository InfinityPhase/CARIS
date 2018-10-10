package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;

public class ReactionAutoRole extends Reaction {
	
	public enum Operation {
		ADD,
		REMOVE
	}
	
	public IGuild guild;
	public IRole role;
	
	public Operation operation;
	
	public ReactionAutoRole(IGuild guild, IRole role, Operation operation) {
		this(guild, role, operation, 2);
	}
	
	public ReactionAutoRole(IGuild guild, IRole role, Operation operation, int priority) {
		super(priority);
		this.guild = guild;
		this.role = role;
		this.operation = operation;
	}
	
	@Override
	public void run() {
		switch(operation) {
			case ADD:
				Variables.guildIndex.get(guild).autoRoles.add((Role) role);
				Logger.print("Added role " + role + " to AutoRole list in Guild " + guild.getName(), 2 );
				break;
			case REMOVE:
				Variables.guildIndex.get(guild).autoRoles.remove((Role) role);
				Logger.print("Removed role " + role + " from AutoRole list in Guild " + guild.getName(), 2 );
				break;
			default:
				Logger.error("Invalid Operation in ReactionAutoRole");
				break;
		}
	}
	
}
