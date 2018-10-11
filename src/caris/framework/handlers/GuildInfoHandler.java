package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.ChannelInfoBuilder;
import caris.framework.embedbuilders.GuildInfoBuilder;
import caris.framework.embedbuilders.UserInfoBuilder;
import caris.framework.main.Brain;
import caris.framework.reactions.ReactionEmbed;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.obj.IGuild;

public class GuildInfoHandler extends MessageHandler {

	public GuildInfoHandler() {
		super("GuildInfo", Access.DEVELOPER, false);
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return isMentioned() && StringUtilities.containsAnyOfIgnoreCase(message, "info", "data", "analysis", "stats", "statistics") && StringUtilities.containsAnyOfIgnoreCase(message, "guild", "channel", "user", "people");
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.print("GuildInfo detected", 2);
		ArrayList<Long> longs = TokenUtilities.parseLongs(message);
		if( StringUtilities.containsIgnoreCase(message, "channel") ) {
			if( !longs.isEmpty() ) {
				for( IGuild guild : Brain.cli.getGuilds() ) {
					if( guild.getLongID() == longs.get(0) ) {
						Logger.debug("Response produced from " + name, 1, true);
						return new ReactionEmbed(new ChannelInfoBuilder(Brain.cli.getGuildByID(guild.getLongID())).getEmbeds(), mrEvent.getChannel(), 2);
					}
				}
			}
			return new ReactionEmbed(new ChannelInfoBuilder(mrEvent.getGuild()).getEmbeds(), mrEvent.getChannel(), 2);
		} else if( StringUtilities.containsAnyOfIgnoreCase(message, "user", "people") ) {
			if( !longs.isEmpty() ) {
				for( IGuild guild : Brain.cli.getGuilds() ) {
					if( guild.getLongID() == longs.get(0) ) {
						Logger.debug("Response produced from " + name, 1, true);
						return new ReactionEmbed(new UserInfoBuilder(Brain.cli.getGuildByID(guild.getLongID())).getEmbeds(), mrEvent.getChannel(), 2);
					}
				}
			}
			Logger.debug("Response produced from " + name, 1, true);
			return new ReactionEmbed(new UserInfoBuilder(mrEvent.getGuild()).getEmbeds(), mrEvent.getChannel(), 2);
		} else if( StringUtilities.containsIgnoreCase(message, "guild") ) {
			Logger.debug("Response produced from " + name, 1, true);
			return new ReactionEmbed(new GuildInfoBuilder().getEmbeds(), mrEvent.getChannel(), 2);
		} else {
			return null;
		}
	}
}
