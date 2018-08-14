package caris.framework.utilities;

import java.util.ArrayList;

public class TokenUtilities {
	public static char[] punctuation = new char[] {
			'.',
			',',
			'!',
			'?',
			';',
			'@',
	};
	
	public static ArrayList<String> parseTokens(String line) {
		ArrayList<String> tokens = new ArrayList<String>();
		while( line.contains("  ") ) {
			line = line.replace("  ", " ");
		}
		while( line.contains("“") ) {
			line = line.replace("“", "\"");
		}
		while( line.contains("”") ) {
			line = line.replace("”", "\"");
		}
		line += " ";
		char[] charArray = line.toCharArray();
		String temp = "";
		boolean openQuote = false;
		boolean border = false;
		for( char c : charArray ) {
			if( c == ' ' && !openQuote && temp.length() > 0 || c == '\n' && !openQuote && temp.length() > 0 ) {
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
					boolean valid = true;
					for( char p : punctuation ) {
						if( c == p ) {
							valid = false;
							break;
						}
					}
					if( valid || openQuote ) {
						temp += c;
					}
				}
				border = false;
			}
		}
		return tokens;
	}
	
	public static ArrayList<String> parseTokens(String line, char[] punctList) {
		ArrayList<String> tokens = new ArrayList<String>();
		line += " ";
		while( line.contains("  ") ) {
			line = line.replace("  ", " ");
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
					boolean valid = true;
					for( char p : punctList ) {
						if( c == p ) {
							valid = false;
							break;
						}
					}
					if( valid || openQuote ) {
						temp += c;
					}
				}
				border = false;
			}
		}
		return tokens;
	}
	
	public static ArrayList<String> parseQuoted(String line) {
		ArrayList<String> tokens = new ArrayList<String>();
		line += " ";
		while( line.contains("“") ) {
			line = line.replace("“", "\"");
		}
		while( line.contains("”") ) {
			line = line.replace("”", "\"");
		}
		while( line.contains("\"") ) {
			int indexA = line.indexOf('\"');
			line = line.substring(indexA+1);
			int indexB = line.indexOf('\"');
			if( indexB != -1 && indexB != 0 ) {
				String token = line.substring(0, indexB);
				tokens.add(token);
				line = line.substring(indexB+1);
			} else {
				break;
			}
		}
		return tokens;
	}
	
}
