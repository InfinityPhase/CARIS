package invokers;

import library.Fortunes;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class FortuneInvoker extends Invoker {

	public FortuneInvoker() {
		status = Status.ENABLED;
		name = "Fortune";
		prefix = "cFortune";
	}

	public FortuneInvoker( Status status ) {
		this.status = status;
		name = "Fortune";
		prefix = "cFortune";
		help = "**__cFortune__**"  +
				"\nThis command has CARIS implement a carefully tailored prediction algorithm, designed to calculate the probablity of any given event."  +
				"\nEvents are given in a human readable format for parsing."  +
				"\nThere are no subcommands available."  +
				"\n"  +
				"\n```cEcho: Should I be happy today?```";
	}

	@Override
	public Response process(MessageReceivedEvent event) {
		tokenSetup(event);

		if( Math.random() < 0.01 ) {
			int chooser = (int) (Math.random() * Fortunes.EASTER_EGG_FORTUNES.length);
			response = Fortunes.EASTER_EGG_FORTUNES[chooser];
		} else {
			int chooser = (int) (Math.random() * Fortunes.FORTUNES.length);
			response = Fortunes.FORTUNES[chooser];
		}

		return build();
	}

}
