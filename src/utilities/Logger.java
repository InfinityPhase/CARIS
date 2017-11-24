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
	BufferedWriter debugWriter;
	BufferedWriter statusWriter;

	private int defaultIndent = Constants.DEFAULT_INDENT;
	private int baseIndent = Constants.DEFAULT_BASE_INDENT;
	private String debugHeader = Constants.DEFAULT_HEADER;
	private String defaultIndentString = Constants.INDENT_STRING;
	private boolean time = Constants.OUTPUT_TIME;
	private boolean writeType = Constants.OUTPUT_TYPE;
	private boolean defaultShouldIndent = Constants.DEFAULT_SHOULD_INDENT;
	private level defaultLevel = Constants.DEFAULT_LEVEL;

	// Enum for debug levels
	public enum level {
		DEBUG, INFO, STATUS
	}

	/* Builder Variables */

	private String message = "";
	private String indentString = "";
	private int indent;
	private boolean shouldIndent;
	private level messageLevel;

	
	private boolean happy = false;

	/* Create the Logger things */

	public Logger() {
		try {
			this.logWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( 
					new File( ( Constants.PREPENDDATE ? sdf.format( Calendar.getInstance().getTime() ) + "_" : "" ) + Constants.LOG_FILE_NAME + Constants.SAVEEXTENTION ) ), Constants.ENCODING));
			this.debugWriter = this.statusWriter = this.logWriter;
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

	public Logger setTime( boolean time ) {
		this.time = time;
		return this;
	}

	public Logger setWriteType( boolean writeType ) {
		this.writeType = writeType;
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

	public void out( String message ) {
		this.message = message;
	}

	public void level( level messageLevel ) {
		this.messageLevel = messageLevel;
	}

	public void indent( int indent ) {
		this.indent = indent;
	}
	
	public void indentString( String indentString ) {
		this.indentString = indentString;
	}

	public void happy( boolean happy ) {
		this.happy = happy;
	}

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

	// Sned messages places
	private void debug( String message ) {
		
	}

	private void status( String message ) {

	}

	private void info( String message ) {

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
		indent = defaultIndent;
		shouldIndent = defaultShouldIndent;
		
		happy = false;
	}

	private String process( String message ) {
		if( time ) {
			message = "[" + sdf.format( Constants.DATEFORMAT ) + "] " + message;
		}

		if( writeType ) {
			message = "[" + /*The type of log*/ "" + "] " + message;
		}

		return message;
	}

}
