package library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.obj.Guild;
import sx.blah.discord.handle.impl.obj.User;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class TestCereal {
	public String one = "Neo";
	public int five = 5;
	public boolean happy = false;
	
	int level;
	String comment;
	
	private String james = "Town";
	private Map< String, String > phrases = new HashMap< String, String >();
	private List< Integer > numbers = new ArrayList< Integer >();
	
	private Map< IUser, IGuild > userToGuild = new HashMap< IUser, IGuild >();
	
	public TestCereal(int level, String comment ) {
		this.level = level;
		this.comment = comment;
	}
	
	public void putPhrases(String key, String value) {
		this.phrases.put(key, value);
	}
	
	public void putNumbers(int number) {
		this.numbers.add(number);
	}
	
	public void putUserToGuild(IUser iUser, IGuild iGuild) {
		this.userToGuild.put(iUser, iGuild);
	}
}
