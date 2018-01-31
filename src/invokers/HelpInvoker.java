package invokers;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class HelpInvoker extends Invoker {

	public HelpInvoker() {
		this( Status.ENABLED );
	}

	public HelpInvoker( Status status ) {
		log.log("Initializing Help Invoker");
		this.status = status;
		name = "Help";
		prefix = "cHelp";
	}

	@Override
	public Response process(MessageReceivedEvent event) {
		multilineSetup(event);
		if( tokens.size() == 1 ) { // No arguments passed
			log.indent(1).log("Giving generic Help");
			response += "**__Help__**";
			response += "\nCaris is controlled using two types of commands: *Invokers*, and *Responders*.";
			response += "\n*Invokers* are commands that you specifically activate, while *Responders* utilize \"natural language processing\" to jump in at the right time.";
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


		} else if( tokens.size() > 1 ) { // Has arguments	
			log.indent(1).log("Finding help document for: " + tokens.get(1) );
			
			if( main.Brain.invokerModules.containsKey( tokens.get(1) ) ) {
				log.indent(2).log("Found help document");
				response = main.Brain.invokerModules.get( tokens.get(1) ).help;
			} else if( library.Variables.commandPrefixes.contains( tokens.get(1) ) ) {
				String invoker = getCommand( tokens.get(1) );
				if( invoker == "" ) {
					log.indent(2).log("Did not find help document");
					response = "There is no module with that name, or that command.\n" +
							"Are you sure that is a command?";
				} else {
					log.indent(2).log("Found help document");
					response = main.Brain.invokerModules.get( invoker ).help;
				}
			} else {
				log.indent(2).log("Did not find help document");
				response = "There is no module with that name, or that command.\n" +
						"Are you sure that is a command?";
			}
			/*
			log.indent(1).log("Giving specific Help");
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

				response += "\n__All Invokers:__";
				response += "\n\t\t` c8ball `";
				response += "\n\t\t` cEcho `";
				response += "\n\t\t` cEmbed `";
				response += "\n\t\t` cLocation `";
				response += "\n\t\t` cPoll `";
				response += "\n\t\t` cVote `";
				response += "\n";

				response += "\nTo learn how to use a specific *Invoker*, type ` cHelp: <Invoker> `.";
			} else if( target.equalsIgnoreCase("Responder") ) {
				response += "**__Responders__**";
				response += "\n*Responders* are quite simple, as CARIS does all the work for you.";
				response += "\nIf you were to say, \"Where the heck did Alina disappear off to?\", for instance, CARIS would promptly respond, \"Alina is at school.\"";
				response += "\n";

				response += "\n__All Responders:__";
				response += "\n\t\t` rHelp `";
				response += "\n\t\t` rLocation `";
				response += "\n\t\t` rMention `";
				response += "\n\t\t` rNickname `";
				response += "\n\t\t` rReminder `";
				response += "\n";
				response += "\nTo learn how a specific *Responder* works, type ` cHelp: <Responder> `";
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
				response += "\nUse ` cEmbed: <Title> ` as the *Main Command*";
				response += "\nEach subcommand represents a property in the Embed, along with its content.";
				response += "\nHere are the properties you can edit:";
				response += "\n\t\t` title <Text> `\t\t-\t\t*The display title of the embed*";
				response += "\n\t\t` url <Link> `\t\t-\t\t*The link clicking the embed forwards you to*";
				response += "\n\t\t` description <Text> `\t\t-\t\t*The description of the embed*";
				response += "\n\t\t` color <Text> `\t\t-\t\t*The color of the embed sidebar*";
				response += "\n\t\t` image <Image Link> `\t\t-\t\t*A url linking to an image*";
				response += "\n\t\t` field <Text> | <Text> `\t\t-\t\t*A field with it's own content*";
				response += "\n\t\t` field-inline <Text> ` | <Text>\t\t-\t\t*A field but in line with other field-inlines*";
				response += "\n\t\t\t\t*fields have two parts; the title and the content, separated by \" | \".*";
				response += "\n\t\t` author <Text> `\t\t-\t\t*The display author of the embed*";
				response += "\n\t\t` thumbnail <Image Link> `\t\t-\t\t*A url linking to a thumbnail image*";
				response += "\n\t\t` footer-icon <Image Link> `\t\t-\t\t*A url linking to the footer icon*";
				response += "\n\t\t` footer-text <Text> `\t\t-\t\t*Text shown at the end of an embed*";
				response += "\n";

				response += "\n```cEmbed: New Embed";
				response += "\ntitle A new Embed";
				response += "\ndescription Basically a test of the embed invoker";
				response += "\ncolor blue";
				response += "\nimage https://cdn.discordapp.com/embed/avatars/0.png";
				response += "\nfield Field Name | Field Content";
				response += "\nfooter-text End of the embed!```";
			} else if( target.equalsIgnoreCase("cLocation") ) {
				response += "**__cLocation__**";
				response += "\nThis command allows you to keep track of where everyone is.";
				response += "\nUse ` cLoc: <Location Name> ` as the *Main Command*.";
				response += "\n";

				response += "\n\t\t` add <Name> `\t\t-\t\t*Adds a person to a location*";
				response += "\n\t\t` remove <Name> `\t\t-\t\t*Removes a person from a location*";
				response += "\n\t\t` reset `\t\t-\t\t*Removes everyone from a location*";
				response += "\n";

				response += "\n```cLocation: School";
				response += "\nreset";
				response += "\nadd Alina";
				response += "\nadd Anthony";
				response += "\nremove Alina```";
			} else if( target.equalsIgnoreCase("cPoll") ) {
				response += "**__cPoll__**";
				response += "\nThis command lets you create polls other people can vote on.";
				response += "\nUse ` cPoll: <Poll Name> ` as the *Main Command*.";
				response += "\nIf no subcommands are used, CARIS will display the current state of the poll.";
				response += "\n";

				response += "\n\t\t` description <Text> `\t\t-\t\t*Sets the description or question the poll is asking*";
				response += "\n\t\t` option <Text> `\t\t-\t\t*Adds a votable option to the poll*";
				response += "\n\t\t` add <Text> `\t\t-\t\t*If the poll already exists, adds a new option to the poll*";
				response += "\n\t\t` remove <Text> `\t\t-\t\t*If the poll already exists, removes an option from the poll*";
				response += "\n\t\t` reset `\t\t-\t\t*Removes all votes from the poll*";
				response += "\n\t\t` end `\t\t-\t\t*Ends the poll and displays the results*";
				response += "\n";
				response += "\n```cPoll: Apples v Oranges";
				response += "\ndescription Do you prefer apples, or oranges?";
				response += "\noption Apples";
				response += "\noption Oranges```";

			} else if( target.equalsIgnoreCase("cVote") ) {
				response += "**__cVote__**";
				response += "\nThis command lets others vote on existing polls.";
				response += "\nUse ` cVote: <Poll Name> ` as the *Main Command*.";
				response += "\nInstead of subcommands, simply type the choice(s) you wish to vote for.";
				response += "\n";

				response += "\n```cVote: Apples v Oranges";
				response += "\nApples```";
			} else if( target.equalsIgnoreCase("rHelp") ) {
				response += "**__Help Responder__**";
				response += "\nIf you say anything like \"how do I use CARIS\" in the chat, CARIS will respond with the help menu.";
				response += "\n";

				response += "\n*\"Hey, how am I supposed to use CARIS?\"*";
			} else if( target.equalsIgnoreCase("rLocation") ) {
				response += "\n**__Location Responder__**";
				response += "\nIf you ask where anyone is, or who's at a certain location, CARIS will see if she knows, and respond appropriately.";
				response += "\n";

				response += "\n*\"Where the heck did Alina go?\"*";
				response += "\n*\"Does anyone know who's at school right now?\"*";
			} else if( target.equalsIgnoreCase("rMention") ) {
				response += "**__Mention Reponder__**";
				response += "\nPretty simple: you say CARIS's name, she responds.";
				response += "\n";

				response += "\n*\"Caris, are you online?\"*";
			} else if( target.equalsIgnoreCase("rNickname") ) {
				response += "**__Nickname Responder__**";
				response += "\nIf you ask CARIS to set your name to something in the chat, she'll do it for you.";
				response += "\nRemember to put your name in quotes!";
				response += "\n";

				response += "\n*\"Caris, set my name to \"Alina Kim\".\"*";
				response += "\n*\"My name is \"Alina Kim\"!\"*";
			} else if( target.equalsIgnoreCase("rReminder") ) {
				response += "\n**__Reminder Responder__**";
				response += "\nYou can even ask CARIS to set reminders for you!";
				response += "\nYou can ask her to remind you at a certain time, or in a certain timer.";
				response += "\nKeep in mind that you need to use military time, or PM.";
				response += "\nUse digits instead of words to express numbers.";
				response += "\nIf you want to include a message, put it in quotes.";
				response += "\n";

				response += "\n*\"Can someone remind me to \"check messages\" at 4 PM?\"*";
				response += "\n*\"Remind me on March 26th to wish Alina a happy birthday.\"*";
				response += "\n*\"CARIS, try to remind me in about 5 minutes.*";
			} */
		}

		return build();
	}
	
	private String getCommand( String prefix ) {
		for( String s : main.Brain.invokerModules.keySet() ) {
			if( main.Brain.invokerModules.get(s).prefix.equalsIgnoreCase( prefix ) ) {
				return s;
			}
		}
		
		return "";
	}

}
