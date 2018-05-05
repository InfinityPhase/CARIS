package utilities;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import library.Constants;

public class Logger {
	DateTimeFormatter fileNameFormat = DateTimeFormatter.ofPattern( Constants.Logger.FILEDATEFORMAT );
	DateTimeFormatter contentTimeFormat = DateTimeFormatter.ofPattern( Constants.Logger.TIMEFORMAT );

	BufferedWriter logWriter;

	private int defaultIndent = Constants.Logger.DEFAULT_INDENT;
	private int baseIndent = Constants.Logger.DEFAULT_BASE_INDENT;
	private String debugHeader = Constants.Logger.DEFAULT_HEADER;
	private String defaultIndentString = Constants.Logger.INDENT_STRING;
	private boolean defaultShouldAppendTime = Constants.Logger.OUTPUT_TIME;
	private boolean defaultShouldAppendLevel = Constants.Logger.OUTPUT_TYPE;
	private boolean defaultShouldIndent = Constants.Logger.DEFAULT_SHOULD_INDENT;
	private level defaultLevel = Constants.Logger.DEFAULT_LEVEL;
	private output defaultOutput = Constants.Logger.DEFUALT_OUTPUT;
	
	// TODO: Logging Features
	// Custom header
	// Custom indent symbol
	// Custom Time Format
	// Multiple data types
	// Colour (If supported)

	// Enum for debug levels
	public enum level {
		DEBUG, INFO, STATUS, ERROR
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
			// We use a verbose version of this, so that we throw an exception if the encoding is bad
			 this.logWriter = new BufferedWriter( new OutputStreamWriter(
				     new FileOutputStream( ( Constants.Logger.PREPENDDATE ? LocalDateTime.now().format( fileNameFormat ) + "_" : "" ) + Constants.Logger.LOG_FILE_NAME + Constants.Logger.SAVEEXTENTION ),
				     Charset.forName( Constants.Logger.ENCODING ).newEncoder() ) );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Change settings

	public Logger setDefaultIndent( int defaultIndent ) {
		this.defaultIndent = defaultIndent;
		return this;
	}
	
	public Logger setBaseIndent( int baseIndent ) {
		this.baseIndent = baseIndent;
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
			message = multiplyString( indentString, (indent + baseIndent) ) + debugHeader + " " + message;
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
			case ERROR:
				error( message );
				break;
			default:
				// WTF? YOu shouldn't be here...
				break;
		}

		reset();
	}
	
	// Flush and/or close the writer
	
	public void flush() {
		try {
			logWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	private void error( String message ) {
		if( shouldAppendLevel ) {
			message = "[ERROR] " + message;
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
			logWriter.newLine();
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
		return "[" + LocalDateTime.now().format( contentTimeFormat ) + "] " + message;
	}
	
}
