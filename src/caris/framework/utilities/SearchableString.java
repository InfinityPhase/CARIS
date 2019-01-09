package caris.framework.utilities;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SearchableString {

	public String content;
	
	public SearchableString(String content) {
		this.content = content;
	}
	
	public boolean equalsAnyOfIgnoreCase( String...strings ) {
		return equalsAnyOfIgnoreCase(Arrays.asList(strings));
	}
	
	public boolean equalsAnyOfIgnoreCase( List<String> strings ) {
		for( String token : strings ) {
			if( content.equalsIgnoreCase(token) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean inIgnoreCase(String[] strings) {
		return inIgnoreCase(Arrays.asList(strings));
	}
	
	public boolean inIgnoreCase(List<String> strings) {
		for( String token: strings ) {
			if( token.equalsIgnoreCase(content) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean inIgnoreCase(Set<String> strings) {
		for( String token: strings ) {
			if( token.equalsIgnoreCase(content) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsIgnoreCase(String string) {
		return content.toLowerCase().contains(string.toLowerCase());
	}
	
	public boolean containsAnyOfIgnoreCase(String... strings) {
		return containsAnyOfIgnoreCase(Arrays.asList(strings));
	}
	
	public boolean containsAnyOfIgnoreCase(List<String> strings) {
		for( String token : strings ) {
			if( containsIgnoreCase(token) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsAllOfIgnoreCase(String... strings) {
		return containsAllOfIgnoreCase(Arrays.asList(strings));
	}

	public boolean containsAllOfIgnoreCase(List<String> strings) {
		for( String token : strings ) {
			if( !containsIgnoreCase(token) ) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		return content;
	}
	
}
