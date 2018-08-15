package caris.framework.embedbuilders;

import sx.blah.discord.util.EmbedBuilder;

public abstract class Builder {

	protected EmbedBuilder builder;
	
	public Builder() {
		builder = new EmbedBuilder();
	}
	
	public EmbedBuilder getEmbed() {
		buildEmbed();
		return builder;
	}
	
	protected void buildEmbed() {
		// Build embed
	}

}
