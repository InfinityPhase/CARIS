package modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;
import tokens.Response;
import utilities.Logger;

public class Module {
	// The base class of any module
	// Use for modules that do not depend on user interaction
	
	public Module() {
		
	}

	protected enum Setup {
		UNSET,
		TOKEN,
		MESSAGE
	};

	public enum Status {
		ENABLED,
		DISABLED,
		ADMIN,
		LIMITED
	};

	public enum Avalibility {
		// Alters how this module responds to blacklists, and other control lists
		STANDARD, // Respects blacklists and whitelists
		ALWAYS // Can never be blocked
	};

	public enum Access {
		PUBLIC,
		ADMIN,
		DEVELOPER
	}
	
	public Status status = Status.DISABLED; // If you don't set this, then don't even try to work
	public Avalibility avalibility = Avalibility.STANDARD; // This should almost always be used
	public String name;
	public String help = "This module does not have a help document attached.\n" + 
			"Please contact the maintainers so that they will do their job, and add more information for me.";
	protected Setup setupType = Setup.UNSET;
	protected List<IChannel> recipient;

	protected String response;
	protected List<EmbedBuilder> embed;
	
	protected Logger log = new Logger().setDefaultIndent( 0 ).setBaseIndent( 2 ).build();
	
	// General purpose processer
	
	public Response process() {
		return build();
	}
	
	// Setup variables
	
	protected void setup() {
		response = "";
		recipient = new ArrayList<>();
		embed = new ArrayList<>();
	}
	
	// Build and send the message
	
	protected Response build() {
		if( embed.isEmpty() ) {
			return buildResponse();
		} else {
			return buildEmbed();
		}
	}

	protected Response buildResponse() {
		if( recipient.isEmpty() ) {
			return new Response(response, getPriority());
		} else {
			return new Response(response, getPriority(), recipient, true);
		}
	}
	protected Response buildEmbed() {
		if( recipient.isEmpty() ) {
			return new Response(embed, getPriority());
		} else {
			return new Response(embed, getPriority(), recipient, true);
		}
	}
	
	protected int getPriority() {
		return 0;
	}
	
	// Useful utilities
	
	protected Boolean containsIgnoreCase(Set<String> a, String b) {
		for( String token : a ) {
			if( containsIgnoreCase(token, b) ) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean hasIgnoreCase(ArrayList<String> a, String b) {
		for( String token: a ) {
			if( token.equalsIgnoreCase(b) ) {
				return true;
			}
		}
		return false;
	}

	protected boolean hasIgnoreCase(String[] a, String b) {
		for( String token: a ) {
			if( token.equalsIgnoreCase(b) ) {
				return true;
			}
		}
		return false;
	}

	protected boolean hasIgnoreCase(Set<String> a, String b) {
		for( String token: a ) {
			if( token.equalsIgnoreCase(b) ) {
				return true;
			}
		}
		return false;
	}

	protected boolean containsIgnoreCase(String a, String b) {
		return a.toLowerCase().contains(b.toLowerCase());
	}

	protected boolean containsIgnoreCase(ArrayList<String> a, String b) {
		for( String token : a ) {
			if( containsIgnoreCase(token, b) ) {
				return true;
			}
		}
		return false;
	}

	protected boolean containsIgnoreCase(String[] a, String b) {
		for( String token : a ) {
			if( containsIgnoreCase(token, b) ) {
				return true;
			}
		}
		return false;
	}

}
