package caris.framework.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import caris.framework.library.Constants;

public class Logger {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat( Constants.DATEFORMAT );
	
	public static void error(String message) {
		error(message, Constants.DEFAULT_INDENT_LEVEL);
	}
	
	public static void error(String message, int level) {
		String output = "";
		for( int f=0; f<level*Constants.DEFAULT_INDENT_INCREMENT; f++ ) {
			output += Constants.ERROR_INDENT;
		}
		output += Constants.HEADER;
		System.out.println(ConsoleColors.RED + output + ConsoleColors.RESET);
		if( Constants.LOG ) {
			log(output);
		}
	}
	
	public static void debug(String message) {
		debug(message, Constants.DEFAULT_INDENT_LEVEL);
	}
	
	public static void debug(String message, int level) {
		if( Constants.DEBUG_VERBOSITY == -1 || Constants.DEBUG_VERBOSITY >= level ) {
			String output = "";
			if( Constants.DEBUG ) {
				for( int f=0; f<level*Constants.DEFAULT_INDENT_INCREMENT; f++ ) {
					output += Constants.DEBUG_INDENT;
				}
				output += Constants.HEADER;
				System.out.println(ConsoleColors.YELLOW + output + ConsoleColors.RESET);
				if( Constants.LOG && ( Constants.LOG_VERBOSITY == -1 || Constants.LOG_VERBOSITY >= level )) {
					log(output);
				}
			}
		}
	}
	
	public static void print(String message) {
		print(message, Constants.DEFAULT_INDENT_LEVEL);
	}
	
	public static void print(String message, int level) {
		if( Constants.PRINT_VERBOSITY == -1 || Constants.PRINT_VERBOSITY >= level ) {
			String output = "";
			if( Constants.PRINT ) {
				for( int f=0; f<level*Constants.DEFAULT_INDENT_INCREMENT; f++ ) {
					output += Constants.PRINT_INDENT;
				}
				output += Constants.HEADER;
				System.out.println(output);
				if( Constants.LOG && ( Constants.LOG_VERBOSITY == -1 || Constants.LOG_VERBOSITY >= level )) {
					log(output);
				}
			}
		}
	}
	
	public static void log(String message) {
		BufferedWriter logWriter;
		try {
			logWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( 
					new File( ( Constants.PREPENDDATE ? sdf.format( Calendar.getInstance().getTime() ) + "_" : "" ) + Constants.LOG_FILE_NAME + Constants.SAVEEXTENTION ) ), Constants.ENCODING));
			logWriter.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}