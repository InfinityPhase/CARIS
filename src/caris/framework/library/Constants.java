package caris.framework.library;

public class Constants {
	public static final String ADMIN_PREFIX = "==>";
	public static final String DEFAULT_PLAYING_TEXT = "Type \"cHelp\" for help!";

	public static final String[] DISABLED_RESPONDERS = new String[] {
			
	};
	
	public static final String[] DISABLED_INVOKERS = new String[] {
			
	};
	
	public static final String[] DISABLED_CONSTRUCTORS = new String[] {
			
	};
	
	public static final String[] DISABLED_TOOLS = new String[] {
			
	};
	
	public static final String[] DISABLED_CONTROLLERS = new String[] {
			
	};
	
	public static final String[] COMMAND_EXACTS = new String[] {
			
	};
	
	public static final String NAME = "CARIS";
	public static final boolean OFFLINE = false;
	public static final boolean RESPOND_TO_BOT = false; // If the user is a bot, ignore.

	public static final long[] ADMIN_IDS = new long[]{
			Long.parseLong("246562987651891200"),
			Long.parseLong("365715538166415362"),
	};

	public static final boolean DEBUG = false;
	public static final boolean PRINT = true;
	public static final boolean LOG = true;
	
	public static final int DEBUG_VERBOSITY = -1;
	public static final int PRINT_VERBOSITY = -1;
	public static final int LOG_VERBOSITY = -1;

	/* Settings for saving the state of CARIS */
	// SAVETIME uses milliseconds
	// Default is 6000000 ms
	public static final int SAVETIME = 20000;
	public static final boolean PREPENDDATE = true;
	public static final boolean SAVESTATE = false;
	public static final boolean LOG_FILE = false;
	public static final boolean DEBUG_FILE = false;
	public static final String SAVEFILE = "CARIS_State";
	public static final String DEBUG_FILE_NAME = "DebugLog";
	public static final String LOG_FILE_NAME = "StatusLog";
	public static final String SAVEEXTENTION = ".caris";
	public static final String DATEFORMAT = "yyyyMMddhhmm";
	public static final String ENCODING = "UTF-8";

	// Simple Logger Constants
	public final static String PRINT_INDENT = "=";
	public final static String DEBUG_INDENT = ".";
	public final static String ERROR_INDENT = "!";
	public final static String HEADER = "> ";
	public final static int DEFAULT_INDENT_LEVEL = 0;
	public final static int DEFAULT_INDENT_INCREMENT = 2;

	// Channel Setting Constants
	public static final boolean LISTEN_BLACKLIST = false;
	public static final boolean LISTEN_NOT_WHITELIST = false;
	public static final String BLACKLIST_HEADER = "NOBOT";
	public static final String WHITELIST_HEADER = "BOT";

	// Default Off Modules
	public static final String[] DEFAULT_DISBABLED = new String[] {
			
	};
	
	public static final String COMMAND_SEPERATOR = ":"; // Eg: cEcho: Hey there
	public static final String[] DISABLED_INDEPENDENTS = {};
}
