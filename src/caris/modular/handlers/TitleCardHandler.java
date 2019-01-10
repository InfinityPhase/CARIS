package caris.modular.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.Builder;
import caris.framework.events.MessageEventWrapper;
import caris.framework.reactions.ReactionEmbed;
import caris.framework.reactions.ReactionMessage;
import caris.framework.utilities.StringUtilities;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class TitleCardHandler extends MessageHandler {

	public TitleCardHandler() {
		super("TitleCard");
	}
	
	@Override
	protected boolean isTriggered(MessageEventWrapper messageEventWrapper) {
		return messageEventWrapper.message.startsWith(">im ");
	}
	
	@Override
	protected Reaction process(MessageEventWrapper messageEventWrapper) {
		ArrayList<String> tokens = messageEventWrapper.tokens;
		IUser subject = null;
		for( IUser user : messageEventWrapper.getGuild().getUsers() ) {
			if( StringUtilities.equalsAnyOfIgnoreCase(tokens.get(1), user.mention(true), user.mention(false)) ) {
				subject = user;
			}
		}
		if( subject == null ) {
			return new ReactionMessage( "**" + messageEventWrapper.getAuthor().getDisplayName(messageEventWrapper.getGuild()) + "**, no one answered to the call and no work found.", messageEventWrapper.getChannel() );
		} else {
			return new ReactionEmbed((new TitleCardBuilder(subject, messageEventWrapper.getGuild())).getEmbeds(), messageEventWrapper.getChannel());
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
	
	@Override
	public String getDescription() {
		return "";
	}
	
	@Override
	public HashMap<String, String> getUsage() {
		return null;
	}
	
}
