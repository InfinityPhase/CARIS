package caris.modular.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import caris.framework.basehandlers.InvokedHandler;
import caris.framework.library.Variables;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.Logger;
import caris.framework.utilities.TokenParser;
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
		if( !(event instanceof MessageReceivedEvent) ) {
			return false;
		}
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		return isAdmin(messageReceivedEvent) && isAdminInvoked(messageReceivedEvent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Reaction process(Event event) {
		MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
		String text = messageReceivedEvent.getMessage().getContent();
		ArrayList<String> tokens = TokenParser.parse(text);
		if( tokens.size() >= 3 ) {
			if( tokens.get(2).equals("get") ) {
				String autoRoles = "";
				List<Role> roles = Variables.guildIndex.get(messageReceivedEvent.getGuild()).autoRoles;
				for( Role role : roles ) {
					autoRoles += role.getName() + ", ";
				}
				if( !autoRoles.isEmpty() ) {
					autoRoles = autoRoles.substring(0, autoRoles.length()-2);
					return new ReactionMessage( "Here are the current default roles for this server: " + autoRoles, messageReceivedEvent.getChannel() );
				} else {
					return new ReactionMessage( "There aren't any default roles set for this server yet.", messageReceivedEvent.getChannel() );
				}
			}
			if( tokens.size() >= 4 ) {
				if( tokens.get(2).equals("add") ) {
					String addedRoles = "";
					for( int f=3; f<tokens.size(); f++ ) {
						String token = tokens.get(f);
						List<IRole> roles = messageReceivedEvent.getGuild().getRolesByName(token);
						Variables.guildIndex.get(messageReceivedEvent.getGuild()).autoRoles.addAll((Collection<? extends Role>) roles);
						for( IRole role : roles ) {
							addedRoles += role.getName() + ", ";
						}
					}
					if( !addedRoles.isEmpty() ) {
						addedRoles = addedRoles.substring(0, addedRoles.length()-2);
						Logger.print("Added roles " + addedRoles + " to AutoRole list in Guild " + messageReceivedEvent.getGuild().getName(), 1 );
						return new ReactionMessage( "AutoRoles updated successfully!", messageReceivedEvent.getChannel() );
					} else {
						return new ReactionMessage( "Sorry, I couldn't find those roles. Did you capitalize them correctly?", messageReceivedEvent.getChannel() );
					}
				} else if( tokens.get(2).equals("remove") ) {
					String removedRoles = "";
					for( int f=3; f<tokens.size(); f++ ) {
						String token = tokens.get(f);
						List<IRole> roles = messageReceivedEvent.getGuild().getRolesByName(token);
						if( !roles.isEmpty() ) {
							for( IRole role : roles ) {
								if( Variables.guildIndex.get(messageReceivedEvent.getGuild()).autoRoles.contains(role) ) {
									Variables.guildIndex.get(messageReceivedEvent.getGuild()).autoRoles.remove(role);
									removedRoles += role.getName() + ", ";
								}
							}
						}
					}
					if( !removedRoles.isEmpty() ) {
						removedRoles = removedRoles.substring(0, removedRoles.length()-2);
						Logger.print("Removed roles " + removedRoles + " to AutoRole list in Guild " + messageReceivedEvent.getGuild().getName(), 1 );
						return new ReactionMessage( "AutoRoles updated successfully!", messageReceivedEvent.getChannel() );
					} else {
						return new ReactionMessage( "Sorry, I couldn't find those roles. Did you capitalize them correctly?", messageReceivedEvent.getChannel() );
					}
				} else {
					return new ReactionMessage( "Syntax Error!", messageReceivedEvent.getChannel() );
				}
			} else {
				return new ReactionMessage( "Syntax Error!", messageReceivedEvent.getChannel() );
			}
		} else {
			return new ReactionMessage( "Syntax Error!", messageReceivedEvent.getChannel());
		}
	}
	
}
