package utilities;

public interface Handler {
	// The base handler interface. Extend this into other classes.
	public String process(String message);
	public int getPriority();
}
