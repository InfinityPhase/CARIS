package caris.framework.basehandlers;

import java.util.ArrayList;
import java.util.Arrays;

import caris.framework.library.Constants;
import caris.framework.utilities.Logger;
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
		Logger.debug("Checking if admin", 2);
		boolean check = event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(Permissions.ADMINISTRATOR) || Arrays.asList(Constants.ADMIN_IDS).contains(event.getAuthor().getLongID());
		if( check ) {
			Logger.debug("Admin confirmed", 2);
		} else {
			Logger.debug("Admin unconfirmed", 2);
		}
		return check;
	}
	
	protected boolean isInvoked(MessageReceivedEvent event) {
		Logger.debug("Checking invocation", 2);
		ArrayList<String> tokens = TokenParser.parse(event.getMessage().getContent());
		Logger.debug("Message tokens: " + tokens.toString(), 3);
		boolean check = false;
		if( tokens.size() >= 1 ) {
			check = StringUtilities.equalsIgnoreCase(tokens.get(0), invocation);
		}
		if( check ) {
			Logger.debug("Handler invoked", 2);
		} else {
			Logger.debug("Handler uninvoked", 2);
		}
		return check;
	}
	
	protected boolean isAdminInvoked(MessageReceivedEvent event) {
		Logger.debug("Checking admin invocation", 2);
		ArrayList<String> tokens = TokenParser.parse(event.getMessage().getContent());
		Logger.debug("Message tokens: " + tokens.toString(), 3);
		boolean check = false;
		if( tokens.size() >= 2 ) {
			check = StringUtilities.equalsIgnoreCase(tokens.get(0), Constants.ADMIN_PREFIX) && StringUtilities.equalsIgnoreCase(tokens.get(1), invocation);
		}
		if( check ) {
			Logger.debug("Admin invoked", 2);
		} else {
			Logger.debug("Admin uninvoked", 2);
		}
		return check;
	}
	
}
