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
	private boolean time = Constants.OUTPUT_TIME;
	private boolean writeType = Constants.OUTPUT_TYPE;

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
	
	void setDefaultIndent( int defaultIndent ) {
		this.defaultIndent = defaultIndent;
	}
	
	void setDebugHeader( String debugHeader ) {
		this.debugHeader = debugHeader;
	}
	
	void setTime( boolean time ) {
		this.time = time;
	}
	
	void setWriteType( boolean writeType ) {
		this.writeType = writeType;
	}
	
	// Use the logger that has been created
	
	public void toConsole(String message) {
		System.out.println(message);

	}

	public void toLog(String message) {
		if( Constants.LOG_FILE ) {
			try {
				logWriter.write(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void out(String message) {
		if( Constants.LOG_FILE ) {
			toLog(message);
		}
		toConsole(message);
	}


	public void debugLog(String message) {
		if( Constants.DEBUG_FILE ) {
			try {
				debugWriter.write(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void debugOut(String message) {
		debugOut( message, 0 );
	}

	public void debugOut( int message ) {
		debugOut( message + "" );
	}

	public void debugOut( int message, int indent ) {
		debugOut( message + "", indent );
	}
	
	public void debugOut( long message ) {
		debugOut( message + "" );
	}
	
	public void debugOut( long message, int indent ) {
		debugOut( message + "", indent );
	}

	public void debugOut( boolean message ) {
		debugOut( message + "" );
	}

	public void debugOut( boolean message, int indent ) {
		debugOut( message + "", indent );
	}

	public void debugOut( String message, int indent ) {
		// Adds the symbols specified to the beginning of the message
		// Eg. -> if indent = 1
		// > if indent = 0
		// ----> if indent = 4

		// For the lols:

		/* I AM A FUCKING IDIOT.
			So, what happened is this:
			This class called the debugOut( String message) class.
			Then, that class called this class, giving it an indent.
			Cue recursive calls appending to the same stupid string ad infinium
		 */
		//debugOut( multiplyString( DEBUG_INDENT, indent ) + DEBUG_HEADER + " " + message );

		if( Constants.DEBUG ) {
			if( Constants.INDENT_CONSOLE ) {
				toConsole( multiplyString( debugHeader, indent + defaultIndent ) + debugHeader + " " + message );
			} else {
				toConsole(message);
			}
			if( Constants.INDENT_FILE ) {
				debugLog( multiplyString( debugHeader, indent + defaultIndent ) + debugHeader + " " + message );
			} else {
				debugLog(message);
			}
		}
	}

	/* Utilities */
	private String multiplyString( String str, int times ) {
		// Creates a string of length 'times' full of "\0", and replaces them all with 'str'
		String sb = new String( new char[ times ] ).replace( "\0", str );
		return sb.toString();
	}

}
