package caris.framework.library;

import java.util.ArrayList;
import java.util.HashMap;

import caris.framework.tokens.Mail;
import sx.blah.discord.handle.obj.IUser;

public class UserInfo {
	
	public IUser user;
	public String location;
	public String lastMessage;
	
	public String nicknameLock;
	
	public Mailbox mailbox;
	
	public HashMap< String, Object > genericInfo;
	
	public UserInfo( IUser user ) {
		this.user = user;
		location = "";
		
		nicknameLock = "";
		
		mailbox = new Mailbox();
		
		genericInfo = new HashMap<String, Object>();
	}
	
	public class Mailbox {
		
		private ArrayList<Mail> inbox;
		
		public Mailbox() {
			this.inbox = new ArrayList<Mail>();
		}
		
		public boolean isEmpty() {
			for( Mail mail : inbox ) {
				if( !mail.read ) {
					return false;
				}
			}
			return true;
 		}
		
		public ArrayList<Mail> getMail() {
			return inbox;
		}
		
		public Mail get(int number) {
			return inbox.get(number);
		}
		
		public Mail remove(int number) {
			return inbox.remove(number);
		}
		
		public int size() {
			return inbox.size();
		}
		
		public void clear() {
			inbox.clear();
		}

		public void add(Mail mail) {
			inbox.add(0, mail);
		}
	}
}
