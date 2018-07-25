package utilities;

import java.time.LocalTime;

import modules.independent.Independent;

public final class CommonConditions {
	
	public class TimeDelay implements Independent.Condition {
		private LocalTime time;
		
		public TimeDelay( LocalTime time ) {
			this.time = time;
		}
		
		@Override
		public boolean check() {
			return LocalTime.now().isAfter( time );
		}
		
	}

}
