package tokens;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Reminder {
	public String message;
	public String author;
	public String channelID;
	
	public Reminder(String message, MessageReceivedEvent event) {
		this.message = message;
		this.author = event.getAuthor().mention();
		this.channelID = event.getChannel().getStringID();
	}
}
