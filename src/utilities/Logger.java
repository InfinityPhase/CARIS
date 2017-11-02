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
	
	public void toConsole(String message) {
		System.out.println(message);

	}

	public void toLog(String message) {
		if( Constants.LOGFILE ) {
			try {
				logWriter.write(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void out(String message) {
		if( Constants.LOGFILE ) {
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
		if( Constants.DEBUG ) {
			toConsole(message);
			debugLog(message);
		}
	}

}
