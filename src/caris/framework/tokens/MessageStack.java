package caris.framework.tokens;

import java.util.ArrayList;

import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IMessage;

public class MessageStack {
	
	private ArrayList<IMessage> messages;
	
	public MessageStack() {
		messages = new ArrayList<IMessage>();
	}
	
	public void add(IMessage message) {
		messages.add(0, message);
		if( messages.size() > 1000 ) {
			messages.remove(1000);
		}
	}
	
	public ArrayList<IMessage> getQuantity(int quantity) {
		int cleanCount = 0;
		for( IMessage message : messages ) {
			if( message.isDeleted() ) {
				messages.remove(message);
				cleanCount++;
			}
		}
		if( cleanCount > 0 ) {
			Logger.debug("MessageStack cleaned of " + cleanCount + " deleted message(s)", 3);
		}
		ArrayList<IMessage> subList = new ArrayList<IMessage>();
		for( int f=0; f<Math.min(1000, Math.min(quantity, messages.size())); f++ ) {
			subList.add(messages.get(f));
		}
		return subList;
	}
}
