package tokens;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

public class Response implements Comparable<Response> {
	// Potential responses
	public String text;
	public IMessage message;
	public int priority;
	public EmbedBuilder builder;
	public boolean embed;
	public IChannel recipient;
	public boolean proxy;
	
	
	public Response(String text, int priority) {
		this(text, priority, null, false);
	}
	
	public Response(IMessage m, int priority) {
		this(m, priority, null, false);
	}
	
	public Response( EmbedBuilder builder, int priority ) {
		this(builder, priority, null, false);
	}
	
	public Response(String text, int priority, IChannel recipient, boolean proxy) {
		this.text = text;
		this.priority = priority;
		this.message = null;
		this.builder = new EmbedBuilder();
		this.recipient = recipient;
		this.proxy = proxy;
		embed = false;
	}
	
	public Response(IMessage m, int priority, IChannel recipient, boolean proxy ) {
		this.text = m.getContent();
		this.priority = priority;
		this.message = m;
		this.builder = new EmbedBuilder();
		this.recipient = recipient;
		this.proxy = proxy;
		embed = false;
	}
	
	public Response( EmbedBuilder builder, int priority, IChannel recipient, boolean proxy ) {
		this.text = "";
		this.priority = priority;
		this.message = null;
		this.builder = builder;
		this.recipient = recipient;
		this.proxy = proxy;
		embed = true;
	}
	
	@Override
	public int compareTo(Response r) { // sort with higher priority first
		int compare = r.priority;
		if( this.priority < compare ) {
			return 1;
		} else if( this.priority > compare ) {
			return -1;
		} else {
			return 0;
		}
	}
}
