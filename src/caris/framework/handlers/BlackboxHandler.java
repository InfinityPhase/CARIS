package caris.framework.handlers;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Constants;
import caris.framework.library.Keywords;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionBlackboxEnd;
import caris.framework.reactions.ReactionBlackboxStart;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;

public class BlackboxHandler extends MessageHandler {

	public BlackboxHandler() {
		super("Blackbox", Access.ADMIN, false);
		description = "Opens a session within which all messages will be deleted once the session is closed.";
		usage.put( Constants.NAME + ", could you open a blackbox?", "Opens a blackbox session; all messages sent will be deleted once it is closed");
		usage.put( Constants.NAME + ", could you close the blackbox?", "Closes the active blackbox session; deletes all messages sent since the blackbox was opened");
		usage.put( Constants.NAME + ", cancel the blackbox", "Cancels the blackbox session");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return (mrEvent.getAuthor().getLongID() == Long.parseLong("249803963279343617") || isMentioned() && (StringUtilities.containsAnyOfIgnoreCase(message, "blackbox", "black box")));
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.print("Blackbox detected!", 2);
		MultiReaction modifyBlackbox = new MultiReaction(2);
		if( StringUtilities.containsAnyOfIgnoreCase(message, Keywords.POSITIVE) ) {
			if( Variables.guildIndex.get(mrEvent.getGuild()).channelIndex.get(mrEvent.getChannel()).blackboxActive ) {
				Logger.print("Failed to open blackbox because blackbox was already open.", 2);
				modifyBlackbox.reactions.add(new ReactionMessage("A blackbox is already open!", mrEvent.getChannel()));
			} else {
				modifyBlackbox.reactions.add(new ReactionBlackboxStart(mrEvent.getChannel(), mrEvent.getMessageID()));
				modifyBlackbox.reactions.add(new ReactionMessage("Blackbox opened!", mrEvent.getChannel()));
			}
		} else if( StringUtilities.containsAnyOfIgnoreCase(message, Keywords.END) ) {
			if( !Variables.guildIndex.get(mrEvent.getGuild()).channelIndex.get(mrEvent.getChannel()).blackboxActive ) {
				Logger.print("Failed to close blackbox because no blackbox was open.", 2);
				modifyBlackbox.reactions.add(new ReactionMessage("No blackbox is currently open!", mrEvent.getChannel()));
			} else {
				modifyBlackbox.reactions.add(new ReactionBlackboxEnd(mrEvent.getChannel(), ReactionBlackboxEnd.Operation.CLOSE));
				modifyBlackbox.reactions.add(new ReactionMessage("Blackbox closed!", mrEvent.getChannel()));
			}
		} else if( StringUtilities.containsAnyOfIgnoreCase(message, TokenUtilities.combineStringArrays(Keywords.DESTROY, Keywords.DISABLE, Keywords.CANCEL)) ) {
			if( !Variables.guildIndex.get(mrEvent.getGuild()).channelIndex.get(mrEvent.getChannel()).blackboxActive ) {
				Logger.print("Failed to close blackbox because no blackbox was open.", 2);
				modifyBlackbox.reactions.add(new ReactionMessage("No blackbox is currently open!", mrEvent.getChannel()));
			} else {
				modifyBlackbox.reactions.add(new ReactionBlackboxEnd(mrEvent.getChannel(), ReactionBlackboxEnd.Operation.CANCEL));
				modifyBlackbox.reactions.add(new ReactionMessage("Blackbox cancelled!", mrEvent.getChannel()));
			}
		}
		Logger.debug("Response produced from " + name, 1, true);
		return modifyBlackbox;
	}
	
}
