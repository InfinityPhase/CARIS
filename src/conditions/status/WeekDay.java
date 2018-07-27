package conditions.status;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeekDay implements StatusCondition {
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
