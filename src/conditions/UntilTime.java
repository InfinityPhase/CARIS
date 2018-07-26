package conditions;

import java.time.LocalTime;

import modules.independent.Independent;

public class UntilTime implements Independent.Condition {
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
