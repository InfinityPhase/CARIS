package caris.framework.embedbuilders;

import caris.framework.library.Constants;
import caris.framework.main.Brain;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.EmbedBuilder;

public class GuildInfoBuilder extends Builder {
	
	public GuildInfoBuilder() {
		super();
	}
	
	@Override
	protected void buildEmbeds() {
		embeds.add(new EmbedBuilder());
		embeds.get(0).withAuthorName(Constants.NAME + " [Guild Data]");
		embeds.get(0).withDesc("__Guilds__");
		int count = 0;
		for( IGuild guild : Brain.cli.getGuilds() ) {
			if( count != 0 && count%25 == 0 ) {
				embeds.add(new EmbedBuilder());
				embeds.get(count/25).withAuthorName(Constants.NAME + "[Guild Data]");
			}
			embeds.get(count/25).appendField("**" + guild.getName() + "**", "*(" + guild.getLongID() + ")*", false);
			count++;
		}
		for( int f=0; f<embeds.size(); f++ ) {
			embeds.get(f).withFooterText("Page " + (f+1) + " / " + embeds.size());
		}
	}
	
}
