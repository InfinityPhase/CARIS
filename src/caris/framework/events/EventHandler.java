package caris.framework.events;

import java.util.ArrayList;
import java.util.Arrays;

import caris.framework.handlers.Handler;
import caris.framework.main.Brain;
import caris.framework.reactions.Reaction;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventSubscriber;
import tokens.Response;

public class EventHandler extends SuperEvent {

	@EventSubscriber
	@Override
	public void onEvent(Event event) {
		ArrayList<Reaction> reactions = new ArrayList<Reaction>();
		for( Handler h : Brain.handlers.values() ) {
			Reaction r = h.handle(event);
			if( r != null ) {
				reactions.add(r);
			}
		}
		if( !reactions.isEmpty() ) {
			Reaction[] options = new Reaction[reactions.size()];
			for( int f=0; f<reactions.size(); f++ ) {
				options[f] = reactions.get(f);
			}
			Arrays.sort(options);
			options[0].execute();
		}
	}
}
