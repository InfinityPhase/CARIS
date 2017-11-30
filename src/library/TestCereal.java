package library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class TestCereal implements Serializable {
	
	/**
	 * Auto generated. Yup. I'm lazy that way.
	 */
	private static final long serialVersionUID = -2806283937847840489L;
	
	public String one = "Neo";
	public int five = 5;
	public boolean happy = false;
	
	protected long tiny = 123456789;
	
	int level;
	String comment;
	
	private String james = "Town";
	private Map< String, String > phrases = new HashMap< String, String >();
	private List< Integer > numbers = new ArrayList< Integer >();
	
	private Map< IUser, IGuild > userToGuild = new HashMap< IUser, IGuild >();
	private IGuild guildHere;
	
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
	
	public void putGuildHere(IGuild guild) {
		this.guildHere = guild;
	}
}
