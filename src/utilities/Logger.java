package utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import library.Constants;
import main.Brain;

public class Logger {
	SimpleDateFormat sdf = new SimpleDateFormat( Constants.DATEFORMAT );

	private final String DEBUG_HEADER = ">";
	private final String DEBUG_INDENT = "-";
	private final boolean INDENT_FILE = true;
	private final boolean INDENT_CONSOLE = true;

	public Logger() {
		Brain.files.newWriter( "logWriter", ( Constants.PREPENDDATE ? sdf.format( Calendar.getInstance().getTime() ) + "_" : "" ) + Constants.LOG_FILE_NAME + Constants.SAVEEXTENTION, Constants.ENCODING );
		// Sigh. Still needs fixing.
		// Later will be used to split output. For now, is useless
		Brain.files.copyWriter( "logWriter", "debugWriter" );
	}

	public Logger(boolean happy) {
		Brain.files.newWriter( "logWriter", ( ( Constants.PREPENDDATE ? sdf.format( Calendar.getInstance().getTime() ) + "_" : "" ) + Constants.LOG_FILE_NAME + Constants.SAVEEXTENTION ), Constants.ENCODING );
		// Sigh. Still needs fixing.
		// Later will be used to split output. For now, is useless
		Brain.files.copyWriter( "logWriter", "debugWriter" );

		if( happy ) {
			toConsole("================");
			toConsole(" :] :) :P ^^ ");
			toConsole("================");
		}
	}

	public void toConsole(String message) {
		System.out.println(message);

	}

	public void toLog(String message) {
		if( Constants.LOG_FILE ) {
			Brain.files.write( "logWriter", message );
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
			Brain.files.write( "debugOut", message );

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
		// Well, we are trying it now.
		String sb = new String( new char[ times ] ).replace( "\0", str );
		/*StringBuilder sb = new StringBuilder();
		for( int i = 0; i < times; i++ ) {
			sb.append( str );
		}*/

		return sb.toString();
	}

}
