package main;

import utilities.Logger.level;

public class LoggerShutdown extends Thread {
	
	public void run() {
		Brain.log.level( level.STATUS ).log("SHUTTING DOWN");
		Brain.log.flush();
		Brain.log.close();
	}

}
