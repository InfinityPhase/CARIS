package utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;

import main.Brain;
import main.GuildInfo;
import sx.blah.discord.handle.obj.IGuild;

public class DataSaver {

	public DataSaver(HashMap<IGuild, GuildInfo> guildIndex) {
		
	}
	
	public static void write(String string) {
		try {
			FileWriter fstream = new FileWriter("save.txt", true);
			BufferedWriter fbw = new BufferedWriter(fstream);
			if(string != null) {
				fbw.write(string);
				fbw.newLine();
				fbw.close();
			}
		} catch(Exception e) {
			Brain.log.debugOut("Couldn't print to the file");
		}
	}
}
 