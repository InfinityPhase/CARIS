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
			response += "**__Help__**"  +
					"\nCaris is controlled using two types of commands: *Invokers*, and *Responders*."  +
					"\n*Invokers* are commands that you specifically activate, while *Responders* utilize \"natural language processing\" to jump in at the right time.\n"  +
					"\n__Invokers__"  +
					"\nA very simple example of an *Invoker* is **cEcho**."  +
					"\nTo use it, simply type ` cEcho: <message> `, and CARIS will repeat what you put as the ` <message> `."  +
					"\nTo see a list of *Invokers*, type ` cHelp: Invoker `.\n"  +
					"\n__Responders__"  +
					"\nA simple responder would be CARIS's **Mention Responder**, which causes her to respond whenever someone says her name."  +
					"\nTo see a list of *Responders*, type ` cHelp: Responder `.\n";

		} else if( tokens.size() > 1 ) { // Has arguments
			String target = remainder(primaryLineSet.tokens.get(0), primaryLineSet.line);			
			log.indent(1).log("Finding help document for: " + target );

			if( target.equalsIgnoreCase("Invoker") ) {
				response += "**__Invokers__**"  +
						"\n*Invokers* have two parts: the *Main Command*, and the *Subcommands*."  +
						"\nThe *Main Command* is the top part of the command, and each line after is a *Subcommand*."  +
						"\nA message using an *Invoker* might look like this:"  +
						"\n```<Main Command>"  +
						"\n<Subcommand 1>"  +
						"\n<Subcommand 2>"  +
						"\n. . .```\n"  +
						"\n__Main Commands__"  +
						"\nThe *Main Command* is written in the format ` <Command>: <Target> `."  +
						"\nFor example, if you wanted to edit a location, such as the school, you would type ` cLocation: School ` as your *Main Command*.\n"  +
						"\n__Sub Commands__"  +
						"\nThe *Subcommands* are written in the format ` <Action> <Content> `."  +
						"\nFor instance, if you wanted to add \"Alina\" to the aformentioned location, you would type ` add Alina Kim ` as your *Subcommand*."  +
						"\nIn addition, some *Subcommands* don't require a ` <Content> ` field."  +
						"\nResetting a location has the subcommand ` reset `, by itself."  +
						"\nYou can have any number of *Subcommands* following a *Main Command*.\n"  +
						"\nHere is an example of an invocation:"  +
						"\n```cLocation: School"  +
						"\nadd Alina"  +
						"\nadd Anthony```\n"  +
						"\n__All Invokers:__"  +
						"\n\t\t` c8ball `"  +
						"\n\t\t` cEcho `"  +
						"\n\t\t` cEmbed `"  +
						"\n\t\t` cLocation `"  +
						"\n\t\t` cPoll `"  +
						"\n\t\t` cVote `\n"  +
						"\nTo learn how to use a specific *Invoker*, type ` cHelp: <Invoker> `.";
			} else if( target.equalsIgnoreCase("Responder") ) {
				response += "**__Responders__**" +
						"\n*Responders* are quite simple, as CARIS does all the work for you." + 
						"\nIf you were to say, \"Where the heck did Alina disappear off to?\", for instance, CARIS would promptly respond, \"Alina is at school.\"\n" + 
						"\n__All Responders:__" + 
						"\n\t\t` rHelp `" +
						"\n\t\t` rLocation `" +
						"\n\t\t` rMention `" +
						"\n\t\t` rNickname `" +
						"\n\t\t` rReminder `\n" +
						"\nTo learn how a specific *Responder* works, type ` cHelp: <Responder> `";
			} else if( main.Brain.invokerModules.containsKey( target ) ) {
				log.indent(2).log("Found help document");
				response = main.Brain.invokerModules.get( target ).help;
			} else if( library.Variables.commandPrefixes.contains( target ) ) {
				String invoker = getCommand( target );
				if( invoker == "" ) {
					log.indent(2).log("Did not find help document");
					response = "There is no module with that name, or that command.\n" +
							"Are you sure that is a command?";
				} else {
					log.indent(2).log("Found help document");
					response = main.Brain.invokerModules.get( invoker ).help;
				}
			} else if( main.Brain.responderModules.containsKey( target ) ) {
				// Is it a responder?
			} else {
				log.indent(2).log("Did not find help document");
				response = "There is no module with that name, or that command.\n" +
						"Are you sure that is a command?";
			}
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