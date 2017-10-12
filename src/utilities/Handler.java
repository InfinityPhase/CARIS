package utilities;

import sx.blah.discord.handle.impl.obj.Message;

public interface Handler {
	// The base handler interface. Extend this into other classes.
	public String process(Message message);
	public String process(String message);
	public int getPriority();
}
