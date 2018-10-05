package caris.framework.handlers;

import java.util.ArrayList;
import java.util.List;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionAutoRole;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.Logger;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.obj.IRole;

public class AutoRoleHandler extends MessageHandler {
	
	public AutoRoleHandler() {
		super("AutoRole Handler");
		keyword = "autorole";
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return isElevated() && isInvoked();
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.debug("AutoRole detected", 2);
		String text = message;
		MultiReaction modifyAutoRoles = new MultiReaction(2);
		ArrayList<String> tokens = TokenUtilities.parseTokens(text);
		ArrayList<IRole> roleMentions = (ArrayList<IRole>) mrEvent.getMessage().getRoleMentions();
		if( tokens.size() >= 3 ) {
			if( tokens.get(2).equals("get") ) {
				String autoRoles = "";
				List<Role> roles = Variables.guildIndex.get(mrEvent.getGuild()).autoRoles;
				for( Role role : roles ) {
					autoRoles += role.getName() + ", ";
				}
				if( !autoRoles.isEmpty() ) {
					autoRoles = autoRoles.substring(0, autoRoles.length()-2);
					Logger.debug("Reaction produced from " + name, 1, true);
					modifyAutoRoles.reactions.add(new ReactionMessage( "Here are the current default roles for this server: " + autoRoles, mrEvent.getChannel()));
				} else {
					Logger.debug("Reaction produced from " + name, 1, true);
					modifyAutoRoles.reactions.add(new ReactionMessage( "There aren't any default roles set for this server yet.", mrEvent.getChannel()));
				}
			} else if( tokens.size() >= 4 ) {
				if( tokens.get(2).equals("add") ) {
					for( IRole role : roleMentions ) {
						modifyAutoRoles.reactions.add(new ReactionAutoRole(mrEvent.getGuild(), role, ReactionAutoRole.Operation.ADD));
					}
					if( !roleMentions.isEmpty() ) {
						Logger.debug("Reaction produced from " + name, 1, true);
						modifyAutoRoles.reactions.add(new ReactionMessage( "AutoRoles updated successfully!", mrEvent.getChannel()));
					} else {
						Logger.debug("Failed to find requested roles", 2);
						Logger.debug("Reaction produced from " + name, 1, true);
						modifyAutoRoles.reactions.add(new ReactionMessage( "Sorry, I couldn't find those roles. Did you capitalize them correctly?", mrEvent.getChannel()));
					}
				} else if( tokens.get(2).equals("remove") ) {
					if( !roleMentions.isEmpty() ) {
						for( IRole role : roleMentions ) {
							if( Variables.guildIndex.get(mrEvent.getGuild()).autoRoles.contains(role) ) {
								modifyAutoRoles.reactions.add(new ReactionAutoRole(mrEvent.getGuild(), role, ReactionAutoRole.Operation.REMOVE));
							}
						}
						Logger.debug("Reaction produced from " + name, 1, true);
						modifyAutoRoles.reactions.add(new ReactionMessage( "AutoRoles updated successfully!", mrEvent.getChannel()));
					} else {
						Logger.debug("Failed to find requested roles", 2);
						Logger.debug("Reaction produced from " + name, 1, true);
						modifyAutoRoles.reactions.add(new ReactionMessage( "Sorry, I couldn't find those roles. Did you capitalize them correctly?", mrEvent.getChannel()));
					}
				} else {
					Logger.debug("Operation failed due to syntax error", 2);
					Logger.debug("Reaction produced from " + name, 1, true);
					modifyAutoRoles.reactions.add(new ReactionMessage( "Syntax Error!", mrEvent.getChannel()));
				}
			} else {
				Logger.debug("Operation failed due to syntax error", 2);
				Logger.debug("Reaction produced from " + name, 1, true);
				modifyAutoRoles.reactions.add(new ReactionMessage( "Syntax Error!", mrEvent.getChannel()));
			}
		} else {
			Logger.debug("Operation failed due to syntax error", 2);
			Logger.debug("Reaction produced from " + name, 1, true);
			modifyAutoRoles.reactions.add(new ReactionMessage( "Syntax Error!", mrEvent.getChannel()));
		}
		return modifyAutoRoles;
	}
	
}
