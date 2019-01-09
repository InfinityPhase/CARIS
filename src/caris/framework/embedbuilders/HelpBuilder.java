package caris.framework.embedbuilders;

import caris.framework.basehandlers.Handler;
import caris.framework.basehandlers.MessageHandler;
import caris.framework.basehandlers.MessageHandler.Access;
import caris.framework.library.Constants;
import caris.framework.main.Brain;
import sx.blah.discord.util.EmbedBuilder;

public class HelpBuilder extends Builder {
	
	public MessageHandler handler;
	public Access accessLevel;
	
	public HelpBuilder(Access accessLevel) {
		super();
		this.accessLevel = accessLevel;
	}
	
	public HelpBuilder(MessageHandler handler) {
		super();
		this.handler = handler;
	}
	
	@Override
	public void buildEmbeds() {
		if( handler == null ) {
			buildDefault();
		} else {
			buildSpecific();
		}
	}
	
	public void buildDefault() {
		embeds.add(new EmbedBuilder());
		embeds.get(0).withAuthorName("Caris Help");
		embeds.get(0).withDescription("Caris employs a variety of modules."
				+ "\nSome of them require a specific prefix to use, but most of them can be activated simply by speaking to Caris in a conversational manner."
				+ "**\n\nType `" + Constants.INVOCATION_PREFIX + "Help <module>` for more information on a specific function.**");
		for( String name : Brain.handlers.keySet() ) {
			Handler h = Brain.handlers.get(name);
			if( !h.getDescription().isEmpty() ) {
				if( h instanceof MessageHandler ) {
					MessageHandler m = (MessageHandler) h;
					boolean access = false;
					switch (m.accessLevel) {
						case DEFAULT:
							access = true;
							break;
						case ADMIN:
							access = accessLevel == Access.ADMIN || accessLevel == Access.DEVELOPER;
							break;
						case DEVELOPER:
							access = accessLevel == Access.DEVELOPER;
							break;
					}
					if( access ) {
						embeds.get(0).appendField(name, h.getDescription(), false);
					}
				} else {
					embeds.get(0).appendField(name, h.getDescription(), false);
				}
			}
		}
	}
	
	public void buildSpecific() {
		embeds.add(new EmbedBuilder());
		embeds.get(0).withAuthorName(handler.name);
		embeds.get(0).withTitle("Status: *" + ((handler.enabled) ? "ENABLED" : "DISABLED") + "*");
		embeds.get(0).withDescription(handler.getDescription());
		for( String usage : handler.getUsage().keySet() ) {
			embeds.get(0).appendField("`" + usage + "`", "*" + handler.getUsage().get(usage) + "*", false);
		}
		String info = "";
		if( handler instanceof MessageHandler ) {
			MessageHandler messageHandler = (MessageHandler) handler;
			switch (messageHandler.accessLevel) {
			case DEFAULT:
				info = "Active | Default";
				break;
			case ADMIN:
				info = "Active | Admin Only";
				break;
			case DEVELOPER:
				info = "Active | Developer Onlyv";
				break;
			}
		} else {
			info = "Passive";
		}
		embeds.get(0).withFooterText(info);
	}
	
}
