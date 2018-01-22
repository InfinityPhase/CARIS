package invokers;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class HelpInvoker extends Invoker_Multiline {
	
	@Override
	 public Response process(MessageReceivedEvent event) {
		multilineSetup(event);
		
		if( event.getMessage().getContent().equalsIgnoreCase("cHelp") ) {
			response += "**__Help__**";
			response += "\nCaris is controlled using two types of commands: *Invokers*, and *Responders*.";
			response += "\n*Invokers* are commands that you specifically activate, while *Responders* utilize natural language processing to jump in at the right time.";
			response += "\n";
			
			response += "\n__Invokers__";
			response += "\nA very simple example of an *Invoker* is **cEcho**.";
			response += "\nTo use it, simply type ` cEcho: <message> `, and CARIS will repeat what you put as the ` <message> `.";
			response += "\nTo see a list of *Invokers*, type ` cHelp: Invoker `.";

			response += "\n";
			
			response += "\n__Responders__";
			response += "\nA simple responder would be CARIS's **Mention Responder**, which causes her to respond whenever someone says her name.";
			response += "\nTo see a list of *Responders*, type ` cHelp: Responder `.";
			response += "\n";
		}
		
		if( tokens.get(0).equalsIgnoreCase("cHelp:") ) {
			String target = remainder(primaryLineSet.tokens.get(0), primaryLineSet.line);
			if( target.equalsIgnoreCase("Invoker") ) {
				response += "**__Invokers__**";
				response += "\n*Invokers* have two parts: the *Main Command*, and the *Subcommands*.";
				response += "\nThe *Main Command* is the top part of the command, and each line after is a *Subcommand*.";
				response += "\nA message using an *Invoker* might look like this:";
				response += "\n```<Main Command>";
				response += "\n<Subcommand 1>";
				response += "\n<Subcommand 2>";
				response += "\n. . .```";
				response += "\n";
				
				response += "\n__Main Commands__";
				response += "\nThe *Main Command* is written in the format ` <Command>: <Target> `.";
				response += "\nFor example, if you wanted to edit a location, such as the school, you would type ` cLocation: School ` as your *Main Command*.";
				response += "\n";
				
				response += "\n__Sub Commands__";
				response += "\nThe *Subcommands* are written in the format ` <Action> <Content> `.";
				response += "\nFor instance, if you wanted to add \"Alina\" to the aformentioned location, you would type ` add Alina Kim ` as your *Subcommand*.";
				response += "\nIn addition, some *Subcommands* don't require a ` <Content> ` field.";
				response += "\nResetting a location has the subcommand ` reset `, by itself.";
				response += "\nYou can have any number of *Subcommands* following a *Main Command*.";
				response += "\n";
				
				response += "\nHere is an example of an invocation:";
				response += "\n```cLocation: School";
				response += "\nadd Alina";
				response += "\nadd Anthony```";
				response += "\n";
				
				response += "\n__All Main Commands:__";
				response += "\n\tc8ball";
				response += "\n\tcEcho";
				response += "\n\tcEmbed";
				response += "\n\tcLocation";
				response += "\n\tcPoll";
				response += "\n\tcVote";
				response += "\n";
				
				response += "\nTo learn how to use a specific *Main Command*, type ` cHelp: <Command> `.";
			} else if( target.equalsIgnoreCase("Responder") ) {
				response += "**__Responders__**";
				response += "\n*Responders* are quite simple, as CARIS does all the work for you.";
				response += "\nIf you were to say, \"Where the heck did Alina disappear off to?\", for instance, CARIS would promptly respond, \"Alina is at school.\"";
				response += "\n";
				response += "\n__All Responders:__";
				response += "\n\tHelp";
				response += "\n\tLocation";
				response += "\n\tMention";
				response += "\n\tNickname";
				response += "\n\tReminder";
			} else if( target.equalsIgnoreCase("c8ball") ) {
				response += "**__c8ball__**";
				response += "\nThis command is used to randomly answer yes/no questions.";
				response += "\nPlease do not take these answers as life advice.";
				response += "\nThere are no subcommands available.";
				response += "\n";
				response += "\n```c8ball: Will I ever find love?```";
			} else if( target.equalsIgnoreCase("cEcho") ) {
				response += "**__cEcho__**";
				response += "\nThis command is used to make CARIS say somemthing.";
				response += "\nPlease don't abuse this command.";
				response += "\nThere are no subcommands available.";
				response += "\n";
				response += "\n```cEcho: My name is CARIS!```";
			} else if( target.equalsIgnoreCase("cEmbed") ) {
				response += "\n__cEmbed__";
				response += "\nThis command allows you to create Embeded messages.";
				response += "\nEach subcommand represents a property in the Embed, along with its content.";
				response += "\nHere are the properties you can edit:";
				response += "\n\ttitle\t-\t*The display title of the embed*";
				response += "\n\turl\t-\tThe link clicking the embed forwards you to";
				response += "\n\tdescription\t-\tThe description of the embed";
				response += "\n\tcolor\t-\tThe color of the embed sidebar";
				response += "\n\timage\t-\tA url linking to an image";
				response += "\n\tfield\t-\tA field with it's own content";
				response += "\n\tfield-inline\t-\tA field but in line with other field-inlines";
				response += "\n\t\tfields have two parts; the title and the content, separated by \" | \".";
				response += "\n\tauthor\t-\tThe display author of the embed";
				response += "\n\tthumbnail\t-\tA url linking to a thumbnail image";
				response += "\n\tfooter-icon\t-\tA url linking to the footer icon";
				response += "\n\tfooter-text\t-\tText shown at the end of an embed";
				response += "\n";
				response += "\n```cEmbed: New Embed";
				response += "\ntitle A new Embed";
				response += "\ndescription Basically a test of the embed invoker";
				response += "\ncolor blue";
				response += "\nimage https://cdn.discordapp.com/embed/avatars/0.png";
				response += "\nfield Field Name | Field Content";
				response += "\nfooter-text End of the embed!";
			} else if( target.equalsIgnoreCase("cLocation") ) {
				response += "**__cLocation__**";
				
			} else if( target.equalsIgnoreCase("cPoll") ) {
				
			} else if( target.equalsIgnoreCase("cVote") ) {
				
			} else if( target.equalsIgnoreCase("rHelp") ) {
				
			} else if( target.equalsIgnoreCase("rLocation") ) {
				
			} else if( target.equalsIgnoreCase("rMention") ) {
				
			} else if( target.equalsIgnoreCase("rNickname") ) {
				
			} else if( target.equalsIgnoreCase("rReminder") ) {
				
			}
		}
		
		return build();
	}
	
}
