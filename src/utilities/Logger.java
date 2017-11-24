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
	
	private int defaultIndent = Constants.DEFAULT_INDENT;
	private String debugHeader = Constants.DEFAULT_HEADER;
	private String indentString = Constants.INDENT_STRING;
	private boolean time = Constants.OUTPUT_TIME;
	private boolean writeType = Constants.OUTPUT_TYPE;
	private level defaultLevel = Constants.DEFAULT_LEVEL;
	
	// Enum for debug levels
	public enum level {
		DEBUG, INFO, STATUS
	}
	
	/* Builder Variables */
	
	private String message;
	private level messageLevel;
	private int indent;
	private boolean happy;
	
	/* Create the Logger things */
	
	public Logger() {
		try {
			this.logWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( 
					new File( ( Constants.PREPENDDATE ? sdf.format( Calendar.getInstance().getTime() ) + "_" : "" ) + Constants.LOG_FILE_NAME + Constants.SAVEEXTENTION ) ), Constants.ENCODING));
			this.debugWriter = this.logWriter;
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
	
	public Logger setIndentString( String indentString ) {
		this.indentString = indentString;
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
	
	public void happy( boolean happy ) {
		this.happy = happy;
	}

	/* For posterity
	public void debugOut( String message, int indent ) {
		// Adds the symbols specified to the beginning of the message
		// Eg. -> if indent = 1
		// > if indent = 0
		// ----> if indent = 4

		if( Constants.DEBUG ) {
			if( Constants.INDENT_CONSOLE ) {
				toConsole( multiplyString( indentString, indent + defaultIndent ) + debugHeader + " " + message );
			} else {
				toConsole(message);
			}
			if( Constants.INDENT_FILE ) {
				debugLog( multiplyString( indentString, indent + defaultIndent ) + debugHeader + " " + message );
			} else {
				debugLog(message);
			}
		}
	}
	*/
	
	/* Utilities */
	private String multiplyString( String str, int times ) {
		// Creates a string of length 'times' full of "\0", and replaces them all with 'str'
		String sb = new String( new char[ times ] ).replace( "\0", str );
		return sb.toString();
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
