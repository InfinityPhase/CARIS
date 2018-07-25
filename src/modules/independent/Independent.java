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
	}
	
	public Response process() {
		setup();
		return build();
	}
}
