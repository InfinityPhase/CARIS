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
		boolean border = false;
		for( char c : charArray ) {
			if( c == ' ' && !openQuote && temp.length() > 0 ) {
				tokens.add(temp);
				temp = "";
				border = false;
			} else if( c == '"' ) {
				if( openQuote ) {
					openQuote = false;
					if( temp.length() > 0 ) {
						tokens.add(temp);
					}
					temp = "";
					border = true;
				} else {
					openQuote = true;
				}
			} else {
				if( !border ) {
					temp += c;
				}
				border = false;
			}
		}
		return tokens;
	}
	
}
