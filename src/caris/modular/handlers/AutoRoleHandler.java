package caris.modular.handlers;

import java.util.ArrayList;
import java.util.List;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.library.Variables;
import caris.framework.reactions.MultiReaction;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionAutoRoleAdd;
import caris.framework.reactions.ReactionAutoRoleRemove;
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
		return isAdmin() && isInvoked();
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.debug("AutoRole detected", 2);
		String text = message;
		MultiReaction modifyAutoRoles = new MultiReaction(2);
		ArrayList<String> tokens = TokenUtilities.parseTokens(text);
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
					boolean modified = false;
					for( int f=3; f<tokens.size(); f++ ) {
						String token = tokens.get(f);
						List<IRole> roles = mrEvent.getGuild().getRolesByName(token);
						for( IRole role : roles ) {
							modifyAutoRoles.reactions.add(new ReactionAutoRoleAdd(mrEvent.getGuild(), role));
							modified = true;
						}
					}
					if( modified ) {
						Logger.debug("Reaction produced from " + name, 1, true);
						modifyAutoRoles.reactions.add(new ReactionMessage( "AutoRoles updated successfully!", mrEvent.getChannel()));
					} else {
						Logger.debug("Failed to find requested roles", 2);
						Logger.debug("Reaction produced from " + name, 1, true);
						modifyAutoRoles.reactions.add(new ReactionMessage( "Sorry, I couldn't find those roles. Did you capitalize them correctly?", mrEvent.getChannel()));
					}
				} else if( tokens.get(2).equals("remove") ) {
					boolean modified = false;
					for( int f=3; f<tokens.size(); f++ ) {
						String token = tokens.get(f);
						List<IRole> roles = mrEvent.getGuild().getRolesByName(token);
						if( !roles.isEmpty() ) {
							for( IRole role : roles ) {
								if( Variables.guildIndex.get(mrEvent.getGuild()).autoRoles.contains(role) ) {
									modifyAutoRoles.reactions.add(new ReactionAutoRoleRemove(mrEvent.getGuild(), role));
									modified = true;
								}
							}
						}
					}
					if( modified ) {
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
