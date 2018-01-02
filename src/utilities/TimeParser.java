package utilities;

import java.util.ArrayList;
import java.util.Calendar;

import utilities.Logger.level;

public class TimeParser {
	private static Logger log = new Logger().setDefaultIndent(2).setDefaultLevel( level.DEBUG ).setDefaultIndent(2).build();

	public String getString( ArrayList<String> tokens ) {
		// TODO: Should use SimpleDateFormat for standardization
		String result = "00000000000000"; // YYYYMMDDhhmmss


		for( int f=0; f<tokens.size(); f++ ) { // Stores the values in a single string
			String token = tokens.get(f);
			if( token.equals("year") || token.equals("years") && f != 0 ) {
				if(isInteger(tokens.get(f-1))) {
					result = tokens.get(f-1);
				}
			} else if( token.equals("month") || token.equals("months") && f != 0 ) {
				if(isInteger(tokens.get(f-1))) {
					result = result + tokens.get(f-1);
				}
			} else if( token.equals("day") || token.equals("days") && f != 0 ) {
				if(isInteger(tokens.get(f-1))) {
					result = result + tokens.get(f-1);		
				}
			} else if( token.equals("hour") || token.equals("hours") && f != 0 ) {
				if(isInteger(tokens.get(f-1))) {
					result = result + tokens.get(f-1);	
				}
			} else if( token.equals("minute") || token.equals("minutes") && f != 0 ) {
				if(isInteger(tokens.get(f-1))) {
					result = result + tokens.get(f-1);	
				}
			} else if( token.equals("second") || token.equals("seconds") && f != 0 ) {
				if(isInteger(tokens.get(f-1))) {
					result = result + tokens.get(f-1);
				}
			}
		}

		return result;
	}

	public Calendar parseTimer(ArrayList<String> tokens) {
		Calendar previous = Calendar.getInstance();
		Calendar target = Calendar.getInstance();
		
		Calendar parsed = parseArray( tokens );
		
		// Because breaking things into functions is a good thing...
		target.set(target.get(Calendar.YEAR)+parsed.get( Calendar.YEAR ), target.get(Calendar.MONTH)+parsed.get( Calendar.MONTH ), target.get(Calendar.DATE)+parsed.get( Calendar.DATE ), target.get(Calendar.HOUR_OF_DAY)+parsed.get( Calendar.HOUR_OF_DAY ), target.get(Calendar.MINUTE)+parsed.get( Calendar.MINUTE ), target.get(Calendar.SECOND)+parsed.get( Calendar.SECOND ));

		if( verify(previous, target) ) {
			return target;
		}

		return null;
	}

	public Calendar parseAlarm(ArrayList<String> tokens) {
		Calendar previous = Calendar.getInstance();
		Calendar target = Calendar.getInstance();

		int year = target.get(Calendar.YEAR);
		int month = target.get(Calendar.MONTH);
		int day = target.get(Calendar.DAY_OF_MONTH);
		int hour = target.get(Calendar.HOUR);
		int minute = target.get(Calendar.MINUTE);

		boolean yearChange = false;
		boolean monthChange = false;
		boolean dayChange = false;
		boolean hourChange = false;
		boolean minuteChange = false;
		boolean pm = false;

		for( String token : tokens ) {
			int order = parseOrder(token);
			if( token.equalsIgnoreCase("January") ) {
				month = Calendar.JANUARY;
				monthChange = true;
			} else if( token.equalsIgnoreCase("February") ) {
				month = Calendar.FEBRUARY;
				monthChange = true;
			} else if( token.equalsIgnoreCase("March") ) {
				month = Calendar.MARCH;
				monthChange = true;
			} else if( token.equalsIgnoreCase("April") ) {
				month = Calendar.APRIL;
				monthChange = true;
			} else if( token.equalsIgnoreCase("May") ) {
				month = Calendar.MAY;
				monthChange = true;
			} else if( token.equalsIgnoreCase("June") ) {
				month = Calendar.JUNE;
				monthChange = true;
			} else if( token.equalsIgnoreCase("July") ) {
				month = Calendar.JULY;
				monthChange = true;
			} else if( token.equalsIgnoreCase("August") ) {
				month = Calendar.AUGUST;
				monthChange = true;
			} else if( token.equalsIgnoreCase("September") ) {
				month = Calendar.SEPTEMBER;
				monthChange = true;
			} else if( token.equalsIgnoreCase("October") ) {
				month = Calendar.OCTOBER;
				monthChange = true;
			} else if( token.equalsIgnoreCase("November") ) {
				month = Calendar.NOVEMBER;
				monthChange = true;
			} else if( token.equalsIgnoreCase("December") ) {
				month = Calendar.DECEMBER;
				monthChange = true;
			} else if( order >= 1 && order <= 31 ) {
				day = order;
				dayChange = true;
			} else if( isInteger(token) ) {
				log.log(token);
				int possibleYear = Integer.parseInt(token);
				if( possibleYear >= target.get(Calendar.YEAR) ) {
					year = possibleYear;
					yearChange = true;
				}
			} else if( token.contains(":") ) {
				int posHour = parseHour(token);
				int posMinute = parseMinute(token);
				log.log( posHour );
				log.log( posMinute);
				if( hour != -1 ) {
					hour = posHour;
					hourChange = true;
				}
				if( minute != -1 ) {
					minute = posMinute;
					minuteChange = true;
				}
			} else if( token.equalsIgnoreCase("PM") ) {
				log.log("pm");
				pm = true;
			}
		}

		if( pm == true && hour < 12 ) {
			hour += 12;
		}

		if( yearChange && !monthChange && !dayChange && !hourChange && !minuteChange ) {
			month = Calendar.JANUARY;
			day = 1;
			hour = 0;
			minute = 0;
		} else if( monthChange && !dayChange && !hourChange && !minuteChange ) {
			day = 1;
			hour = 0;
			minute = 0;
		} else if( dayChange && !hourChange && !minuteChange ) {
			hour = 0;
			minute = 0;
		} else if( hourChange && !minuteChange ) {
			minute = 0;
		}

		if( verify(year, month, day) ) {
			target.set(year, month, day, hour, minute, 0);
			if( verify(previous, target) ) {
				return target;
			}
		}

		return null;
	}
	
	private Calendar parseArray( ArrayList<String> tokens ) {
		Calendar result = Calendar.getInstance();
		
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
		
		result.set( year, month, day, hour, minute, second );
		
		return result;
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
