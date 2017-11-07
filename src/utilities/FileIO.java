package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class FileIO {
	// TODO: Ability to have name and filename be the same
	
	private BufferedWriter writer;
	private BufferedReader reader;
	private static Map< String, BufferedWriter > writers = new HashMap< String, BufferedWriter >();
	private static Map< String, BufferedReader > readers = new HashMap< String, BufferedReader >();
	
	// Okay, this is rediculous. They all do something really similar, just diffrent ways.
	// Fucking overloading functions... Just let me set default values!
	
	public void newReader( String name, String fileIn, String encoding ) {
		try {
			readers.put( name, new BufferedReader( new InputStreamReader( new FileInputStream( new File( fileIn ) ), encoding ) ) );
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void newReader( String name, String fileIn ) {
		try {
			readers.put( name, new BufferedReader( new InputStreamReader( new FileInputStream( new File( fileIn ) ) ) ) );
		} catch ( FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void newReader( String name, File fileIn, String encoding ) {
		try {
			readers.put( name, new BufferedReader( new InputStreamReader( new FileInputStream( fileIn ), encoding ) ) );
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void newReader( String name, File fileIn ) {
		try {
			readers.put( name, new BufferedReader( new InputStreamReader( new FileInputStream( fileIn ) ) ) );
		} catch ( FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void newWriter( String name, String fileOut, String encoding ) {
		try {
			writers.put( name, new BufferedWriter( new OutputStreamWriter( new FileOutputStream( new File( fileOut ) ), encoding ) ) );	
		} catch( FileNotFoundException | UnsupportedEncodingException e ) {
			e.printStackTrace();
		}
	}
	
	public void newWriter( String name, String fileOut ) {
		try {
			writers.put( name, new BufferedWriter( new OutputStreamWriter( new FileOutputStream( new File( fileOut ) ) ) ) );	
		} catch( FileNotFoundException e ) {
			e.printStackTrace();
		}
	}
	
	public void newWrter( String name, File fileOut, String encoding ) {
		try {
			writers.put( name, new BufferedWriter( new OutputStreamWriter( new FileOutputStream( fileOut ), encoding ) ) );	
		} catch( FileNotFoundException | UnsupportedEncodingException e ) {
			e.printStackTrace();
		}
	}
	
	public void newWrter( String name, File fileOut ) {
		try {
			writers.put( name, new BufferedWriter( new OutputStreamWriter( new FileOutputStream( fileOut ) ) ) );	
		} catch( FileNotFoundException e ) {
			e.printStackTrace();
		}
	}
	
	// Okay, actually use the things that have been made
	
	public void write( String name, String content ) {
		try {
			writers.get( name ).write( content );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String read( String name ) {
		String output = null;
		try {
			output = readers.get( name ).readLine();
		} catch( IOException e) {
			e.printStackTrace();
		}
		return output;
	}
	
}
