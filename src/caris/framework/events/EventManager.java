package caris.framework.events;

import java.util.ArrayList;
import java.util.Arrays;

import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventSubscriber;
import caris.framework.basehandlers.Handler;
import caris.framework.basereactions.Reaction;
import caris.framework.main.Brain;

public class EventManager extends SuperEvent {

	@EventSubscriber
	@Override
	public void onEvent(Event event) {
		ArrayList<Reaction> reactions = new ArrayList<Reaction>();
		ArrayList<Reaction> passiveQueue = new ArrayList<Reaction>();
		for( Handler h : Brain.handlers.values() ) {
			Reaction r = h.handle(event);
			if( r != null ) {
				if( r.priority == -1 ) {
					passiveQueue.add(r);
				} else {
					reactions.add(r);
				}
			}
		}
		if( !reactions.isEmpty() ) {
			Reaction[] options = new Reaction[reactions.size()];
			for( int f=0; f<reactions.size(); f++ ) {
				options[f] = reactions.get(f);
			}
			Arrays.sort(options);
			options[0].run();
		}
		for( Reaction r : passiveQueue ) {
			r.run();
		}
	}
}
