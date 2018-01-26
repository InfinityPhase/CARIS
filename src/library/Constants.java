package library;

import java.util.Calendar;

import utilities.Logger.level;
import utilities.Logger.output;

public class Constants {
	public static final String ADMIN_PREFIX = "==> ";
	public static final String COMMAND_PREFIX = ".c "; // soon to be deprecated
	public static final String DEFAULT_PLAYING_TEXT = "Type \"cHelp\" for help!";

	public static final String[] DISABLED = new String[] {
			"cFortune",
			"cNick",
			"cMusic"
	};

	public static final String[] COMMAND_PREFIXES = new String[] {
			"cLoc:",
			"cLocation:",
			"cVote:",
			"cPoll:",
			"cEmbed:",
	};
	
	public static final String[] COMMAND_EXACTS = new String[] {
			"cLoc",
			"cLocation",
			"cVote",
			"cPoll",
			"cHelp",
	};
	
	public static final String NAME = "CARIS";
	public static final boolean OFFLINE = false;

	public static final long[] ADMIN_IDS = new long[]{
			Long.parseLong("246562987651891200"),
			Long.parseLong("365715538166415362"),
	};

	/* Debug Levels: 0: Everything 1: DebugMessages 2: Warning 3:Error -1 or 4:Status */
	public static final boolean DEBUG = true; // This isn't an int. What's up with those levels?

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

	// Controls the order of thoughts in the message log
	public static final String[] THOUGHT_ORDER = { "Author", "Time" };

	// Logger constants
	public final static String INDENT_STRING = "=";
	public final static String DEFAULT_HEADER = ">";
	public final static int DEFAULT_INDENT = 0;
	public final static int DEFAULT_BASE_INDENT = 0;
	public final static boolean INDENT_FILE = true;
	public final static boolean INDENT_CONSOLE = true;
	public final static boolean OUTPUT_TIME = false;
	public final static boolean OUTPUT_TYPE = false;
	public final static boolean DEFAULT_SHOULD_INDENT = true;
	// Maybe move the declaration of the eval here, so that the logger isn't imported here
	public final static level DEFAULT_LEVEL = level.STATUS;
	public final static output DEFUALT_OUTPUT = output.ALL;

	// Channel Setting Constants
	public static final boolean LISTEN_BLACKLIST = false;
	public static final boolean LISTEN_NOT_WHITELIST = false;
	public static final String BLACKLIST_HEADER = "NOBOT";
	public static final String WHITELIST_HEADER = "BOT";

	// Build Season Clock
	public static Calendar kickoff = Calendar.getInstance();

	// Default Off Modules
	public static final String[] DEFAULT_DISBABLED = new String[] {
			"Fortune Invoker",
			"Music Invoker",
			"Nickname Invoker",
	};
}
