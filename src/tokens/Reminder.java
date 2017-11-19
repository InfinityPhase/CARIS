package tokens;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Reminder {
	public String message;
	public MessageReceivedEvent origin;
	
	public Reminder(String message, MessageReceivedEvent origin) {
		this.message = message;
		this.origin = origin;
	}
}
