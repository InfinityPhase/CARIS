package utilities;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.Constants;

public class Database {
	/* Wrapper for whatever it is y'all need to do */

	/* TODO
	 * Add logging.
	 * Add array accepting functions
	 */

	/* A note: This trusts that your querys are entirely correct, 
	 * so errors should be non existent. Be careful.
	 */

	String name;
	Connection connection = null;
	Statement statement = null;

	public static SimpleDateFormat sdf = new SimpleDateFormat( Constants.DATEFORMAT );

	public Database() {
		this( sdf.format( Calendar.getInstance().getTime() ) );
	}

	public Database( String name ) {

		this.name = name;

		try {
			if( !Constants.USE_MEMORY_DATABASE ) {
				Class.forName("org.sqlite.JDBC"); // Do i need this?
				connection = DriverManager.getConnection( name );

			} else {
				connection = DriverManager.getConnection( Constants.MEMORY_DATABASE );
			}

			statement = connection.createStatement();
			statement.setQueryTimeout( Constants.DEFAULT_SQL_TIMEOUT );  // set timeout to 30 sec.
		} catch( SQLException e ) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/* General Utils for Database stuff */

	public void setQueryTimeout() {
		/* Resets back to default */
		setQueryTimeout( Constants.DEFAULT_SQL_TIMEOUT );
	}

	public void setQueryTimeout( int time ) {
		/* Change from default */
		try {
			statement.setQueryTimeout( time );
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean exists() {
		/* Probably not needed, but whatever */
		File name_file = new File( this.name );
		return ( name_file.exists() && !name_file.isDirectory() );
	}

	public void backup() {
		/* Useful if using a memory database */
		try {
			statement.executeUpdate( "backup to " + Constants.BACKUP_DATABASE  );
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void restore() {
		restore( Constants.BACKUP_DATABASE );
	}

	public void restore( String name ) {
		/* Load database, useful for in-memory databases */
		try {
			statement.executeUpdate( "restore from " + name );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public List<Object> convert( ResultSet input, String query ) {
		/* Takes a single query and gives all results as list */
		List<Object> result = new ArrayList<Object>();

		try {
			while( input.next() ) {
				result.add( input.getObject( query ) );
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Map< String, List<Object> > convert( ResultSet input, List<String> query ){
		return convert( input, query.toArray( new String[ query.size() ] ) );
	}
	
	public Map< String, List<Object> > convert( ResultSet input, String[] query ) {
		/* Gives the contents of the resultset as a map holding an arraylist of values for each query */
		Map< String, List<Object> > result = new HashMap< String, List<Object> >();

		try {
			while( input.next() ) {
				for( int i = 0; i < query.length; i++ ) {
					if( result.get( query[i] ) == null ) { // Replace with putIfAbsent
						result.put( query[i], new ArrayList<Object>() );
					}
					result.get( query[i] ).add( input.getObject( i ) );
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/* Actually perform shit on this stupid database */
	/* Sorry for the anger. Blame stress */

	public ResultSet getPragma( String name ) {
		try {
			return statement.executeQuery( "PRAGMA " + name + ";" );
		} catch( SQLException e ) {
			e.printStackTrace();
		}

		return null;
	}

	public void setPragma( String name, boolean value ) {
		try {
			statement.executeUpdate( "PRAGMA " + name + " = " + ( value ? "ON" : "OFF" ) + ";" );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void setPragma( String name, int value ) {
		try {
			statement.executeUpdate( "PRAGMA " + name + " = " + value + ";" );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void setPragma( Map<String, Boolean> values ) {
		/* I wish I could make this one command, for efficent access */
		for( String s : values.keySet() ) {
			try {
				statement.executeUpdate( "PRAGMA " + s + " = " + ( values.get(s) ? "ON" : "OFF" ) + ";" );
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
	}

	public void rawUpdate( String update ) {
		/* Runs a raw update */
		try {
			statement.executeUpdate( update );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("serial")
	public void update( String table, Map<String, String> values, String condition ) {
		update( table, values, new ArrayList<String>() {{ add(condition); }} ); // Becuase I am really lazy. Probably pretty inefficent, though.
	}

	public void update( String table, Map<String, String> values, List<String> conditions ) {
		/*
		UPDATE table_name
		SET column1 = value1, column2 = value2...., columnN = valueN
		WHERE [condition];
		 */

		String condition = "";
		String value = "";

		for( String s : conditions ) {
			condition = condition + ", " + s;
		}

		for( String k : values.keySet() ) {
			value = value + ", " + k + " = " + values.get(k);
		}

		try {
			statement.executeUpdate( "UPDATE " + table + " SET " + value.substring(2) + " WHERE " + condition.substring(2) + ";" );
		} catch (SQLException e) {
			System.out.println("");
			e.printStackTrace();
		}
	}

	public void update( String table, Map<String, String> values ) {
		/* Ignores the WHERE clause */

		String value = "";

		for( String k : values.keySet() ) {
			value = value + ", " + k + " = " + values.get(k);
		}

		try {
			statement.executeUpdate( "UPDATE " + table + " SET " + value.substring(2) + ";" );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void insert( String table, List<String> collumnOrder, List<String> data ) {
		String collumnOrder_string = "";
		String data_string = "";

		for( String s : collumnOrder ) {
			collumnOrder_string = collumnOrder_string + ", " + s;
		}

		for( String s : data ) {
			data_string = data_string + ", " + s;
		}

		try {
			statement.executeUpdate( "INSERT INTO " + table + " (" + collumnOrder_string.substring(2) + ") VALUES " + data_string + ";");
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void insert( String table, String[] collumnOrder, String[] data ) {
		String collumnOrder_string = "";
		String data_string = "";

		for( String s : collumnOrder ) {
			collumnOrder_string = collumnOrder_string + ", " + s;	
		}

		for( String s : data ) {
			data_string = data_string + ", " + s;	
		}

		try {
			statement.executeUpdate( "INSERT INTO " + table + " (" + collumnOrder_string.substring(2) + ") VALUES " + data_string + ";");
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void insert( String table, List<String> data ) {
		insert( table, data.toArray( new String[ data.size() ] ) );
	}

	public void insert( String table, String[] data ) {
		/* Uses default table collumn order */		
		String data_string = "";

		for( String s : data ) {
			data_string = data_string + ", " + s;
		}

		try {
			statement.executeUpdate( "INSERT INTO " + table + " VALUES " + data_string.substring(2) + ";" );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void insert( String table, String collumnOrder, String data ) {
		/* We all like strings, right? Less handholding than the lists */
		try {
			statement.executeUpdate( "INSERT INTO " + table + " (" + collumnOrder + ") VALUES " + data + ";" );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void insert( String table, String data ) {
		/* For tables with two collumns, ID and what they store
		 * Usually represent Lists. Convience, nothing more.
		 */

		try {
			statement.executeUpdate( "INSERT INTO " + table + " VALUES " + data + ";" );
		} catch( SQLException e ) {
			e.printStackTrace();
		}

	}
	
	public void insertReplace( String table, List<String> collumnOrder, List<String> data ) {
		/* Woo, replaces on an error */
		String collumnOrder_string = "";
		String data_string = "";

		for( String s : collumnOrder ) {
			collumnOrder_string = collumnOrder_string + ", " + s;
		}

		for( String s : data ) {
			data_string = data_string + ", " + s;
		}

		try {
			statement.executeUpdate( "INSERT OR REPLACE INTO " + table + " (" + collumnOrder_string.substring(2) + ") VALUES " + data_string + ";");
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void insertReplace( String table, String[] collumnOrder, String[] data ) {
		String collumnOrder_string = "";
		String data_string = "";

		for( String s : collumnOrder ) {
			collumnOrder_string = collumnOrder_string + ", " + s;	
		}

		for( String s : data ) {
			data_string = data_string + ", " + s;	
		}

		try {
			statement.executeUpdate( "INSERT OR REPLACE INTO " + table + " (" + collumnOrder_string.substring(2) + ") VALUES " + data_string + ";");
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void insertReplace( String table, List<String> data ) {
		insert( table, data.toArray( new String[ data.size() ] ) );
	}

	public void insertReplace( String table, String[] data ) {
		/* Uses default table collumn order */		
		String data_string = "";

		for( String s : data ) {
			data_string = data_string + ", " + s;
		}

		try {
			statement.executeUpdate( "INSERT OR REPLACE INTO " + table + " VALUES " + data_string.substring(2) + ";" );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void insertReplace( String table, String collumnOrder, String data ) {
		/* We all like strings, right? Less handholding than the lists */
		try {
			statement.executeUpdate( "INSERT OR REPLACE INTO " + table + " (" + collumnOrder + ") VALUES " + data + ";" );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}
	
	public void insertReplace( String table, String data ) {
		/* Notice the OR REPLACE */
		
		try {
			statement.executeUpdate( "INSERT OR REPLACE INTO " + table + " VALUES " + data + ";" );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}
	
	public ResultSet query( String query ) {
		/* Get all values from a given search */
		ResultSet rs = null;

		try {
			rs = statement.executeQuery( query );
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rs;
	}

	public void dropTable( String table ) {
		/* Drops the given table if it exists */
		try {
			statement.executeUpdate( "drop table if exists " + table + ";" );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void makeTable( String table ) {
		/* Creates the table if it doesn't exist */
		try {
			statement.executeUpdate( "CREATE TABLE IF NOT EXISTS " + table + ";" );
		} catch( SQLException e ){
			e.printStackTrace();
		}
	}

	public void makeTable( String table, Map<String, String> collumns ) {
		/* Create table with given collumns */
		/* Does not include most needed features. Deprecated before finished... */
		String collumns_string = "";

		for( String name : collumns.keySet() ) {
			collumns_string = collumns_string + ", " + name + " " + collumns.get( name );
		}

		try {
			statement.executeUpdate( "CREATE TABLE IF NOT EXISTS " + table + " (" + collumns_string.substring(2) + ");");
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void makeTable( String table, List<String> collumns ) {
		makeTable( table, collumns.toArray( new String[ collumns.size() ] ) );
	}

	public void makeTable( String table, String[] collumns ) {
		String collumns_string = "";

		for( String s : collumns ) {
			collumns_string = collumns_string + ", " + s;
		}

		try {
			statement.executeUpdate( "CREATE TABLE IF NOT EXISTS " + table + " (" + collumns_string.substring(2) + ");" );
		} catch( SQLException e ) {
			System.out.println("CREATE TABLE IF NOT EXISTS " + table + " (" + collumns_string.substring(2) + ");");
			e.printStackTrace();
		}
	}

	public void addCollumn( String table, String collumn ) {
		/* What it says on the tin. Adds a single collumn to a given table */
		try {
			statement.executeUpdate( "ALTER TABLE " + table + " ADD COLLUMN " + collumn + ";" );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}	

	public boolean tableExists( String table ) {
		/* Checks for the existance of a table */
		ResultSet test = null;

		try {
			test = statement.executeQuery( "SELECT * from sqlite_master WHERE name ='" + table +"' and type='table';" );
		} catch( SQLException e ) {
			e.printStackTrace();
		}

		try {
			if( test.next() ) {
				return true; // If the row is not null, it must exist. Therefore, the table exists.
				// Very existential, no?
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

}
