package utilities;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeParser {
	
	public Calendar parseTimer(ArrayList<String> tokens) {
		Calendar previous = Calendar.getInstance();
		Calendar target = Calendar.getInstance();
		int year = 0;
		int month = 0;
		int day = 0;
		int hour = 0;
		int minute = 0;
		int second = 0;
		for( int f=0; f<tokens.size(); f++ ) {
			String token = tokens.get(f);
			if( token.equals("year") || token.equals("years") && f != 0 ) {
				if(isInteger(tokens.get(f-1))) {
					year = Integer.parseInt(tokens.get(f-1));
				}
			} else if( token.equals("month") || token.equals("months") && f != 0 ) {
				if(isInteger(tokens.get(f-1))) {
					month = Integer.parseInt(tokens.get(f-1));
				}
			} else if( token.equals("day") || token.equals("days") && f != 0 ) {
				if(isInteger(tokens.get(f-1))) {
					day = Integer.parseInt(tokens.get(f-1));
				}
			} else if( token.equals("hour") || token.equals("hours") && f != 0 ) {
				if(isInteger(tokens.get(f-1))) {
					hour = Integer.parseInt(tokens.get(f-1));
				}
			} else if( token.equals("minute") || token.equals("minutes") && f != 0 ) {
				if(isInteger(tokens.get(f-1))) {
					minute = Integer.parseInt(tokens.get(f-1));
				}
			} else if( token.equals("second") || token.equals("seconds") && f != 0 ) {
				if(isInteger(tokens.get(f-1))) {
					second = Integer.parseInt(tokens.get(f-1));
				}
			}
		}
		target.set(target.get(Calendar.YEAR)+year, target.get(Calendar.MONTH)+month, target.get(Calendar.DATE)+day, target.get(Calendar.HOUR_OF_DAY)+hour, target.get(Calendar.MINUTE)+minute, target.get(Calendar.SECOND)+second);
		if( verify(previous, target) ) {
			return target;
		}
		return null;
	}
	
	public Calendar parseAlarm(ArrayList<String> tokens) {
		Calendar target = Calendar.getInstance();
		int year = target.get(Calendar.YEAR);
		int month = target.get(Calendar.MONTH);
		int day = target.get(Calendar.DAY_OF_MONTH);
		int hour = target.get(Calendar.HOUR);
		int minute = target.get(Calendar.MINUTE);
		boolean pm = false;
		for( String token : tokens ) {
			int order = parseOrder(token);
			if( token.equalsIgnoreCase("January") ) {
				month = Calendar.JANUARY;
			} else if( token.equalsIgnoreCase("February") ) {
				month = Calendar.FEBRUARY;
			} else if( token.equalsIgnoreCase("March") ) {
				month = Calendar.MARCH;
			} else if( token.equalsIgnoreCase("April") ) {
				month = Calendar.APRIL;
			} else if( token.equalsIgnoreCase("May") ) {
				month = Calendar.MAY;
			} else if( token.equalsIgnoreCase("June") ) {
				month = Calendar.JUNE;
			} else if( token.equalsIgnoreCase("July") ) {
				month = Calendar.JULY;
			} else if( token.equalsIgnoreCase("August") ) {
				month = Calendar.AUGUST;
			} else if( token.equalsIgnoreCase("September") ) {
				month = Calendar.SEPTEMBER;
			} else if( token.equalsIgnoreCase("October") ) {
				month = Calendar.OCTOBER;
			} else if( token.equalsIgnoreCase("November") ) {
				month = Calendar.NOVEMBER;
			} else if( token.equalsIgnoreCase("December") ) {
				month = Calendar.DECEMBER;
			} else if( order >= 1 && order <= 31 ) {
				day = order;
			} else if( isInteger(token) ) {
				int possibleYear = Integer.parseInt(token);
				if( possibleYear >= target.get(Calendar.YEAR) ) {
					year = possibleYear;
				}
			} else if( token.contains(":") ) {
				int posHour = parseHour(token);
				int posMinute = parseMinute(token);
				if( hour != -1 ) {
					hour = posHour;
				}
				if( minute != -1 ) {
					minute = posMinute;
				}
			} else if( token.equalsIgnoreCase("PM") ) {
				pm = true;
			}
		}
		if( pm == true && hour < 12 ) {
			hour += 12;
		}
		if( verify(year, month, day) ) {
			target.set(year, month, day, hour, minute, 0);
			return target;
		}
		return null;
	}
	private boolean verify( Calendar p, Calendar t ) {
		return t.after(p);
	}
	private boolean verify(int year, int month, int day) {
		if( month == Calendar.JANUARY ) {
			return day <= 31;
		} else if( month == Calendar.FEBRUARY ) {
			if( year % 4 == 0 ) {
				return day <= 29;
			} else {
				return day <= 28;
			}
		} else if( month == Calendar.MARCH ) {
			return day <= 31;
		} else if( month == Calendar.APRIL ) {
			return day <= 30;
		} else if( month == Calendar.MAY ) {
			return day <= 31;
		} else if( month == Calendar.JUNE ) {
			return day <= 30;
		} else if( month == Calendar.JULY ) {
			return day <= 31;
		} else if( month == Calendar.AUGUST ) {
			return day <= 31;
		} else if( month == Calendar.SEPTEMBER ) {
			return day <= 30;
		} else if( month == Calendar.OCTOBER ) {
			return day <= 31;
		} else if( month == Calendar.NOVEMBER ) {
			return day <= 30;
		} else if( month == Calendar.DECEMBER ) {
			return day <= 31;
		} else {
			return false;
		}
	}
	private int parseOrder( String s ) {
		s = s.replace("st", "");
		s = s.replace("nd", "");
		s = s.replace("rd", "");
		s = s.replace("th", "");
		if( isInteger(s) ) {
			return Integer.parseInt(s);
		}
		return -1;
	}
	private int parseHour( String s ) {
		int index = s.indexOf(":");
		String hour = s.substring(0, index);
		if( isInteger(hour) ) {
			int num = Integer.parseInt(hour);
			if( num >= 0 && num < 24 ) {
				return num;
			}
		}
		return -1;
	}
	private int parseMinute( String s ) {
		int index = s.indexOf(":");
		if( index == s.length()-1 ) {
			return -1;
		}
		String minute = s.substring(index+1);
		if( isInteger(minute) ) {
			int num = Integer.parseInt(minute);
			if( num >= 0 && num < 60 ) {
				return num;
			}
		}
		return -1;
	}
	private boolean isInteger(String s) {
	    return isInteger(s,10);
	}

	private boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
}
