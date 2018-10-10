package caris.modular.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.Builder;
import caris.framework.reactions.ReactionEmbed;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class TitleCardHandler extends MessageHandler {

	public TitleCardHandler() {
		super("Title Card Handler");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return message.startsWith(">im ");
	}
	
	@Override
	protected Reaction process(Event event) {
		Logger.debug("TitleCard detected", 2);
		ArrayList<String> tokens = TokenUtilities.parseTokens(message, new char[] {});
		IUser subject = null;
		for( IUser user : mrEvent.getGuild().getUsers() ) {
			if( StringUtilities.equalsAnyOfIgnoreCase(tokens.get(1), user.mention(true), user.mention(false)) ) {
				subject = user;
			}
		}
		if( subject == null ) {
			Logger.debug("Operation failed because no user specified", 2);
			return new ReactionMessage( "**" + mrEvent.getAuthor().getDisplayName(mrEvent.getGuild()) + "**, no one answered to the call and no work found.", mrEvent.getChannel() );
		} else {
			Logger.debug("Response produced from " + name, 1, true);
			return new ReactionEmbed((new TitleCardBuilder(subject, mrEvent.getGuild())).getEmbeds(), mrEvent.getChannel());
		}
	}
	
	public class TitleCardBuilder extends Builder {
		
		public IUser user;
		public IGuild guild;
		
		public TitleCardBuilder(IUser user, IGuild guild) {
			super();
			this.user = user;
			this.guild = guild;
		}
		
		@Override
		protected void buildEmbeds() {
			embeds.add(new EmbedBuilder());
			embeds.get(0).withColor(255, 157, 44);
			embeds.get(0).withTitle(user.getDisplayName(guild));
			embeds.get(0).withDescription(guild.getName());
			embeds.get(0).withImage(user.getAvatarURL());
			if( user.getLongID() == Long.parseLong("246562987651891200") && guild.getLongID() == Long.parseLong("223606003491405824") ) {
				embeds.get(0).withFooterIcon(guild.getUserByID(Long.parseLong("298652669839671296")).getAvatarURL());
				embeds.get(0).withFooterText("Belongs to " + guild.getUserByID(Long.parseLong("298652669839671296")).getName() + " ~~ " + "1/1");
			}
		}
		
	}
	
}
