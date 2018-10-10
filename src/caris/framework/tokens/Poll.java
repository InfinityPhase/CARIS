package caris.framework.tokens;

import java.util.ArrayList;
import java.util.Arrays;

import com.vdurmont.emoji.Emoji;

import caris.framework.library.GuildInfo;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class Poll {

	public int ID;
	public boolean yesNo;
	public boolean opened;
	public boolean closed;
	
	private GuildInfo guildInfo;
	public IUser author;
	private String description;
	private Option[] options;
	private IMessage message;
		
	public Poll( IUser author, String description, GuildInfo guildInfo ) {
		this.author = author;
		this.description = description;
		yesNo = true;
		this.options = new Option[2];
		options[0] = new Option(0, "Yes", guildInfo.emojiSet.affirmative[0]);
		options[1] = new Option(1, "No", guildInfo.emojiSet.negative[0]);
		this.guildInfo = guildInfo;
		this.ID = guildInfo.polls.size();
		this.opened = false;
		this.closed = false;
	}
	
	public Poll(IUser author, String description, ArrayList<String> options, GuildInfo guildInfo ) {
		this.author = author;
		this.description = description;
		yesNo = false;
		this.options = new Option[Math.min(options.size(), 11)];
		for( int f=0; f<this.options.length; f++ ) {
			this.options[f] = new Option(f, options.get(f), guildInfo.emojiSet.numbers[f]);
		}
		this.guildInfo = guildInfo;
		this.ID = guildInfo.polls.size();
		this.opened = false;
		this.closed = false;
	}
	
	public void setMessage(IMessage message) {
		this.message = message;
		guildInfo.polls.put(ID, this);
		this.opened = true;
		Logger.print("Poll " + ID + " opened", 4);
	}
	
	public EmbedBuilder getPollEmbed() {
		EmbedBuilder embed = new EmbedBuilder();
		embed.withAuthorName("Poll ID: " + ID);
		embed.withTitle("*" + description + "*");
		if( yesNo ) {
			embed.withDescription("Vote by reacting with :thumbsup: for yes, and :thumbsdown: for no!");
		} else {
			embed.withDescription("Vote by reacting with the number of the option you choose!");
			for( int f=0; f<options.length; f++ ) {
				embed.appendField("Option #" + f, options[f].name, false);
			}
		}
		embed.withFooterIcon(author.getAvatarURL());
		embed.withFooterText("Poll created by " + author.getDisplayName(guildInfo.guild));
		return embed;
	}
	
	public EmbedBuilder getResultEmbed() {
		if( !closed ) {
			return null;
		}
		EmbedBuilder embed = new EmbedBuilder();
		embed.withAuthorName("Winner: " + options[0].name);
		embed.withTitle("*" + description + "*");
		embed.withDescription("**__Results:__**");
		for( int f=0; f<options.length; f++ ) {
			embed.appendDescription( "\n**" + (f+1) + ". " + options[f].name + ": " + options[f].votes + "**");
		}
		embed.withFooterIcon(author.getAvatarURL());
		embed.withFooterText("Poll created by " + author.getDisplayName(guildInfo.guild));
		return embed;
	}
	
	public void close() {
		for( int f=0; f<options.length; f++ ) {
			for( IReaction ireaction : message.getReactions() ) {
				if( ireaction.getEmoji().getName().equals(ReactionEmoji.of(options[f].emoji.getUnicode()).getName()) ) {
					options[f].votes = ireaction.getUsers().size()-1;
				}
			}
		}
		Arrays.sort(options);
		message.removeAllReactions();
		closed = true;
		Logger.print("Poll " + ID + " closed", 4);
	}
	
	public Option[] getOptions() {
		return options;
	}
	
	public IMessage getMessage() {
		if( opened ) {
			return message;
		} else {
			return null;
		}
	}
	
	public class Option implements Comparable<Option> {
		public int id;
		public String name;
		public int votes;
		public Emoji emoji;
		
		public Option(int id, String name, Emoji emoji) {
			this.id = id;
			this.name = name;
			this.emoji = emoji;
			this.votes = 0;
		}

		@Override
		public int compareTo(Option o) {
			int compare = o.votes;
			if( this.votes < compare ) {
				return 1;
			} else if( this.votes > compare ) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}
