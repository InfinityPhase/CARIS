package main;

import library.Variables;
import utilities.Logger;
import utilities.Logger.level;

public class LoggerShutdown extends Thread {
	
	public void run() {
		Brain.log.level( level.STATUS ).log("SHUTTING DOWN");
		
		for( Logger l : Variables.loggers ) {
			l.close();
		}
	}

}
