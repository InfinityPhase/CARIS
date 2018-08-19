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
		super("GuildInfo Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return isAdmin() && isMentioned() && StringUtilities.containsAnyOfIgnoreCase(message, "info", "data", "analysis", "stats", "statistics") && StringUtilities.containsAnyOfIgnoreCase(message, "guild", "channel", "user", "people");
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.print("GuildInfo detected", 2);
		Logger.debug("Response produced from " + name, 1);
		if( StringUtilities.containsIgnoreCase(message, "channel") ) {
			ArrayList<String> tokens = TokenUtilities.parseTokens(message, new char[] {});
			for( IGuild guild : Brain.cli.getGuilds() ) {
				for( String token : tokens ) {
					if( guild.getLongID() + "" == token ) {
						return new ReactionEmbed(new ChannelInfoBuilder(Brain.cli.getGuildByID(guild.getLongID())).getEmbeds(), mrEvent.getChannel(), 2);
					}
				}
			}
			return new ReactionEmbed(new ChannelInfoBuilder(mrEvent.getGuild()).getEmbeds(), mrEvent.getChannel(), 2);
		} else if( StringUtilities.containsAnyOfIgnoreCase(message, "user", "people") ) {
			ArrayList<String> tokens = TokenUtilities.parseTokens(message, new char[] {});
			ArrayList<Long> numbers = new ArrayList<Long>();
			for( String token : tokens ) {
				try {
					numbers.add(Long.parseLong(token));
				} catch(NumberFormatException e) {
					
				}
			}
			for( IGuild guild : Brain.cli.getGuilds() ) {
				for( Long number : numbers ) {
					if( guild.getLongID() == number ) {
						return new ReactionEmbed(new UserInfoBuilder(Brain.cli.getGuildByID(number)).getEmbeds(), mrEvent.getChannel(), 2);
					}
				}
			}
			return new ReactionEmbed(new UserInfoBuilder(mrEvent.getGuild()).getEmbeds(), mrEvent.getChannel(), 2);
		} else if( StringUtilities.containsIgnoreCase(message, "guild") ) {
			return new ReactionEmbed(new GuildInfoBuilder().getEmbeds(), mrEvent.getChannel(), 2);
		} else {
			return null;
		}
	}
}
