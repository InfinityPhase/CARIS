package invokers;

import library.Constants;
import library.Variables;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import tokens.Response;

public class DebugInvoker extends Invoker {
	public Response process( MessageReceivedEvent event ) {
		setup( event );
		
		if( tokens.get( 0 ).equalsIgnoreCase( "debugTest" ) || tokens.get( 1 ).equalsIgnoreCase( "debugtest" ) ) {
			if( Constants.DEBUG ) { System.out.println("Adding message to debugTest map"); }
			Variables.debugTest.put(event.getAuthor().getNicknameForGuild( event.getGuild() ), event );
		}
		return build();
		
	}
	
	@Override
	public int getPriority() {
		return 1;		
	}
}
