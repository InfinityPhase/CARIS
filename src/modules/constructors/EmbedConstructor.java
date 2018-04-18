package modules.constructors;

import java.awt.Color;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import tokens.LineSet;
import tokens.Response;

public class EmbedConstructor extends Constructor {

	public EmbedConstructor() {
		this( Status.ENABLED );
	}

	public EmbedConstructor( Status status ) {
		this.status = status;
		name = "cEmbed";
		prefix = "cEmbed";
		help = "\n__cEmbed__"  +
				"\nThis command allows you to create Embeded messages."  +
				"\nUse ` cEmbed: <Title> ` as the *Main Command*"  +
				"\nEach subcommand represents a property in the Embed, along with its content."  +
				"\nHere are the properties you can edit:"  +
				"\n\t\t` title <Text> `\t\t-\t\t*The display title of the embed*"  +
				"\n\t\t` url <Link> `\t\t-\t\t*The link clicking the embed forwards you to*"  +
				"\n\t\t` description <Text> `\t\t-\t\t*The description of the embed*"  +
				"\n\t\t` color <Text> `\t\t-\t\t*The color of the embed sidebar*"  +
				"\n\t\t` image <Image Link> `\t\t-\t\t*A url linking to an image*"  +
				"\n\t\t` field <Text> | <Text> `\t\t-\t\t*A field with it's own content*"  +
				"\n\t\t` field-inline <Text> ` | <Text>\t\t-\t\t*A field but in line with other field-inlines*"  +
				"\n\t\t\t\t*fields have two parts  + the title and the content, separated by \" | \".*"  +
				"\n\t\t` author <Text> `\t\t-\t\t*The display author of the embed*"  +
				"\n\t\t` thumbnail <Image Link> `\t\t-\t\t*A url linking to a thumbnail image*"  +
				"\n\t\t` footer-icon <Image Link> `\t\t-\t\t*A url linking to the footer icon*"  +
				"\n\t\t` footer-text <Text> `\t\t-\t\t*Text shown at the end of an embed*"  +
				"\n"  +
				"\n```cEmbed: New Embed"  +
				"\ntitle A new Embed"  +
				"\ndescription Basically a test of the embed invoker"  +
				"\ncolor blue"  +
				"\nimage https://cdn.discordapp.com/embed/avatars/0.png"  +
				"\nfield Field Name | Field Content"  +
				"\nfooter-text End of the embed!```";
	}

	@Override
	public Response process(MessageReceivedEvent event) {
		multilineSetup(event);

		String target = remainder(primaryLineSet.tokens.get(0), primaryLineSet.line).toLowerCase();
		if( target.isEmpty() ) {
			log.indent(2).log("SyntaxError. Aborting.");
			response = "Please enter a valid Embed name.";
			return build();
		} else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.withTitle(target);
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
							}
						} else if( field.equalsIgnoreCase("field-inline") ) { 
							int index = content.indexOf(" | ");
							if( index != -1 && index != content.length() ) {
								String name = content.substring(0, index);
								String text = content.substring(index+1);
								builder.appendField(name, text, true);
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
			event.getMessage().delete();
			embed = builder;
		}

		return build();
	}

}
