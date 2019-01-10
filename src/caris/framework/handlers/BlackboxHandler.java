package caris.framework.handlers;

import java.util.HashMap;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.events.MessageEventWrapper;
import caris.framework.library.Constants;
import caris.framework.library.Keywords;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionBlackboxEnd;
import caris.framework.reactions.ReactionBlackboxStart;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.TokenUtilities;

public class BlackboxHandler extends MessageHandler {

	public BlackboxHandler() {
		super("Blackbox", Access.ADMIN);
	}
	
	@Override
	public boolean accessGranted(MessageEventWrapper messageEventWrapper) {
		return super.accessGranted(messageEventWrapper) || messageEventWrapper.getAuthor().getLongID() == Long.parseLong("249803963279343617");
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return mentioned(messageEventWrapper) && (messageEventWrapper.containsAnyWords("black box", "blackbox"));
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		MultiReaction modifyBlackbox = new MultiReaction(2);
		if( messageEventWrapper.containsAnyWords(Keywords.POSITIVE) ) {
			if( Variables.guildIndex.get(messageEventWrapper.getGuild()).channelIndex.get(messageEventWrapper.getChannel()).blackboxActive ) {
				modifyBlackbox.reactions.add(new ReactionMessage("A blackbox is already open!", messageEventWrapper.getChannel()));
			} else {
				modifyBlackbox.reactions.add(new ReactionBlackboxStart(messageEventWrapper.getChannel(), messageEventWrapper.getMessageID()));
				modifyBlackbox.reactions.add(new ReactionMessage("Blackbox opened!", messageEventWrapper.getChannel()));
			}
		} else if( messageEventWrapper.containsAnyWords(Keywords.END) ) {
			if( !Variables.guildIndex.get(messageEventWrapper.getGuild()).channelIndex.get(messageEventWrapper.getChannel()).blackboxActive ) {
				modifyBlackbox.reactions.add(new ReactionMessage("No blackbox is currently open!", messageEventWrapper.getChannel()));
			} else {
				modifyBlackbox.reactions.add(new ReactionBlackboxEnd(messageEventWrapper.getChannel(), ReactionBlackboxEnd.Operation.CLOSE));
				modifyBlackbox.reactions.add(new ReactionMessage("Blackbox closed!", messageEventWrapper.getChannel()));
			}
		} else if( messageEventWrapper.containsAnyWords(TokenUtilities.combineStringArrays(Keywords.DESTROY, Keywords.DISABLE, Keywords.CANCEL)) ) {
			if( !Variables.guildIndex.get(messageEventWrapper.getGuild()).channelIndex.get(messageEventWrapper.getChannel()).blackboxActive ) {
				modifyBlackbox.reactions.add(new ReactionMessage("No blackbox is currently open!", messageEventWrapper.getChannel()));
			} else {
				modifyBlackbox.reactions.add(new ReactionBlackboxEnd(messageEventWrapper.getChannel(), ReactionBlackboxEnd.Operation.CANCEL));
				modifyBlackbox.reactions.add(new ReactionMessage("Blackbox cancelled!", messageEventWrapper.getChannel()));
			}
		}
		return modifyBlackbox;
	}
	
	@Override
	public String getDescription() {
		return "Opens a session within which all messages will be deleted once the session is closed.";
	}
	
	@Override
	public HashMap<String, String> getUsage() {
		HashMap<String, String> usage = new HashMap<String, String>();
		usage.put( Constants.NAME + ", could you open a blackbox?", "Opens a blackbox session; all messages sent will be deleted once it is closed");
		usage.put( Constants.NAME + ", could you close the blackbox?", "Closes the active blackbox session; deletes all messages sent since the blackbox was opened");
		usage.put( Constants.NAME + ", cancel the blackbox", "Cancels the blackbox session");
		return usage;
	}
	
}
