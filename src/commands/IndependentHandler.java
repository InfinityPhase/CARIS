package commands;

import main.Brain;
import modules.independent.Independent;
import tokens.Response;
import utilities.BotUtils;
import utilities.Logger;

public class IndependentHandler {
	static Logger log = new Logger().setBaseIndent(1).setDefaultShouldAppendTime(true).build();

	public static void check() {
		for( Independent i : Brain.independentModules.values() ) {
			Response result = i.process();
			
			if( result.recipient.size() > 0 ) {
				log.log("Independent Module (" + i.name + ") has executed a response");
				
				if( result.embed ) {
					BotUtils.sendMessage( result.recipient, result.builder );
				} else {
					BotUtils.sendMessage( result.recipient, result.text ); // print out highest priority response option 
				}
			}
			
		}
	}

}
