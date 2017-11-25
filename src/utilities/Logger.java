package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import library.Constants;

public class Logger {
	SimpleDateFormat sdf = new SimpleDateFormat( Constants.DATEFORMAT );

	BufferedWriter logWriter;

	private int defaultIndent = Constants.DEFAULT_INDENT;
	private int baseIndent = Constants.DEFAULT_BASE_INDENT;
	private String debugHeader = Constants.DEFAULT_HEADER;
	private String defaultIndentString = Constants.INDENT_STRING;
	private boolean defaultShouldAppendTime = Constants.OUTPUT_TIME;
	private boolean defaultShouldAppendLevel = Constants.OUTPUT_TYPE;
	private boolean defaultShouldIndent = Constants.DEFAULT_SHOULD_INDENT;
	private level defaultLevel = Constants.DEFAULT_LEVEL;
	private output defaultOutput = Constants.DEFUALT_OUTPUT;
	
	// TODO: Logging Features
	// Custom header
	// Custom indent symbol
	// Custom Time Format
	// Multiple data types
	// Colour (If supported)

	// Enum for debug levels
	public enum level {
		DEBUG, INFO, STATUS
	}
	
	// Enumb for possible output locations
	public enum output {
		CONSOLE, FILE, ALL
	}

	/* Builder Variables */

	private String message = "";
	private String indentString = "";
	private int indent;
	private boolean shouldIndent;
	private boolean shouldAppendTime;
	private boolean shouldAppendLevel;
	private level messageLevel;
	private output messageOutput;
	
	private boolean happy = false;

	/* Create the Logger things */

	public Logger() {
		try {
			this.logWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( 
					new File( ( Constants.PREPENDDATE ? sdf.format( Calendar.getInstance().getTime() ) + "_" : "" ) + Constants.LOG_FILE_NAME + Constants.SAVEEXTENTION ) ), Constants.ENCODING));
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Change settings

	public Logger setDefaultIndent( int defaultIndent ) {
		this.defaultIndent = defaultIndent;
		return this;
	}

	public Logger setDebugHeader( String debugHeader ) {
		this.debugHeader = debugHeader;
		return this;
	}

	public Logger setIndentString( String defaultIndentString ) {
		this.defaultIndentString = defaultIndentString;
		return this;
	}
	
	public Logger setDefaultShouldIndent( boolean shouldIndent ) {
		this.defaultShouldIndent = shouldIndent;
		return this;
	}

	public Logger setDefaultShouldAppendTime( boolean defaultShouldAppendTime ) {
		this.defaultShouldAppendTime = defaultShouldAppendTime;
		return this;
	}

	public Logger setDefaultShouldAppendLevel( boolean defaultShouldAppendLevel ) {
		this.defaultShouldAppendLevel = defaultShouldAppendLevel;
		return this;
	}

	public Logger setDefaultLevel( level defaultLevel ) {
		this.defaultLevel = defaultLevel;
		return this;
	}

	public Logger build() {
		reset();
		return this;
	}

	// Use the logger that has been created
	// All options override the defaults

	public Logger level( level messageLevel ) {
		this.messageLevel = messageLevel;
		return this;
	}

	public Logger indent( int indent ) {
		this.indent = indent;
		return this;
	}
	
	public Logger indentString( String indentString ) {
		this.indentString = indentString;
		return this;
	}

	public Logger happy( boolean happy ) {
		this.happy = happy;
		return this;
	}
	
	// Diffrent accepted types to log
	
	public void log( String message ) {
		// Maybe combine with the last thing, log()
		this.message = message;
		log();
	}
	
	public void log( boolean message ) {
		log( "" + message );
	}
	
	public void log( int message ) {
		log( "" + message );
	}
	
	public void log( long message ) {
		log( "" + message );
	}
	
	// Actually do the thing

	public void log() {
		if( shouldIndent ) {
			message = multiplyString( indentString, indent + baseIndent ) + debugHeader + " " + message;
		}
		
		if( happy ) {
			message.concat(" :)");
		}
		
		switch( messageLevel ) {
			case DEBUG:
				debug( message );
				break;
			case INFO:
				info( message );
				break;
			case STATUS:
				status( message );
				break;
			default:
				// WTF? YOu shouldn't be here...
				break;
		}

		reset();
	}

	// Send messages places
	
	private void debug( String message ) {
		if( shouldAppendLevel ) {
			message = "[DEBUG] " + message;
		}
		
		if( shouldAppendTime ) {
			message = appendTime( message );
		}
		
		switch( messageOutput ) {
			case ALL:
				all( message );
				break;
			case FILE:
				file( message );
				break;
			case CONSOLE:
				console( message );
				break;
		}
	}

	private void status( String message ) {
		if( shouldAppendLevel ) {
			message = "[STATUS] " + message;
		}
		
		if( shouldAppendTime ) {
			message = appendTime( message );
		}
		
		switch( messageOutput ) {
			case ALL:
				all( message );
				break;
			case FILE:
				file( message );
				break;
			case CONSOLE:
				console( message );
				break;
		}
	}

	private void info( String message ) {
		if( shouldAppendLevel ) {
			message = "[INFO] " + message;
		}
		
		if( shouldAppendTime ) {
			message = appendTime( message );
		}
		
		switch( messageOutput ) {
			case ALL:
				all( message );
				break;
			case FILE:
				file( message );
				break;
			case CONSOLE:
				console( message );
				break;
		}
	}
	
	// Places to send messages
	
	private void file( String message ) {
		try {
			logWriter.write( message );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void console( String message ) {
		System.out.println( message );
	}
	
	private void all( String message ) {
		file( message );
		console( message );
	}

	/* Utilities */
	private String multiplyString( String str, int times ) {
		// Creates a string of length 'times' full of "\0", and replaces them all with 'str'
		String sb = new String( new char[ times ] ).replace( "\0", str );
		return sb.toString();
	}

	private void reset() {
		// NOTE: Remember to keep updated
		message = "";
		indentString = defaultIndentString;
		messageLevel = defaultLevel;
		messageOutput = defaultOutput;
		indent = defaultIndent;
		shouldIndent = defaultShouldIndent;
		shouldAppendTime = defaultShouldAppendTime;
		shouldAppendLevel = defaultShouldAppendLevel;
		
		happy = false;
	}

	private String appendTime( String message ) {
		return "[" + sdf.format( Constants.DATEFORMAT ) + "] " + message;
	}
	
}
