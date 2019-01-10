package caris.framework.handlers;

import java.util.HashMap;
import java.util.List;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionAutoRole;
import caris.framework.reactions.ReactionMessage;
import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.obj.IRole;

public class AutoRoleHandler extends MessageHandler {
	
	public AutoRoleHandler() {
		super("AutoRole", Access.ADMIN);
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return invoked(messageEventWrapper);
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		MultiReaction modifyAutoRoles = new MultiReaction(2);
		if( messageEventWrapper.tokens.size() >= 2 ) {
			if( messageEventWrapper.tokens.get(1).equals("get") ) {
				String autoRoles = "";
				List<Role> roles = Variables.guildIndex.get(messageEventWrapper.getGuild()).autoRoles;
				for( Role role : roles ) {
					autoRoles += role.getName() + ", ";
				}
				if( !autoRoles.isEmpty() ) {
					autoRoles = autoRoles.substring(0, autoRoles.length()-2);
					modifyAutoRoles.reactions.add(new ReactionMessage( "Here are the current default roles for this server: " + autoRoles, messageEventWrapper.getChannel()));
				} else {
					modifyAutoRoles.reactions.add(new ReactionMessage( "There aren't any default roles set for this server yet.", messageEventWrapper.getChannel()));
				}
			} else if( messageEventWrapper.tokens.size() >= 3 ) {
				if( messageEventWrapper.tokens.get(1).equals("add") ) {
					for( IRole role : messageEventWrapper.getMessage().getRoleMentions() ) {
						modifyAutoRoles.reactions.add(new ReactionAutoRole(messageEventWrapper.getMessage().getGuild(), role, ReactionAutoRole.Operation.ADD));
					}
					if( !messageEventWrapper.getMessage().getRoleMentions().isEmpty() ) {
						modifyAutoRoles.reactions.add(new ReactionMessage( "AutoRoles updated successfully!", messageEventWrapper.getChannel()));
					} else {
						modifyAutoRoles.reactions.add(new ReactionMessage( "You need to mention the roles you want added!", messageEventWrapper.getChannel()));
					}
				} else if( messageEventWrapper.tokens.get(1).equals("remove") ) {
					if( !messageEventWrapper.getMessage().getRoleMentions().isEmpty() ) {
						for( IRole role : messageEventWrapper.getMessage().getRoleMentions() ) {
							if( Variables.guildIndex.get(messageEventWrapper.getGuild()).autoRoles.contains(role) ) {
								modifyAutoRoles.reactions.add(new ReactionAutoRole(messageEventWrapper.getGuild(), role, ReactionAutoRole.Operation.REMOVE));
							}
						}
						modifyAutoRoles.reactions.add(new ReactionMessage( "AutoRoles updated successfully!", messageEventWrapper.getChannel()));
					} else {
						modifyAutoRoles.reactions.add(new ReactionMessage( "You need to mention the roles you want removed!", messageEventWrapper.getChannel()));
					}
				} else {
					modifyAutoRoles.reactions.add(new ReactionMessage( "Syntax Error!", messageEventWrapper.getChannel()));
				}
			} else {
				modifyAutoRoles.reactions.add(new ReactionMessage( "Syntax Error!", messageEventWrapper.getChannel()));
			}
		} else {
			modifyAutoRoles.reactions.add(new ReactionMessage( "Syntax Error!", messageEventWrapper.getChannel()));
		}
		return modifyAutoRoles;
	}
	
	@Override
	public String getDescription() {
		 return "Automatically assigns roles to new users.";
	}
	
	@Override
	public HashMap<String, String> getUsage() {
		HashMap<String, String> usage = new HashMap<String, String>();
		usage.put(invocation + " add @Role1 @Role2 ... @RoleN", "Automatically assigns the given roles to each user who joins the server");
		usage.put(invocation + " remove @Role1 @Role2 ... @RoleN", "Removes the given roles from the list of roles to assign to new users");
		return usage;
	}
}
