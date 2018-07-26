package conditions;

import java.time.DayOfWeek;
import java.time.LocalDate;

import modules.independent.Independent;

public class WeekDay implements Independent.Condition {
	private DayOfWeek activeDay;
	
	public WeekDay( DayOfWeek activeDay ) {
		this.activeDay = activeDay;
	}

	@Override
	public boolean check() {
		return LocalDate.now().getDayOfWeek().equals( activeDay );
	}

	@Override
	public void reset() {}
}
