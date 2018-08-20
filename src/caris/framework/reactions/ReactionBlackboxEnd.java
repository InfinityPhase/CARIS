package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;

public class ReactionBlackboxEnd extends Reaction {

	public enum Operation {
		CLOSE,
		CANCEL
	}
	
	public IChannel channel;
	public Operation operation;
	
	public ReactionBlackboxEnd(IChannel channel, Operation operation) {
		this(channel, operation, 2);
	}
	
	public ReactionBlackboxEnd(IChannel channel, Operation operation, int priority) {
		super(priority);
		this.channel = channel;
		this.operation = operation;
	}
	
	@Override
	public void run() {
		switch(operation) {
			case CLOSE:
				Long messageID = Variables.guildIndex.get(channel.getGuild()).channelIndex.get(channel).closeBlackbox();
				channel.getMessageHistoryTo(messageID).bulkDelete();
				channel.getMessageHistory(1).bulkDelete();
				Logger.print("Blackbox closed in (" + channel.getLongID() + ") <" + channel.getName() + ">", 2);
				break;
			case CANCEL:
				Variables.guildIndex.get(channel.getGuild()).channelIndex.get(channel).cancelBlackbox();
				Logger.print("Blackbox cancelled in (" + channel.getLongID() + ") <" + channel.getName() + ">", 2);
				break;
			default:
				Logger.error("Invalid Operation in ReactionBlackboxEnd");
		}
	}
	
}
