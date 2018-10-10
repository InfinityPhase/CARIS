package caris.framework.embedbuilders;

import caris.framework.library.GuildInfo;
import caris.framework.library.Variables;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class UserInfoBuilder extends Builder {
	
	private GuildInfo guildInfo;
	private IGuild guild;
	
	public UserInfoBuilder(IGuild guild) {
		super();
		this.guild = guild;
		this.guildInfo = Variables.guildIndex.get(guild);
	}
	
	@Override
	protected void buildEmbeds() {
		embeds.add(new EmbedBuilder());
		embeds.get(0).withAuthorName(guildInfo.name + " [User Data]");
		embeds.get(0).withTitle("*ID: " + guild.getStringID() + "*");
		embeds.get(0).withDesc("__Users__");
		int count = 0;
		for( IUser user : guild.getUsers() ) {
			if( count != 0 && count%25 == 0 ) {
				embeds.add(new EmbedBuilder());
				embeds.get(count/25).withAuthorName(guildInfo.name + "[User Data]");
			}
			embeds.get(count/25).appendField("**" + user.getName() + "#" + user.getDiscriminator() + " -- " + user.getPresence().getStatus().toString() + "**", "<" + user.getDisplayName(guild) + "> *(" + user.getLongID() + ")*", false);
			count++;
		}
		for( int f=0; f<embeds.size(); f++ ) {
			embeds.get(f).withFooterText("Page " + (f+1) + " / " + embeds.size());
		}
	}
	
}
