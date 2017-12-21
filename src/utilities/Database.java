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

	File name;
	Connection connection = null;
	Statement statement = null;

	public static SimpleDateFormat sdf = new SimpleDateFormat( Constants.DATEFORMAT );

	public Database() {
		this( sdf.format( Calendar.getInstance().getTime() ) );
	}

	public Database( String name ) {
		this( new File( name ) );
	}

	public Database( File name ) {
		/* Don't do any loading, just set variables */

		this.name = name;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			statement = connection.createStatement();
			statement.setQueryTimeout( Constants.DEFAULT_SQL_TIMEOUT );  // set timeout to 30 sec.
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

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
		return ( name.exists() && !name.isDirectory() );
	}

	public List<Object> convert( ResultSet input, String query ) {
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
					if( result.get( query.get( i ) ) == null ) {
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
			statement.executeUpdate( "drop table if exists " + table );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

}
