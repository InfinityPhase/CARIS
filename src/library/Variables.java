package library;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.eclipse.jetty.util.Fields;

import com.fasterxml.jackson.databind.ObjectWriter;

import main.GuildInfo;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import utilities.DataSaver;

public class Variables implements Serializable{
	// Dynamic global variables
	
	/* This is nessessary */
	private static final long serialVersionUID = 5666750084753825282L;
	/* Gigantic Variable Library */
	public static HashMap<String, IChannel> channelMap = new HashMap<String, IChannel>();
	public static HashMap<IGuild, GuildInfo> guildIndex = new HashMap<IGuild, GuildInfo>();
	
	/* Global Utilities */
	public static DataSaver ds = new DataSaver();
	
	public void writeObject( ObjectWriter out, File fileName ) {
		try {
			// Fuck. This doesn't work.
			out.writeValue( fileName, new Variables() );
			for( Field f : Variables.class.getDeclaredFields() ) {
				out.writeValue( fileName, f );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
