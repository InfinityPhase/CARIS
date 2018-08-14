package caris.framework.basehandlers;

import java.util.ArrayList;
import java.util.Arrays;

import caris.framework.library.Constants;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;
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
	
	protected boolean isMessageReceivedEvent(Event event) {
		return event instanceof MessageReceivedEvent;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	protected boolean isAdmin(MessageReceivedEvent event) {
		Logger.debug("Checking if admin", 3);
		boolean check = event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(Permissions.ADMINISTRATOR) || Arrays.asList(Constants.ADMIN_IDS).contains(event.getAuthor().getLongID());
		if( check ) {
			Logger.debug("Admin confirmed", 4);
		} else {
			Logger.debug("Admin unconfirmed", 4);
		}
		return check;
	}
	
	protected boolean isInvoked(MessageReceivedEvent event) {
		Logger.debug("Checking invocation", 3);
		ArrayList<String> tokens = TokenUtilities.parseTokens(event.getMessage().getContent(), new char[] {});
		Logger.debug("Message tokens: " + tokens.toString(), 4);
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
		Logger.debug("Checking admin invocation", 3);
		ArrayList<String> tokens = TokenUtilities.parseTokens(event.getMessage().getContent(), new char[] {});
		Logger.debug("Message tokens: " + tokens.toString(), 4);
		boolean check = false;
		if( tokens.size() >= 2 ) {
			check = StringUtilities.equalsIgnoreCase(tokens.get(0), Constants.ADMIN_PREFIX) && StringUtilities.equalsIgnoreCase(tokens.get(1), invocation);
		}
		if( check ) {
			Logger.debug("Admin invoked", 4);
		} else {
			Logger.debug("Admin uninvoked", 4);
		}
		return check;
	}
	
}
