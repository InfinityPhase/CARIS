package modules.tools;

import library.Variables;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.LineSet;
import tokens.Response;

public class RulesTool extends Tool {
	
	public RulesTool() {
		this( Status.ENABLED );
	}
	
	public RulesTool( Status status ) {
		this.status = status;
		name = "/rules";
		prefix = "/rules";
		help = "\n__/Rules__"  +
				"\nThis command allows you to modify the server rules." +
				"\nThe rules themselves don't do anything, but they'll be visible to all users, and will be shown whenever a new member joins.\n" +
				"\nUse ` /Rules: <function> ` as the *Main Command*" +
				"\nAnything you write under the *Main Command* in the same message will be the text to modify the rules with." +
				"\n\t` <function> `: the way to modify the server rules" +
				"\n\t\t` set `\t\t-\t\t*Sets the rules for the server.*"  +
				 "\n\t\t` append `\t\t-\t\t*Adds text to the rules.*"  +
				 "\n\t\t` remove `\t\t-\t\t*Removes the rules from the server.*"  +
				"\n```/Rules: set"  +
				"\n1. Don't swear."  +
				"\n2. Don't be mean."  +
				"\n3. Have fun!```\n";
	}
	
	@Override
	public Response process(MessageReceivedEvent event) {
		multilineSetup(event);
		
		if( primaryLineSet.tokens.size() == 1 ) {
			if( Variables.guildIndex.get(event.getGuild()).rules.isEmpty() ) {
				response = "There are no rules set for this server. Anarchy!";
			} else {
				response = "**__Server Rules__**\n" + Variables.guildIndex.get(event.getGuild()).rules;
			}
		} else {
			String function = primaryLineSet.tokens.get(1);
			if( function.equalsIgnoreCase("set") ) {
				Variables.guildIndex.get(event.getGuild()).rules = "";
				for( LineSet ls : auxiliaryLineSets ) {
					Variables.guildIndex.get(event.getGuild()).rules += ls.line + "\n";
				}
				response = "Server rules set.";
			} else if( function.equalsIgnoreCase("append") ) {
				for( LineSet ls : auxiliaryLineSets ) {
					Variables.guildIndex.get(event.getGuild()).rules += ls.line + "\n";
				}
				response = "Appended to server rules.";
			} else if( function.equalsIgnoreCase("remove") ) {
				Variables.guildIndex.get(event.getGuild()).rules = "";
				response = "Rules removed.";
			} else {
				log.indent(3).log("Invalid command.");
				response = "Invalid command.";
				return build();
			}
		}
		log.indent(1).log("RulesTool processed.");
		return build();
	}
}
