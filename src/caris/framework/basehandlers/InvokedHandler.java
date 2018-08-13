package caris.framework.basehandlers;

import java.util.ArrayList;
import java.util.Arrays;

import caris.framework.library.Constants;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenParser;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;

public class InvokedHandler extends Handler {

	public String invocation = "";
	
	public InvokedHandler() {
		this("");
	}
	
	public InvokedHandler(String name) {
		super(name);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	protected boolean isAdmin(MessageReceivedEvent event) {
		return event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(Permissions.ADMINISTRATOR) || Arrays.asList(Constants.ADMIN_IDS).contains(event.getAuthor().getLongID());
	}
	
	protected boolean isInvoked(MessageReceivedEvent event) {
		ArrayList<String> tokens = TokenParser.parse(event.getMessage().getContent());
		if( tokens.size() >= 1 ) {
			return StringUtilities.equalsIgnoreCase(tokens.get(0), invocation);
		} else {
			return false;
		}
	}
	
	protected boolean isAdminInvoked(MessageReceivedEvent event) {
		ArrayList<String> tokens = TokenParser.parse(event.getMessage().getContent());
		if( tokens.size() >= 2 ) {
			return StringUtilities.equalsIgnoreCase(tokens.get(0), Constants.ADMIN_PREFIX) && StringUtilities.equalsIgnoreCase(tokens.get(1), invocation);
		} else {
			return false;
		}
	}
	
}
