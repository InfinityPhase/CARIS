package exceptions;

public class InequalCollumnsValues extends Exception {

	public InequalCollumnsValues() {
		super("The number of collumns and the number of values is inequal");
	}

	public InequalCollumnsValues( int collumnCount, int valueCount ) {
		super("The number of collumns (" + collumnCount + " ) does not equal the number of values (" + valueCount + ")");
	}
	
	public InequalCollumnsValues( String table, int collumnCount, int valueCount ) {
		super("The number of collumns (" + collumnCount + " ) does not equal the number of values (" + valueCount + ") in the statement for the table " + table );
	}

}
