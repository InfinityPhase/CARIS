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
	
	private final String DEBUG_HEADER = ">";
	private final String DEBUG_INDENT = "-";
	private final boolean INDENT_FILE = true;
	private final boolean INDENT_CONSOLE = true;

	public Logger() {
		try {
			this.logWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( 
					new File( ( Constants.PREPENDDATE ? sdf.format( Calendar.getInstance().getTime() ) + "_" : "" ) + Constants.LOG_FILE_NAME + Constants.SAVEEXTENTION ) ), Constants.ENCODING));
			// You know what? Shut up.
			this.debugWriter = this.logWriter;
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Logger(boolean happy) {
		try {
			this.logWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( 
					new File( ( Constants.PREPENDDATE ? sdf.format( Calendar.getInstance().getTime() ) + "_" : "" ) + Constants.LOG_FILE_NAME + Constants.SAVEEXTENTION ) ), Constants.ENCODING));
			// You know what? Shut up.
			this.debugWriter = this.logWriter;
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if( happy ) {
			toConsole("============================================================");
			toConsole(" :] :) :P ^^ ");
			toConsole("============================================================");
		}
	}
	
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
	
	public void debugOut( String message, int indent ) {
		// Adds the symbols specified to the beginning of the message
		// Eg. -> if indent = 1
		// > if indent = 0
		// ----> if indent = 4
		
		debugOut( multiplyString( DEBUG_INDENT, indent ) + DEBUG_HEADER + " " + message );
		
		if( Constants.DEBUG ) {
			if( INDENT_CONSOLE ) {
				toConsole( multiplyString( DEBUG_INDENT, indent ) + DEBUG_HEADER + " " + message );
			} else {
			toConsole(message);
			}
			if( INDENT_FILE ) {
				debugLog( multiplyString( DEBUG_INDENT, indent ) + DEBUG_HEADER + " " + message );
			} else {
			debugLog(message);
			}
		}
	}
	
	/* Utilities */
	private String multiplyString( String str, int times ) {
		// Perhaps replace with this:
		// String sb = new String( new char[ times ] ).replace( "\0", str );
		StringBuilder sb = new StringBuilder();
		for( int i = 0; i < times; i++ ) {
			sb.append( str );
		}
		
		return sb.toString();
	}

}
