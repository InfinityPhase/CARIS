package conditions.status;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;


public class RunTimesPerWeek implements StatusCondition {
	private static final TemporalUnit truncateUnit = ChronoUnit.MINUTES; // Resolution of reset
	
	private int times;
	private int currentCount;
	private DayOfWeek resetDay;
	private LocalTime resetTime;
	
	public RunTimesPerWeek( int times, DayOfWeek resetDay ) {
		this( times, resetDay, LocalTime.of(0, 0) );
	}

	public RunTimesPerWeek( int times, DayOfWeek resetDay, LocalTime resetTime ) {
		this.times = times;
		this.currentCount = 0;
		this.resetDay = resetDay;
		this.resetTime = resetTime.truncatedTo( truncateUnit );
	}

	@Override
	public boolean check() {
		if( LocalDate.now().getDayOfWeek().equals( resetDay ) && LocalTime.now().truncatedTo(truncateUnit).equals( resetTime ) ) {
			reset();
		}
		
		if( currentCount >= times ) {
			return false;
		}
		
		return true;
	}

	@Override
	public void reset() {
		currentCount = 0;
	}

}
