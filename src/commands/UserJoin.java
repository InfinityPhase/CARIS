package commands;

import library.Variables;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import utilities.BotUtils;
import utilities.Logger;

public class UserJoin extends SuperEvent {
	private Logger log = new Logger().setBaseIndent(1).build();
	
	@Override
	@EventSubscriber
	public void onUserJoin( UserJoinEvent event ) {
		log.log("Checking user...");
		if( !Variables.guildIndex.get( event.getGuild() ).userIndex.containsKey( event.getUser() ) ) {
			log.indent(1).log("Creating new UserInfo Object " + event.getUser().getName() + ":" + event.getUser().getLongID() );
			String message = "Hello, " + event.getUser().getDisplayName(event.getGuild()) + "!"
					+ "\nWelcome to " + event.getGuild().getName() + ".";
			if( !Variables.guildIndex.get(event.getGuild()).rules.isEmpty() ) {
				message += "\nHere are the rules for this guild: \n" + Variables.guildIndex.get(event.getGuild()).rules;
			}
			BotUtils.sendMessage( event.getGuild().getDefaultChannel(), message);
			Variables.guildIndex.get( event.getGuild() ).addUser( event.getUser() );
		}
	}

}
