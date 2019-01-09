package caris.framework.handlers;

import java.util.ArrayList;

import caris.framework.basehandlers.GeneralHandler;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basereactions.Reaction;
import caris.framework.embedbuilders.HelpBuilder;
import caris.framework.library.Constants;
import caris.framework.main.Brain;
import caris.framework.reactions.ReactionEmbed;
import caris.framework.utilities.StringUtilities;
import caris.framework.utilities.TokenUtilities;
import sx.blah.discord.api.events.Event;

public class HelpHandler extends MessageHandler {

	public HelpHandler() {
		super("Help", Access.DEFAULT, false);
		description = "Provides information on how to use " + Constants.NAME + ".";
		usage.put(getInvocation(), "Displays basic information on how to use " + Constants.NAME);
		usage.put(getInvocation() + " <Module>", "Displays information on a module");
	}
	
	@Override
	protected boolean isTriggered(Event event) {
		return isInvoked();
	}
	
	@Override
	protected Reaction process(Event event) {
		ArrayList<String> tokens = TokenUtilities.parseTokens(message);
		GeneralHandler handler = null;
		if( tokens.size() > 1 ) {
			for( int f=1; f<tokens.size(); f++ ) {
				if( StringUtilities.containsIgnoreCase(Brain.handlers.keySet(), tokens.get(f)) ) {
					GeneralHandler temp = Brain.handlers.get(tokens.get(f));
					if( temp instanceof MessageHandler ) {
						MessageHandler m = (MessageHandler) temp;
						switch (m.accessLevel) {
							case DEFAULT:
								handler = temp;
							case ADMIN:
								if( isElevated() ) {
									handler = temp;
								}
							case DEVELOPER:
								if( isDeveloper() ) {
									handler = temp;
								}
						}
					} else {
						handler = temp;
					}
				}
			}
		}
		if( handler == null ) {
			return new ReactionEmbed(new HelpBuilder((isDeveloper()) ? Access.DEVELOPER : ((isAdmin()) ? Access.ADMIN : Access.DEFAULT)).getEmbeds(), mrEvent.getChannel());
		} else {
			return new ReactionEmbed(new HelpBuilder(handler).getEmbeds(), mrEvent.getChannel());
		}
	}

}
