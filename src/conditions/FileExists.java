package conditions;

import modules.independent.Independent;
import modules.independent.Independent.Condition;

public class FileExists implements Independent.Condition {
	@Override
	public boolean check() {
		return false;
	}

	@Override
	public void reset() {}
}
