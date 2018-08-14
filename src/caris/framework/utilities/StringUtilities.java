package caris.framework.utilities;

import java.util.ArrayList;
import java.util.Set;

public class StringUtilities {
	
	public static boolean equalsIgnoreCase( String a, String b ) {
		return a.toLowerCase().equals(b);
	}
	
	public static Boolean containsIgnoreCase(Set<String> a, String b) {
		for( String token : a ) {
			if( containsIgnoreCase(token, b) ) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasIgnoreCase(ArrayList<String> a, String b) {
		for( String token: a ) {
			if( token.equalsIgnoreCase(b) ) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasIgnoreCase(String[] a, String b) {
		for( String token: a ) {
			if( token.equalsIgnoreCase(b) ) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasIgnoreCase(Set<String> a, String b) {
		for( String token: a ) {
			if( token.equalsIgnoreCase(b) ) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsIgnoreCase(String a, String b) {
		return a.toLowerCase().contains(b.toLowerCase());
	}

	public static boolean containsIgnoreCase(ArrayList<String> a, String b) {
		for( String token : a ) {
			if( containsIgnoreCase(token, b) ) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsIgnoreCase(String[] a, String b) {
		for( String token : a ) {
			if( containsIgnoreCase(token, b) ) {
				return true;
			}
		}
		return false;
	}
	
}
