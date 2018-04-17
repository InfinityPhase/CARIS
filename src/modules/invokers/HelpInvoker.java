package modules.invokers;

import main.Brain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class HelpInvoker extends Invoker {

	public HelpInvoker() {
		this( Status.ENABLED );
	}

	public HelpInvoker( Status status ) {
		log.log("Initializing Help Invoker");
		this.status = status;
		name = "cHelp";
		prefix = "cHelp";
	}

	@Override
	public Response process(MessageReceivedEvent event) {
		linesetSetup(event);
		if( command.tokens.size() == 1 ) { // No arguments passed
			log.indent(1).log("Giving generic Help");
			response += "**__Help__**"  +
					"\nCaris is controlled using three types of commands: *Responders*, *Invokers*, and *Constructors*."  +
					"\n*Responders* utilize \"natural language processing\" to jump in at the right time, while *Invokers* are commands that you specifically activate.\n"  +
					"\n*Constructors* are a special type of *Invoker* that require multiple lines to use, but shouldn't need to be used very often." + 
					"\n__Responders__"  +
					"\nA simple responder would be CARIS's **Mention Responder**, which causes her to respond whenever someone says her name."  +
					"\nTo see a list of *Responders*, type ` cHelp: Responder `.\n" + 
					"\n__Invokers__"  +
					"\nA very simple example of an *Invoker* is **cEcho**."  +
					"\nTo use it, simply type ` cEcho: <message> `, and CARIS will repeat what you put as the ` <message> `."  +
					"\nTo see a list of *Invokers*, type ` cHelp: Invoker `.\n" + 
					"\n__Constructors__" + 
					"\nConstructors are a fair bit more complex." +  
					"\nFor more information on them, type ` cHelp: Constructor `.";

		} else if( command.tokens.size() > 1 ) { // Has arguments
			String target = command.tokens.get(1);			
			log.indent(1).log("Finding help document for: " + target );

			if( target.equalsIgnoreCase("Invoker") ) {
				response += "**__Invokers__**"  +
						"\n*Invokers* have two parts: the *Invocation*, and the *Arguments*."  +
						"\nThe *Invocation* is the first part of the command, and is followed by *Arguments*."  +
						"\nIf a command has more than one *Argument*, they are separated by a space." + 
						"\nIn the case that you need a space in an argument, simply surround the argument with quotes.\n" +
						"\nA message using an *Invoker* might look like this:"  +
						"\n```<invocation>: <argument1> <argument2>```\n"  +
						"\nHere is an example of an invocation:"  +
						"\n```cLocation: School add \"Alina Kim\" Anthony```\n" +
						"\n__All Invokers:__"  +
						"\n\t\t` c8ball `"  +
						"\n\t\t` cEcho `"  +
						"\n\t\t` cLocation `"  +
						"\n\t\t` cVote `\n"  +
						"\nTo learn how to use a specific *Invoker*, type ` cHelp: <Invoker> `.";
			} else if( target.equalsIgnoreCase("Responder") ) {
				response += "**__Responders__**" +
						"\n*Responders* are quite simple, as CARIS does all the work for you." + 
						"\nIf you were to say, \"Where the heck did Alina disappear off to?\", for instance, CARIS would promptly respond, \"Alina is at school.\"\n" + 
						"\n__All Responders:__" + 
						"\n\t\t` rLocation `" +
						"\n\t\t` rMention `" +
						"\n\t\t` rNickname `" +
						"\n\t\t` rReminder `\n" +
						"\nTo learn how a specific *Responder* works, type ` cHelp: <Responder> `";
			} else if( target.equalsIgnoreCase("Constructor") ) {
				response += "**__Constructors__**" +
						"\n*Constructors* are a bit more complex, but have a high level of fuctionality." +
						"\nIn essence, they allow you to create interfaces that other users can view or interact with." +
						"\nConstructors have two parts: the *Main Command*, and the *Sub Commands*.\n" +
						"\nThe *Main Command* is written in the format ` <Command>: <Target> `, and is pretty much the same as how you would use an *Invoker*."  +
						"\nFor example, if you wanted to create a poll for favorite colors, you could type ` cPoll: Color ` as your *Main Command*.\n"  +
						"\n__Sub Commands__"  +
						"\nThe *Subcommands* are written in the format ` <Function> <Content> `."  +
						"\nFor instance, if you wanted to add \"Alina\" to the aformentioned location, you would type ` add Alina ` as your *Subcommand*."  +
						"\nYou can have any number of *Subcommands* following a *Main Command*.\n"  +
						"\nHere is an example of an Constructor:"  +
						"\n```cPoll: Color"  +
						"\nooption Red"  +
						"\noption Blue```\n" +
						"\n__All Constructors:__" +
						"\n\t\t` cPoll `" +
						"\n\t\t` cEmbed `";
			} else if( Brain.responderModules.containsKey( target ) ) {
				log.indent(2).log("Found help document");
				response = Brain.responderModules.get( target ).help;
			} else if( Brain.invokerModules.containsKey( target ) ) {
				log.indent(2).log("Found help document");
				response = Brain.invokerModules.get( target ).help;
			} else if( Brain.constructorModules.containsKey( target ) ) {
				log.indent(2).log("Found help document");
				response = Brain.constructorModules.get( target ).help;
			} else {
				log.indent(2).log("Did not find help document");
				response = "There is no module with that name, or that command.\n" +
						"Are you sure that is a command?";
			}
		}

		return build();
	}
}
