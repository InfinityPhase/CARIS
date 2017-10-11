package utilities;

import java.util.ArrayList;

public class TokenParser {
	
	public TokenParser() {}
	
	public ArrayList<String> parse(String line) {
		ArrayList<String> tokens = new ArrayList<String>();
		line += " ";
		while( line.contains("  ") ) {
			line.replace("  ", " ");
		}
		char[] charArray = line.toCharArray();
		String temp = "";
		boolean openQuote = false;
		for( char c : charArray ) {
			if( c == ' ' && !openQuote) {
				tokens.add(temp);
				temp = "";
			} else if( c == '"' ) {
				if( openQuote ) {
					openQuote = false;
					tokens.add(temp);
					temp = "";
				} else {
					openQuote = true;
				}
			} else {
				temp += c;
			}
		}
		return tokens;
	}
	
}
