package invokers;

import library.Fortunes;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class FortuneInvoker extends Invoker {
	
	@Override
	public Response process(MessageReceivedEvent event) {
		tokenSetup(event);
		if( tokens.get(0).equals("cFortune:") ) {
			if( Math.random() < 0.01 ) {
				int chooser = (int) (Math.random() * Fortunes.EASTER_EGG_FORTUNES.length);
				response = Fortunes.EASTER_EGG_FORTUNES[chooser];
			} else {
				int chooser = (int) (Math.random() * Fortunes.FORTUNES.length);
				response = Fortunes.FORTUNES[chooser];
			}
		}
		return build();
	}
	
}
