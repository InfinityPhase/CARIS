package tokens;

import java.util.ArrayList;

public class LineSet {
	public String line;
	public ArrayList<String> tokens;
	
	public LineSet(String line, ArrayList<String> tokens) {
		this.line = line;
		this.tokens = tokens;
	}
}
