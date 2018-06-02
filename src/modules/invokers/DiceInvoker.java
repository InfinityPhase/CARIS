package modules.invokers;

import java.util.Random;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class DiceInvoker extends Invoker {

	public DiceInvoker() {
		 this(Status.ENABLED);
	}

	public DiceInvoker( Status status ) {
		this.status = status;
		prefix = "cDice";
		name = "cDice";
		help = "**__cDice__**"  +
				"\nThis command is used to roll dice."  +
				"\nPlease do not take these answers as life advice."  +
				"\nThere are no subcommands available."  +
				"\n"  +
				"\n```cDice: d8```";
	}

	public Response process(MessageReceivedEvent event) {
		tokenSetup(event);
    String token = tokens.get(0);
		Random r = new Random();
    int n = 0;
    if( token.equals("d2" ) {
		  n = r.nextInt(2)+1;
		} else if( token.equals("d4" ) {
		  n = r.nextInt(4)+1;
		} else if( token.equals("d6" ) {
		  n = r.nextInt(6)+1;
		} else if( token.equals("d8" ) {
		  n = r.nextInt(8)+1;
		} else if( token.equals("d10" ) {
		  n = r.nextInt(10)+1;
		} else if( token.equals("d12" ) {
		  n = r.nextInt(12)+1;
		} else if( token.equals("d20" ) {
		  n = r.nextInt(20)+1;
		} else if( token.equals("d100" ) {
		  n = r.nextInt(100)+1;
		}
    response = "You rolled a " + n + "!";
		return build();
	}
}
