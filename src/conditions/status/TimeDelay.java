package conditions.status;

import java.time.LocalTime;
import java.time.temporal.TemporalAmount;

public class TimeDelay implements StatusCondition {
	private LocalTime previous;
	private TemporalAmount diffrence;
	
	public TimeDelay( TemporalAmount diffrence ) {
		this( diffrence, LocalTime.now() );
	}
	
	public TimeDelay( TemporalAmount diffrence, LocalTime previous ) {
		this.diffrence = diffrence;
		this.previous = previous;
	}
	
	@Override
	public boolean check() {
		return ( previous.plus( diffrence ).isBefore( LocalTime.now() ) );
	}

	@Override
	public void reset() {
		previous = LocalTime.now();
	}
}
