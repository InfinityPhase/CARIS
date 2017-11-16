package embedbuilders;

import java.util.HashMap;

import sx.blah.discord.util.EmbedBuilder;

public class ModuleStatusBuilder {
	
	
	public ModuleStatusBuilder() {}
	
	public EmbedBuilder list(String name, HashMap<String, Boolean> modules) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorName("Module Status:");
		builder.withTitle("**__" + name + "__**");
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