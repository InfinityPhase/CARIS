package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.MultiReaction;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.MailCheckBuilder;
import caris.framework.library.Variables;
import caris.framework.reactions.ReactionEmbed;
import caris.framework.reactions.ReactionMailSend;
import caris.framework.reactions.ReactionMessage;
import caris.framework.tokens.Mail;
import caris.framework.utilities.Logger;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.obj.IUser;

public class MailHandler extends MessageHandler {

	public MailHandler() {
		super("Mail Handler");
		keyword = "mail";
	}
	
	@Override
	public boolean isTriggered(Event event) {
		return isInvoked() && keywordMatched();
	}
	
	@Override
	public Reaction process(Event event) {
		Logger.debug("Mail detected", 2);
		ArrayList<String> tokens = TokenUtilities.parseTokens(message, new char[] {});
		ArrayList<String> quotes = TokenUtilities.parseQuoted(message);
		MultiReaction mailbox = new MultiReaction(1);
		if( tokens.size() >= 3 ) {
			if( tokens.get(2).equalsIgnoreCase("check") ) {
				mailbox.reactions.add(new ReactionEmbed(new MailCheckBuilder(Variables.guildIndex.get(mrEvent.getGuild()).userIndex.get(mrEvent.getAuthor())).getEmbeds(), mrEvent.getChannel(), 1));
			} else if( tokens.size() >= 4 ) {
				if( tokens.get(2).equalsIgnoreCase("send") ) {
					if( !quotes.isEmpty() ) {
						boolean userFound = false;
						for( IUser user : mrEvent.getGuild().getUsers() ) {
							if( StringUtilities.equalsAnyOfIgnoreCase(tokens.get(3), user.mention(true), user.mention(false)) ) {
								userFound = true;
								mailbox.reactions.add(new ReactionMailSend(Variables.guildIndex.get(mrEvent.getGuild()).userIndex.get(user), new Mail(mrEvent.getAuthor(), quotes.get(0))));
								mailbox.reactions.add(new ReactionMessage(":incoming_envelope: Message sent!", mrEvent.getChannel()));
							}
						}
						if( !userFound ) {
							Logger.debug("Operation failed because no user specified", 2);
							Logger.debug("Reaction produced from " + name, 1, true);
							mailbox.reactions.add(new ReactionMessage("You need to specify a user to send it to! Did you mention them?", mrEvent.getChannel()));
						}
					} else {
						Logger.debug("Operation failed because no message included", 2);
						Logger.debug("Reaction produced from " + name, 1, true);
						mailbox.reactions.add(new ReactionMessage("You need to include a message to send! Did you use quotes?", mrEvent.getChannel()));
					}
				} else {
					Logger.debug("Operation failed due to syntax error", 2);
					Logger.debug("Reaction produced from " + name, 1, true);
					mailbox.reactions.add(new ReactionMessage("Syntax Error!", mrEvent.getChannel()));
				}
			} else {
				Logger.debug("Operation failed due to syntax error", 2);
				Logger.debug("Reaction produced from " + name, 1, true);
				mailbox.reactions.add(new ReactionMessage("Syntax Error!", mrEvent.getChannel()));
			}
		} else {
			Logger.debug("Operation failed due to syntax error", 2);
			Logger.debug("Reaction produced from " + name, 1, true);
			mailbox.reactions.add(new ReactionMessage("Syntax Error!", mrEvent.getChannel()));
		}
		Logger.debug("Response produced from " + name, 1, true);
		return mailbox;
	}
	
}
