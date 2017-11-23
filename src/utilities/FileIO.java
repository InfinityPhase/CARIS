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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileIO {
	// TODO: Ability to have name and filename be the same
	// NOTE: Checking for the existence of a writer/reader
	// is the user's responsibility
	
	private static Map< String, BufferedWriter > writers = new HashMap< String, BufferedWriter >();
	private static Map< String, BufferedReader > readers = new HashMap< String, BufferedReader >();
	
	public FileIO() { System.out.println("I AM HERE"); }
	
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
		} catch (FileNotFoundException e) {
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
	
	public void copyWriter( String first, String dest ) {
		writers.put( dest, writers.get( first ) );
	}
	
	public void copyReader( String first, String dest ) {
		readers.put( dest, readers.get( first ) );
	}
	
	// Check if the thing exists
	
	public boolean writerExists( String name ) {
		if( writers.containsKey( name ) ) {
			return true;
		}
		return false;
	}
	
	public boolean readerExists( String name ) {
		if( readers.containsKey( name ) ) {
			return true;
		}
		return false;
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
	
	public String[] readAll( String name ) {
		List<String> output = new ArrayList<String>();
		String line;
		try {
			while( ( line = readers.get( name ).readLine() ) != null ) {
				output.add( line );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return (String[]) output.toArray();
		
	}
	
	public void readReset( String name ) {
		try {
			readers.get( name ).reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Return the raw reader or writer object */
	// Don't know why you would need to, but you can
	
	public BufferedReader getReader( String name ) {
		return readers.get( name );
	}
	
	public BufferedWriter getWriter( String name ) {
		return writers.get( name );
	}
	
}
