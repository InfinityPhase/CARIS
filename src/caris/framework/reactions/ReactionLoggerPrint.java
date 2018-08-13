package caris.framework.reactions;

import caris.framework.utilities.Logger;

public class ReactionLoggerPrint extends Reaction {

	public String message;
	public int level;
	
	public ReactionLoggerPrint(String message, int level) {
		this(message, level, -1);
	}
	
	public ReactionLoggerPrint(String message, int level, int priority) {
		super(priority);
		this.message = message;
		this.level = level;
	}
	
	@Override
	public void execute() {
		Logger.print(message, level);
	}
	
}
