package utilities;

import java.util.HashMap;
import java.util.Map;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class AccessControlList {

	public Class klass;

	public AccessControlList( Class klass ) {
		this.klass = klass;
	}

	enum permLevel {
		OWNER,
		WRITE,
		READ,
		NONE
	};

	enum groups {
		USER,
		CHANNEL,
		ROLE,
		GUILD,
		NONE
	};

	private Map<Group, Permission> permissions = new HashMap<Group, Permission>();

	private class Permission { // Stores the permission level values
		public boolean canShare;
		public permLevel level;
		
		public boolean owner;
		public boolean write;
		public boolean read;

		private Permission( permLevel level, boolean canShare ){
			this.level = level;
			this.canShare = canShare;
			
			switch( level ) {
			case OWNER:
				owner = true;
				write = true;
				read = true;
				break;
			case WRITE:
				owner = false;
				write = true;
				read = true;
				break;
			case READ:
				owner = false;
				write = false;
				read = true;
				break;
			case NONE:
				owner = false;
				write = false;
				read = false;
				break;
			default:
				owner = false;
				write = false;
				read = false;
				break;				
			}
		}

		private Permission( permLevel level ) {
			this( level, false );
		}
	}

	private class Group {
		String id;
		groups group;

		private Group( IUser user ) {
			this( groups.USER, user.getStringID() );
		}

		private Group( IChannel channel ) {
			this( groups.CHANNEL, channel.getStringID() );
		}

		private Group( IRole role ) {
			this( groups.ROLE, role.getStringID() );
		}

		private Group( IGuild guild ) {
			this( groups.GUILD, guild.getStringID() );
		}

		private Group( String id ) {
			this( groups.NONE, id );
		}

		private Group( groups group, String id ) {
			this.id = id;
			this.group = group;
		}

		public String getID() {
			return id;
		}

		public groups getGroup() {
			return group;
		}
	}
	
	/* Add info about a subset of users */

	public void addUser( IUser user, permLevel level, boolean canShare ) {
		addGroup( groups.USER, user.getStringID(), level, canShare );
	}

	public void addUser( IUser user, permLevel level ) {
		addUser( user, level, false );
	}

	public void addChannel( IChannel channel, permLevel level, boolean canShare ) {
		addGroup( groups.CHANNEL, channel.getStringID(), level, canShare );
	}

	public void addChannel( IChannel channel, permLevel level ) {
		addChannel( channel, level, false );
	}

	public void addRole( IRole role, permLevel level, boolean canShare ) {
		addGroup( groups.ROLE, role.getStringID(), level, canShare );
	}

	public void addRole( IRole role, permLevel level ) {
		addRole( role, level, false );
	}

	public void addGuild( IGuild guild, permLevel level, boolean canShare ) {
		addGroup( groups.GUILD, guild.getStringID(), level, canShare );
	}

	public void addGuild( IGuild guild, permLevel level ) {
		addGuild( guild, level, false );
	}

	public void addGroup( groups group, String id, permLevel level, boolean canShare ) {
		permissions.put( new Group( group, id ), new Permission(level, canShare) );
	}
	
	public void addGroup( groups group, String id, permLevel level ) {
		addGroup( group, id, level, false );
	}
	
	/* Get Permission object about a subset of users */
	
	private Permission getGroupPermission( Group group ) {
		return permissions.get( group );
	}
	
	/* More userfriendly stuff */

}
