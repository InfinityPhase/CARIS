package conditions.status;

public interface StatusCondition {	
	public boolean check();
	public void reset(); // For repeating conditions
}