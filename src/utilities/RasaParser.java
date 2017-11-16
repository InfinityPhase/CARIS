package utilities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

public class RasaParser {
	private Map< String, JSONObject > messageResponses = new HashMap< String, JSONObject >();
	
	final String rasaURL = "http://localhost:5000/parse";
	HttpURLConnection rasa;
	
	public RasaParser() {}
	
	private HttpURLConnection setup( String urlName ) {
		try {
			URL url = new URL( urlName );
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput( true );
			connection.setDoOutput( true );
			connection.setRequestMethod( "POST" );
			
			return connection;
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void disable( HttpURLConnection connection ) {
		connection.disconnect();
	}
	
	public void process( String message ) {
		rasa = setup(rasaURL);
		rasa.setRequestProperty( "q", message );
		try {
			messageResponses.put( message,  new JSONObject( rasa.getResponseMessage() ) );
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		disable(rasa);
	}
	
	public void process( IMessage message ) {
		process( message.getContent() );
	}
	
	public void process( MessageReceivedEvent event ) {
		process( event.getMessage() );
	}
	
	public String getIntent(String message) {
		String intent = null;
		
		if( !messageResponses.containsKey( message ) ) {
			process( message );
		}
		
		intent = messageResponses.get( message ).getString( "name" );
		
		return intent;
	}
	
	public String getIntent(IMessage message) {
		return getIntent( message.getContent() );
	}
	
	public String getIntent( MessageReceivedEvent event ) {
		return getIntent( event.getMessage() );
	}
	
	public String getEntity(String message) {
		// TODO: THIS NEEDS TO BE FIXED
		String entity = null;
		
		if ( !messageResponses.containsKey( message ) ) {
			process( message );
		}
		
		entity = messageResponses.get( message ).getString( "entities" );
		
		return entity;
	}
	
	public String getEntity(IMessage message) {
		return getEntity( message.getContent() );
	}
	
	public String getEntity( MessageReceivedEvent event ) {
		return getEntity( event.getMessage() );
	}
	
	public int getConfidence( String message ) {		
		@SuppressWarnings("null")
		int confidence = (Integer) null;
		
		if( !messageResponses.containsKey( message ) ) {
			process( message );
		}
		
		confidence = messageResponses.get( message ).getInt( "confidence" );
		
		return confidence;
	}
	
	public int getConfidence( IMessage message ) {
		return getConfidence( message.getContent() );
	}
	
	public int getConfidence( MessageReceivedEvent event ) {
		return getConfidence( event.getMessage() );
	}
	
	public JSONObject getAll(String message) {
		// This will return the raw json		
		if( !messageResponses.containsKey( message ) ) {
			process( message );
		}
		
		return messageResponses.get( message );
	}
	
	public JSONObject getAll( IMessage message ) {
		return getAll( message.getContent() );
	}
	
	public JSONObject getAll( MessageReceivedEvent event ) {
		return getAll( event.getMessage() );
	}
	
	/* Temporary Processing */
	
	public JSONObject processTemp( String message ) {
		rasa.setRequestProperty( "q", message );
		
		try {
			return new JSONObject( rasa.getResponseMessage() );
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public JSONObject processTemp( IMessage message ) {
		return processTemp( message.getContent() );
	}
	
	public String getIntentTemp(String message) {
		String intent = null;

		intent = processTemp( message ).getString( "name" );
		return intent;
	}
	
	public String getIntentTemp(IMessage message) {
		return getIntent( message.getContent() );
	}
	
	public String getEntityTemp(String message) {
		// TODO: THIS NEEDS TO BE FIXED
		String entity = null;
		
		entity = processTemp( message ).getString( "entities" );
		
		return entity;
	}
	
	public String getEntityTemp(IMessage message) {
		return getEntity( message.getContent() );
	}
	
	public int getConfidenceTemp( String message ) {		
		@SuppressWarnings("null")
		int confidence = (Integer) null;

		confidence = processTemp( message ).getInt( "confidence" );
		
		return confidence;
	}
	
	public int getConfidenceTemp( IMessage message ) {
		return getConfidence( message.getContent() );
	}
	
	public JSONObject getAllTemp(String message) {
		// This will return the raw json		
		
		return processTemp( message );
	}
	
	public JSONObject getAllTemp( IMessage message ) {
		// This will return the raw json
		return getAll( message.getContent() );
	}
}
