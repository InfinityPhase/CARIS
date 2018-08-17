package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.MessageHandler;
import caris.framework.embedbuilders.MailCheckBuilder;
import caris.framework.embedbuilders.MailOpenBuilder;
import caris.framework.library.Variables;
import caris.framework.reactions.MultiReaction;
import caris.framework.reactions.Reaction;
import caris.framework.reactions.ReactionEmbed;
import caris.framework.reactions.ReactionMailDelete;
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
		keyword = "mailbox";
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
								mailbox.reactions.add(new ReactionMailSend(Variables.guildIndex.get(mrEvent.getGuild()).userIndex.get(mrEvent.getAuthor()), new Mail(user, quotes.get(0))));
								mailbox.reactions.add(new ReactionMessage("Message sent!", mrEvent.getChannel()));
							}
						}
						if( !userFound ) {
							Logger.debug("Operation failed because no user specified", 2);
							Logger.debug("Reaction produced from " + name, 1, true);
							mailbox.reactions.add(new ReactionMessage("You need to specify a user to send it to!", mrEvent.getChannel()));
						}
					} else {
						Logger.debug("Operation failed because no message included", 2);
						Logger.debug("Reaction produced from " + name, 1, true);
						mailbox.reactions.add(new ReactionMessage("You need to include a message to send!", mrEvent.getChannel()));
					}
				} else if( StringUtilities.equalsAnyOfIgnoreCase(tokens.get(2), "open", "delete") ) {
					try {
						int number = Integer.parseInt(tokens.get(3));
						if( number >= 1 && number < Variables.guildIndex.get(mrEvent.getGuild()).userIndex.get(mrEvent.getAuthor()).incomingMail.size()+1 ) {
							if( tokens.get(2).equalsIgnoreCase("open") ) {
								return new ReactionEmbed(new MailOpenBuilder(Variables.guildIndex.get(mrEvent.getGuild()).userIndex.get(mrEvent.getAuthor()).incomingMail.get(number-1)).getEmbeds(), mrEvent.getChannel(), 1);
							} else if( tokens.get(2).equalsIgnoreCase("delete") ) {
								mailbox.reactions.add(new ReactionMailDelete(Variables.guildIndex.get(mrEvent.getGuild()).userIndex.get(mrEvent.getAuthor()), number));
								mailbox.reactions.add(new ReactionMessage("Message deleted!", mrEvent.getChannel()));
							} else {
								Logger.debug("Operation failed due to syntax error", 2);
								Logger.debug("Reaction produced from " + name, 1, true);
								mailbox.reactions.add(new ReactionMessage("Syntax Error!", mrEvent.getChannel()));
							}
						} else {
							Logger.debug("Operation failed because invalid number selected", 2);
							Logger.debug("Reaction produced from " + name, 1, true);
							mailbox.reactions.add(new ReactionMessage("That's not a valid number!", mrEvent.getChannel()));
						}
					} catch(NumberFormatException e) {
						Logger.debug("Operation failed because no number selected", 2);
						Logger.debug("Reaction produced from " + name, 1, true);
						mailbox.reactions.add(new ReactionMessage("You need to select a number!", mrEvent.getChannel()));
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
		return mailbox;
	}
	
}
