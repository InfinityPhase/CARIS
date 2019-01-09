package caris.framework.events;

import java.util.ArrayList;
import java.util.Arrays;

import caris.framework.library.Constants;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;

public class MessageEventWrapper {

	public MessageReceivedEvent messageReceivedEvent;
	
	public String message;
	public ArrayList<String> tokens;
	public ArrayList<String> quotedTokens;
	public ArrayList<Integer> integerTokens;
	public ArrayList<Long> longTokens;
	
	public boolean adminAuthor;
	public boolean developerAuthor;
	public boolean elevatedAuthor;
	
	@SuppressWarnings("unlikely-arg-type")
	public MessageEventWrapper(MessageReceivedEvent messageReceivedEvent) {
		this.messageReceivedEvent = messageReceivedEvent;
		message = messageReceivedEvent.getMessage().getContent();
		tokens = TokenUtilities.parseTokens(message);
		quotedTokens = TokenUtilities.parseQuoted(message);
		integerTokens = TokenUtilities.parseIntegers(message);
		longTokens = TokenUtilities.parseLongs(message);	
		
		adminAuthor = messageReceivedEvent.getAuthor().getPermissionsForGuild(messageReceivedEvent.getGuild()).contains(Permissions.ADMINISTRATOR);
		developerAuthor = Arrays.asList(Constants.ADMIN_IDS).contains(messageReceivedEvent.getAuthor().getLongID());
		elevatedAuthor = adminAuthor || developerAuthor;
	}
	
	public ArrayList<String> getCapturedTokens(String boundary) {
		return TokenUtilities.parseCaptured(message, boundary);
	}
	
	public ArrayList<String> getCapturedTokens(String open, String close) {
		return TokenUtilities.parseCaptured(message, open, close);
	}
	
}
