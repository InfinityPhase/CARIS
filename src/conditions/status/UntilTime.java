package conditions.status;

import java.time.LocalTime;

public class UntilTime implements StatusCondition {
	private LocalTime time;
	
	public UntilTime( LocalTime time ) {
		this.time = time;
	}
	
	@Override
	public boolean check() {
		return LocalTime.now().isAfter( time );
	}
	
	@Override
	public void reset() {} // Don't need to reset...
}
