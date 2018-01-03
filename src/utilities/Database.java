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
		/* Gives the contents of the resultset as a map holding an arraylist of values for each query */
		Map< String, List<Object> > result = new HashMap< String, List<Object> >();

		try {
			while( input.next() ) {
				for( int i = 0; i < query.size(); i++ ) {
					if( result.get( query.get( i ) ) == null ) { // Replace with putIfAbsent
						result.put( query.get( i ), new ArrayList<Object>() );
					}
					result.get( query.get( i ) ).add( input.getObject( i ) );
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

	public void update( String update ) {
		/* Runs a raw update */
		try {
			statement.executeUpdate( update );
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
		// Because I realized my stupidity
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
	
	// Damn. This is unsupported by SQLite, and must be done during table creation
	/*public void addForeignKey( String table, String key, String refrencedTable, String refrencedKey ) {
		try {
			statement.executeUpdate( "FOREIGN KEY (" + key + ") REFRENCES " + refrencedTable + "(" + refrencedKey + ");" ); // Wrong syntax. Pls fix.
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}*/

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
