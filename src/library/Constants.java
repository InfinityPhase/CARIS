package library;

public class Constants {
	public static final String PREFIX = "==> ";
	public static final String NAME = "CARIS";
	public static final boolean OFFLINE = false;
	public static final boolean DEBUG = true;
	
	/* Settings for saving the state of CARIS */
	// SAVETIME uses milliseconds
	// Default is 6000000 ms
	public static final int SAVETIME = 10000;
	public static final boolean SAVESTATE = true;
	public static final boolean PREPENDDATE = true;
	public static final String SAVEFILE = "CARIS_State";
	public static final String SAVEEXTENTION = ".json";
	public static final String DATEFORMAT = "yyyyMMddhhmm";
	public static final String ENCODING = "UTF-8";
}
