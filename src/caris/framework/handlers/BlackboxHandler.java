package caris.framework.handlers;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionBlackboxEnd;
import caris.framework.reactions.ReactionBlackboxStart;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.api.events.Event;

public class BlackboxHandler extends MessageHandler {

	public BlackboxHandler() {
		super("Blackbox Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return (mrEvent.getAuthor().getLongID() == Long.parseLong("249803963279343617") || isElevated()) && isMentioned() && (StringUtilities.containsAnyOfIgnoreCase(message, "blackbox", "black box"));
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.print("Blackbox detected!", 2);
		MultiReaction modifyBlackbox = new MultiReaction(2);
		if( StringUtilities.containsAnyOfIgnoreCase(message, "open", "create", "activate", "enable", "start", "commence", "establish", "set up", "turn on", "switch on", "initiate", " up") ) {
			if( Variables.guildIndex.get(mrEvent.getGuild()).channelIndex.get(mrEvent.getChannel()).blackboxActive ) {
				Logger.print("Failed to open blackbox because blackbox was already open.");
				modifyBlackbox.reactions.add(new ReactionMessage("A blackbox is already open!", mrEvent.getChannel()));
			} else {
				modifyBlackbox.reactions.add(new ReactionBlackboxStart(mrEvent.getChannel(), mrEvent.getMessageID()));
				modifyBlackbox.reactions.add(new ReactionMessage("Blackbox opened!", mrEvent.getChannel()));
			}
		} else if( StringUtilities.containsAnyOfIgnoreCase(message, "close", "finish", "complete", "end", "conclude", "stop", "terminate", "wrap up", "down") ) {
			if( !Variables.guildIndex.get(mrEvent.getGuild()).channelIndex.get(mrEvent.getChannel()).blackboxActive ) {
				Logger.print("Failed to close blackbox because no blackbox was open.");
				modifyBlackbox.reactions.add(new ReactionMessage("No blackbox is currently open!", mrEvent.getChannel()));
			} else {
				modifyBlackbox.reactions.add(new ReactionBlackboxEnd(mrEvent.getChannel(), ReactionBlackboxEnd.Operation.CLOSE));
				modifyBlackbox.reactions.add(new ReactionMessage("Blackbox closed!", mrEvent.getChannel()));
			}
		} else if( StringUtilities.containsAnyOfIgnoreCase(message, "cancel", "abort", "disable", "deactivate", "remove", "delete", "destroy", "trash", "do away with", "turn off", "switch off", "disestablish", "call off", "kill", "dismiss") ) {
			if( !Variables.guildIndex.get(mrEvent.getGuild()).channelIndex.get(mrEvent.getChannel()).blackboxActive ) {
				Logger.print("Failed to close blackbox because no blackbox was open.");
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
