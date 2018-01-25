package tokens;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class Reminder {
	public String message;
	public String author;
	public String channelID;
	
	// TODO: Use a snowflake id? UUID? AtomicInteger?
	// Time, Worker, Sequence
	public String reminderID; // For the purposes of the database, allows duplicate reminders to not be all removed if one finishes.
	
	public Reminder(String message, MessageReceivedEvent event) {
		this.message = message;
		this.author = event.getAuthor().mention();
		this.channelID = event.getChannel().getStringID();
	}
	
	public Reminder(String message, MessageReceivedEvent event, String reminderID ) {
		this.message = message;
		this.author = event.getAuthor().mention();
		this.channelID = event.getChannel().getStringID();
		this.reminderID = reminderID;
	}
	
	public Reminder( String message, IUser author, IChannel channel ) {
		this.message = message;
		this.author = author.getName();
		this.channelID = channel.getStringID();
	}
	
	public Reminder( String message, IUser author, IChannel channel, String reminderID ) {
		this.message = message;
		this.author = author.getName();
		this.channelID = channel.getStringID();
		this.reminderID = reminderID;
	}
	
	public Reminder( String message, String author, String channelID ) {
		this.message = message;
		this.author = author;
		this.channelID = channelID;
	}
	
	public Reminder( String message, String author, String channelID, String reminderID ) {
		this.message = message;
		this.author = author;
		this.channelID = channelID;
		this.reminderID = reminderID;
	}
}
