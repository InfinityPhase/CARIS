package invokers;

import java.awt.Color;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import tokens.LineSet;
import tokens.Response;

public class EmbedInvoker extends Invoker_Multiline {
	
	@Override
	public Response process(MessageReceivedEvent event) {
		multilineSetup(event);
		
		if( tokens.get(0).equals("cEmbed:") ) {
			log.indent(1).log("PollInvoker triggered.");
			String target = remainder(primaryLineSet.tokens.get(0), primaryLineSet.line);
			if( target.isEmpty() ) {
				log.indent(2).log("SyntaxError. Aborting.");
				response = "Please enter a valid Embed name.";
				return build();
			} else {
				EmbedBuilder builder = new EmbedBuilder();
				builder.withAuthorName(event.getAuthor().getDisplayName(event.getGuild()));
				builder.withAuthorIcon(event.getAuthor().getAvatarURL());
				for( LineSet ls : auxiliaryLineSets ) {
					if( !ls.line.isEmpty() ) {
						String field = ls.tokens.get(0);
						String content = remainder(field, ls.line);
						if( !content.isEmpty() ) {
							if( field.equalsIgnoreCase("title") ) {
								builder.withTitle(content);
							} else if( field.equalsIgnoreCase("url") ) {
								builder.withUrl(content);
							} else if( field.equalsIgnoreCase("description") ) {
								builder.withDescription(content);
							} else if( field.equalsIgnoreCase("color") ) {
								if( content.equalsIgnoreCase("black") ) {
									builder.withColor(Color.BLACK);
								} else if( content.equalsIgnoreCase("blue") ) {
									builder.withColor(Color.BLUE);
								} else if( content.equalsIgnoreCase("cyan") ) {
									builder.withColor(Color.CYAN);
								} else if( content.equalsIgnoreCase("dark gray") || content.equalsIgnoreCase("dark grey") ) {
									builder.withColor(Color.DARK_GRAY);
								} else if( content.equalsIgnoreCase("gray") || content.equalsIgnoreCase("grey") ) {
									builder.withColor(Color.GRAY);
								} else if( content.equalsIgnoreCase("green") ) {
									builder.withColor(Color.GREEN);
								} else if( content.equalsIgnoreCase("light gray") || content.equalsIgnoreCase("light grey") ) {
									builder.withColor(Color.LIGHT_GRAY);
								} else if( content.equalsIgnoreCase("magenta") ) {
									builder.withColor(Color.MAGENTA);
								} else if( content.equalsIgnoreCase("orange") ) {
									builder.withColor(Color.ORANGE);
								} else if( content.equalsIgnoreCase("pink") ) {
									builder.withColor(Color.PINK);
								} else if( content.equalsIgnoreCase("red") ) {
									builder.withColor(Color.RED);
								} else if( content.equalsIgnoreCase("white") ) {
									builder.withColor(Color.WHITE);
								} else if( content.equalsIgnoreCase("yellow") ) {
									builder.withColor(Color.YELLOW);
								}
							} else if( field.equalsIgnoreCase("image") ) {
								builder.withImage(content);
							} else if( field.equalsIgnoreCase("field") ) {
								int index = content.indexOf(" | ");
								if( index != -1 && index != content.length() ) {
									String name = content.substring(0, index);
									String text = content.substring(index+1);
									builder.appendField(name, text, false);
								} else {
									builder.appendField(content, "", false);
								}
							} else if( field.equalsIgnoreCase("field-inline") ) { 
								int index = content.indexOf(" | ");
								if( index != -1 && index != content.length() ) {
									String name = content.substring(0, index);
									String text = content.substring(index+1);
									builder.appendField(name, text, true);
								} else {
									builder.appendField(content, "", true);
								}
							} else if( field.equalsIgnoreCase("author") ) {
								builder.withAuthorName(content);
							} else if( field.equalsIgnoreCase("footer-icon") ) {
								builder.withFooterIcon(content);
							} else if( field.equalsIgnoreCase("footer-text") ) {
								builder.withFooterText(content);
							} else if( field.equalsIgnoreCase("thumbnail") ) {
								builder.withThumbnail(content);
							}
						}
					}
				}
				embed = builder;
			}
		}
		
		return build();
	}
	
}
