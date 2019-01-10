package caris.framework.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.ChannelInfoBuilder;
import caris.framework.embedbuilders.GuildInfoBuilder;
import caris.framework.embedbuilders.UserInfoBuilder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.library.Constants;
import caris.framework.main.Brain;
import caris.framework.reactions.ReactionEmbed;
import sx.blah.discord.handle.obj.IGuild;

public class GuildInfoHandler extends MessageHandler {

	public GuildInfoHandler() {
		super("GuildInfo", Access.DEVELOPER);
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return mentioned(messageEventWrapper) && messageEventWrapper.containsAnyWords("info", "data", "analysis", "stats", "statistics") && messageEventWrapper.containsAnyWords("guild", "channel", "user", "people");
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		ArrayList<Long> longs = messageEventWrapper.longTokens;
		if( messageEventWrapper.containsWord("channel") ) {
			if( !longs.isEmpty() ) {
				for( IGuild guild : Brain.cli.getGuilds() ) {
					if( guild.getLongID() == longs.get(0) ) {
						return new ReactionEmbed(new ChannelInfoBuilder(Brain.cli.getGuildByID(guild.getLongID())).getEmbeds(), messageEventWrapper.getChannel(), 2);
					}
				}
			}
			return new ReactionEmbed(new ChannelInfoBuilder(messageEventWrapper.getGuild()).getEmbeds(), messageEventWrapper.getChannel(), 2);
		} else if( messageEventWrapper.containsAnyWords("user", "people") ) {
			if( !longs.isEmpty() ) {
				for( IGuild guild : Brain.cli.getGuilds() ) {
					if( guild.getLongID() == longs.get(0) ) {
						return new ReactionEmbed(new UserInfoBuilder(Brain.cli.getGuildByID(guild.getLongID())).getEmbeds(), messageEventWrapper.getChannel(), 2);
					}
				}
			}
			return new ReactionEmbed(new UserInfoBuilder(messageEventWrapper.getGuild()).getEmbeds(), messageEventWrapper.getChannel(), 2);
		} else if( messageEventWrapper.containsWord("guild") ) {
			return new ReactionEmbed(new GuildInfoBuilder().getEmbeds(), messageEventWrapper.getChannel(), 2);
		} else {
			return null;
		}
	}

	@Override
	public String getDescription() {
		return "Displays information on Guilds/Channels.";
	}
	
	@Override
	public HashMap<String, String> getUsage() {
		HashMap<String, String> usage = new HashMap<String, String>();
		usage.put(Constants.NAME + ", can I get the guild data?", "Displays a list of all the guilds Caris is on, and their IDs");
		usage.put(Constants.NAME + ", can I get channel data for <Guild ID>?", "Displays a list of all the channels and their IDs from a guild that Caris is on");
		usage.put(Constants.NAME + ", can I get user data for <Channel ID>?", "Displays a list of all the users and their IDs from a guild that Caris is on");
		return usage;
	}
}
