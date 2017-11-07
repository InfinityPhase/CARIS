package library;

public class Constants {
        public static final String PREFIX = "==> ";
        public static final String NAME = "CARIS";
        public static final boolean OFFLINE = false;
        
        /* Debug Levels: 0: Everything 1: DebugMessages 2: Warning 3:Error -1 or 4:Status */
        public static final boolean DEBUG = true;
        
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
}
