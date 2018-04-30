package utilities;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import exceptions.InequalCollumnsValues;
import library.Constants;

public class Database {

	String name;
	Connection connection = null;
	Statement statement = null;
	PreparedStatement prep = null;

	public static SimpleDateFormat sdf = new SimpleDateFormat( Constants.DATEFORMAT );

	public Database() {
		this( sdf.format( Calendar.getInstance().getTime() ) );
	}

	public Database( String name ) {
		this.name = name;

		try {
			if( !Constants.Database.USE_MEMORY_DATABASE ) {
				Class.forName("org.sqlite.JDBC"); // Do i need this?
				connection = DriverManager.getConnection( name );

			} else {
				connection = DriverManager.getConnection( Constants.Database.MEMORY_DATABASE );
			}

			statement = connection.createStatement();
			statement.setQueryTimeout( Constants.Database.DEFAULT_SQL_TIMEOUT );  // set timeout to 30 sec.
		} catch( SQLException e ) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/* General Utils for Database stuff */

	public void setQueryTimeout() {
		/* Resets back to default */
		setQueryTimeout( Constants.Database.DEFAULT_SQL_TIMEOUT );
	}

	public void setQueryTimeout( int time ) {
		/* Change from default */
		try {
			statement.setQueryTimeout( time );
			prep.setQueryTimeout( time ); // NOTE: Due to a bug, the global PreparedStatement is not used.
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
		backup( "backup to " + Constants.Database.BACKUP_DATABASE  );
	}

	public void backup( String fileName ) {
		try {
			statement.executeUpdate( "backup to " + fileName  );
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void restore() {
		restore( Constants.Database.BACKUP_DATABASE );
	}

	public void restore( String name ) {
		/* Load database, useful for in-memory databases */
		try {
			statement.executeUpdate( "restore from " + name );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			connection.close();
			prep.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* Set Pragma Values */

	public void setPragma( String name, boolean value ) {
		PreparedStatement prep = null;
		try {
			prep = connection.prepareStatement("PRAGMA ? = ?;");
			prep.setString(1, name);
			prep.setString(2, ( value ? "ON" : "OFF" ));
			prep.executeUpdate();
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void setPragma( String name, int value ) {
		PreparedStatement prep = null;
		try {
			prep = connection.prepareStatement("PRAGMA ? = ?;");
			prep.setString(1, name);
			prep.setInt(2, value);
			prep.executeUpdate();
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	/* No security on this, beware */ 

	public void rawUpdate( String update ) {
		/* Runs a raw update */
		try {
			statement.executeUpdate( update );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	/* INSERTS */

	public void insert( String table, String[] collumns, Object[] values ) throws InequalCollumnsValues {
		if( collumns.length != values.length ) {
			throw new InequalCollumnsValues( table, collumns.length, values.length );
		}

		PreparedStatement prep = null;
		StringBuilder collumnQ = new StringBuilder();
		StringBuilder valueQ = new StringBuilder();

		// Hackery
		collumnQ.append("?");
		for( int i = 1; i < collumns.length; i++ ) {
			collumnQ.append(", ?");
		}
		
		// Continuing the hackery
		valueQ.append("?");
		for( int i = 1; i < values.length; i++ ) {
			valueQ.append(", ?");
		}

		try {
			prep = connection.prepareStatement("INSERT INTO ? ( " + collumnQ.toString() + " )VALUES ( " + valueQ.toString() + " );");
			prep.setString(1, table);

			// Even more hackery here
			for( int i = 0; i < collumns.length; i++ ) {
				prep.setObject((i+1), collumns[i]);
			}
			
			// When will the hackery end...
			for( int i = 0; i < values.length; i++ ) {
				prep.setObject((i+collumns.length+1), values[i]);
			}

			prep.executeQuery();
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void insert( String table, String collumn, Object value ) throws InequalCollumnsValues {
		insert( table, new String[]{ collumn }, new Object[]{ value } );
	}

	public void insert( String table, Object[] values ) {
		PreparedStatement prep = null;
		StringBuilder sb = new StringBuilder();

		// Hackery
		sb.append("?");
		for( int i = 1; i < values.length; i++ ) {
			sb.append(", ?");
		}

		try {
			prep = connection.prepareStatement("INSERT INTO ? VALUES ( " + sb.toString() + " );");
			prep.setString(1, table);

			// More hackery
			for( int i = 0; i < values.length; i++ ) {
				prep.setObject(i+1, values[i]);
			}

			prep.executeQuery();
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void insert( String table, Object value ) {
		insert( table, new Object[]{ value } );
	}

}
