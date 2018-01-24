package tokens;

public class NullReminderIDException extends Exception {
	
	public NullReminderIDException() {
		super( "Reminder does not include an ID" );
	}
	
	public NullReminderIDException( String message ) {
		super( message );
	}

}
