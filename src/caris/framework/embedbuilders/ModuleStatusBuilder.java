package caris.framework.embedbuilders;

import java.util.HashMap;

import sx.blah.discord.util.EmbedBuilder;

public class ModuleStatusBuilder {
	
	
	public ModuleStatusBuilder() {}
	
	public EmbedBuilder list(String name, HashMap<String, Boolean> modules) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorName("**__" + name + "__**");
		builder.withDescription("*Module Status:*");
		for( String s : modules.keySet() ) {
			boolean state = modules.get(s);
			if( state ) {
				builder.appendField(s, "***ENABLED***", false);
			} else {
				builder.appendField(s, "*DISABLED*", false);
			}
		}
		builder.withFooterText("--o Admin Console o--");
		return builder;
	}
	
}