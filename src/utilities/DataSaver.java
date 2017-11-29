package utilities;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;

import library.Variables;
import main.GuildInfo;
import main.UserInfo;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import tokens.Poll;
import tokens.Reminder;
import utilities.Logger.level;

public class DataSaver {
	private Logger log = new Logger().indent(2).setDefaultLevel( level.INFO ).build();

	public DataSaver() {}
	
	public void save() {
		reset();
		for( IGuild iGuild : Variables.guildIndex.keySet() ) {
			String id = iGuild.getStringID();
			GuildInfo guildInfo = Variables.guildIndex.get(iGuild);
			String converted = convert(guildInfo);
			write(id);
			write(converted);
			write("");
		}
	}
	
	private void reset() {
		PrintWriter pw;
		try {
			pw = new PrintWriter("save.save");
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private String convert(GuildInfo guildInfo) {
		JSONObject json = new JSONObject();
		JSONObject modules = new JSONObject();
		for( String s : guildInfo.modules.keySet() ) {
			modules.put(s, guildInfo.modules.get(s));
		}
		JSONObject polls = new JSONObject();
		for( String s : guildInfo.polls.keySet() ) {
			Poll p = guildInfo.polls.get(s);
			JSONObject poll = new JSONObject();
			JSONObject options = new JSONObject();
			for( String t : p.options.keySet() ) {
				ArrayList<String> o = p.options.get(t);
				JSONObject option = new JSONObject();
				for( int f=0; f<o.size(); f++ ) {
					option.put(f+"", o.get(f));
				}
				options.put(t, option);
			}
			poll.put("name", p.name);
			poll.put("description", p.description);
			poll.put("options", options);
			poll.put("locked", p.locked);
			polls.put(s, poll);
		}
		JSONObject locations = new JSONObject();
		for( String s : guildInfo.locations.keySet() ) {
			ArrayList<String> l = guildInfo.locations.get(s);
			JSONObject location = new JSONObject();
			for( int f=0; f<l.size(); f++ ) {
				location.put(f+"", l.get(f));
			}
		}
		JSONObject people = new JSONObject();
		for( String s : guildInfo.people.keySet() ) {
			people.put(s, guildInfo.people.get(s));
		}
		JSONObject translator = new JSONObject();
		for( String s : guildInfo.translator.keySet() ) {
			translator.put(s, guildInfo.translator.get(s));
		}
		JSONObject userIndex = new JSONObject();
		for( IUser s : guildInfo.userIndex.keySet() ) {
			UserInfo d = guildInfo.userIndex.get(s);
			JSONObject userData = new JSONObject();
			JSONObject contactInfo = new JSONObject();
			for( String t : d.contactInfo.keySet() ) {
				contactInfo.put(t, d.contactInfo.get(t));
			}
			// TODO: Find way to iterate over the variables automagically.
			// This is rediculous.
			userData.put("karma", d.karma);
			userData.put("id", d.id);
			userData.put("location", d.location);
			userData.put("contactInfo", contactInfo);
			userData.put("location", d.location);
			userData.put("lastMessage", d.lastMessage);
			userIndex.put(s.toString(), userData); // TODO: Make sure this works the way it should
		}
		JSONObject reminders = new JSONObject();
		for( Calendar c : guildInfo.reminders.keySet() ) {
			String date = c.toString();
			Reminder r = guildInfo.reminders.get(c);
			JSONObject reminder = new JSONObject();
			reminder.put("message", r.message);
			reminder.put("author", r.author);
			reminder.put("channelID", r.channelID);
			reminders.put(date, reminder);
		}
		json.put("name", guildInfo.name);
		json.put("modules", modules);
		json.put("polls", polls);
		json.put("locations", locations);
		json.put("people", people);
		json.put("translator", translator);
		json.put("userIndex", userIndex);
		json.put("reminders", reminders);
		
		return json.toString(2);
	}
	
	private void write(String string) {
		try {
			FileWriter fstream = new FileWriter("save.save", true);
			BufferedWriter fbw = new BufferedWriter(fstream);
			if(string != null) {
				fbw.write(string);
				fbw.newLine();
				fbw.close();
			}
		} catch(Exception e) {
			log.level( level.ERROR ).log("Couldn't print to the file");
		}
	}
}
 