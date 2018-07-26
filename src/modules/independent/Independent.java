package modules.independent;

import java.util.ArrayList;
import java.util.List;

import modules.Module;
import tokens.Response;

public class Independent extends Module {
	List<Condition> conditions;

	public Independent() {
		conditions = new ArrayList<>();
	}

	public interface Condition {	
		public boolean check();
		public void reset(); // For repeating conditions
	}

	public Response process() {
		if( checkConditions() ) {
			setup();
			run();
		}
		
		return build();
	}

	protected void run() {
		// Skips the check
	}

	protected boolean checkConditions() {
		for( Condition c : conditions ) {
			if( !c.check() ) {
				return false;
			}
		}

		return true;
	}
}
