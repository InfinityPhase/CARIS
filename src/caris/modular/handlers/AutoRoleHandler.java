package caris.modular.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import caris.framework.basehandlers.InvokedHandler;
import caris.framework.library.Variables;
import caris.framework.reactions.MultiReaction;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionAutoRoleAdd;
import caris.framework.reactions.ReactionAutoRoleRemove;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.Logger;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.obj.IRole;

public class AutoRoleHandler extends InvokedHandler {
	
	public AutoRoleHandler() {
		super("AutoRole Handler");
		invocation = "autorole";
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		if( !isMessageReceivedEvent(event) ) {
			return false;
		}
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		return isAdmin(messageReceivedEvent) && isAdminInvoked(messageReceivedEvent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Reaction process(Event event) {
		Logger.debug("AutoRole detected", 2);
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		String text = messageReceivedEvent.getMessage().getContent();
		MultiReaction modifyAutoRoles = new MultiReaction(2);
		ArrayList<String> tokens = TokenUtilities.parseTokens(text);
		if( tokens.size() >= 3 ) {
			if( tokens.get(2).equals("get") ) {
				String autoRoles = "";
				List<Role> roles = Variables.guildIndex.get(messageReceivedEvent.getGuild()).autoRoles;
				for( Role role : roles ) {
					autoRoles += role.getName() + ", ";
				}
				if( !autoRoles.isEmpty() ) {
					autoRoles = autoRoles.substring(0, autoRoles.length()-2);
					Logger.debug("Reaction produced from " + name, 1, true);
					modifyAutoRoles.reactions.add(new ReactionMessage( "Here are the current default roles for this server: " + autoRoles, messageReceivedEvent.getChannel()));
				} else {
					Logger.debug("Reaction produced from " + name, 1, true);
					modifyAutoRoles.reactions.add(new ReactionMessage( "There aren't any default roles set for this server yet.", messageReceivedEvent.getChannel()));
				}
			}
			if( tokens.size() >= 4 ) {
				if( tokens.get(2).equals("add") ) {
					boolean modified = false;
					for( int f=3; f<tokens.size(); f++ ) {
						String token = tokens.get(f);
						List<IRole> roles = messageReceivedEvent.getGuild().getRolesByName(token);
						for( IRole role : roles ) {
							modifyAutoRoles.reactions.add(new ReactionAutoRoleAdd(messageReceivedEvent.getGuild(), role));
							modified = true;
						}
					}
					if( modified ) {
						Logger.debug("Reaction produced from " + name, 1, true);
						modifyAutoRoles.reactions.add(new ReactionMessage( "AutoRoles updated successfully!", messageReceivedEvent.getChannel()));
					} else {
						Logger.debug("Failed to find requested roles", 2);
						Logger.debug("Reaction produced from " + name, 1, true);
						modifyAutoRoles.reactions.add(new ReactionMessage( "Sorry, I couldn't find those roles. Did you capitalize them correctly?", messageReceivedEvent.getChannel()));
					}
				} else if( tokens.get(2).equals("remove") ) {
					boolean modified = false;
					for( int f=3; f<tokens.size(); f++ ) {
						String token = tokens.get(f);
						List<IRole> roles = messageReceivedEvent.getGuild().getRolesByName(token);
						if( !roles.isEmpty() ) {
							for( IRole role : roles ) {
								if( Variables.guildIndex.get(messageReceivedEvent.getGuild()).autoRoles.contains(role) ) {
									modifyAutoRoles.reactions.add(new ReactionAutoRoleRemove(messageReceivedEvent.getGuild(), role));
									modified = true;
								}
							}
						}
					}
					if( modified ) {
						Logger.debug("Reaction produced from " + name, 1, true);
						modifyAutoRoles.reactions.add(new ReactionMessage( "AutoRoles updated successfully!", messageReceivedEvent.getChannel()));
					} else {
						Logger.debug("Failed to find requested roles", 2);
						Logger.debug("Reaction produced from " + name, 1, true);
						modifyAutoRoles.reactions.add(new ReactionMessage( "Sorry, I couldn't find those roles. Did you capitalize them correctly?", messageReceivedEvent.getChannel()));
					}
				} else {
					Logger.debug("Operation failed due to syntax error", 2);
					Logger.debug("Reaction produced from " + name, 1, true);
					modifyAutoRoles.reactions.add(new ReactionMessage( "Syntax Error!", messageReceivedEvent.getChannel()));
				}
			} else {
				Logger.debug("Operation failed due to syntax error", 2);
				Logger.debug("Reaction produced from " + name, 1, true);
				modifyAutoRoles.reactions.add(new ReactionMessage( "Syntax Error!", messageReceivedEvent.getChannel()));
			}
		} else {
			Logger.debug("Operation failed due to syntax error", 2);
			Logger.debug("Reaction produced from " + name, 1, true);
			modifyAutoRoles.reactions.add(new ReactionMessage( "Syntax Error!", messageReceivedEvent.getChannel()));
		}
		return modifyAutoRoles;
	}
	
}
