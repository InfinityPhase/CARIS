package caris.framework.reactions;

import caris.framework.library.ChannelInfo;
import caris.framework.library.GuildInfo;
import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class ReactionCreateGuild extends Reaction {

	public IGuild guild;
	
	public ReactionCreateGuild(IGuild guild) {
		this(guild, -1);
	}
	
	public ReactionCreateGuild(IGuild guild, int priority) {
		super(-1);
		this.guild = guild;
	}
	
	@Override
	public void execute() {
		if( !Variables.guildIndex.containsKey(guild) ) {
			GuildInfo guildInfo = new GuildInfo( guild.getName(), guild );
			Variables.guildIndex.put( guild, guildInfo );			
			for( IChannel c : guild.getChannels() ) {
				if( !Variables.guildIndex.get(guild).channelIndex.containsKey( c ) ) {
					Variables.guildIndex.get(guild).channelIndex.put( c, new ChannelInfo( c.getName(), c ) );
				}
			}
		}
		Logger.print("Guild (" + guild.getLongID() + ") <" + guild.getName() + "> loaded");
	}
	
}
