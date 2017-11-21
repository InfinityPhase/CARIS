package utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import main.GuildInfo;
import sx.blah.discord.handle.obj.IGuild;
import tokens.Poll;
import tokens.UserData;

public class DataSaver {

	public DataSaver(HashMap<IGuild, GuildInfo> guildIndex) {
		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<String> jsons = new ArrayList<String>();
		for( IGuild iGuild : guildIndex.keySet() ) {
			String id = iGuild.getStringID();
			GuildInfo guildInfo = guildIndex.get(iGuild);
			String converted = convert(guildInfo);
			ids.add(id);
			jsons.add(converted);
			
		}
	}
	
	public String convert(GuildInfo guildInfo) {
		String ret = "";
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
		for( String s : guildInfo.userIndex.keySet() ) {
			UserData d = guildInfo.userIndex.get(s);
			JSONObject userData = new JSONObject();
			JSONObject contactInfo = new JSONObject();
			for( String t : d.contactInfo.keySet() ) {
				contactInfo.put(t, d.contactInfo.get(t));
			}
			userData.put("karma", d.karma);
			userData.put("id", d.id);
			userData.put("location", d.location);
			userData.put("contactInfo", contactInfo);
			userData.put("location", d.location);
			userData.put("lastMessage", d.lastMessage);
			userIndex.put(s, userData);
		}
		json.put("name", guildInfo.name);
		json.put("modules", modules);
		json.put("polls", polls);
		json.put("locations", locations);
		json.put("people", people);
		json.put("translator", translator);
		json.put("userIndex", userIndex);
		
		return ret;
	}
	
	public void write(String string) {
		try {
			FileWriter fstream = new FileWriter("save.txt", true);
			BufferedWriter fbw = new BufferedWriter(fstream);
			if(string != null) {
				fbw.write(string);
				fbw.newLine();
				fbw.close();
			}
		} catch(Exception e) {
			System.out.println("Couldn't print to the file");
		}
	}
}
 