package utilities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class ModularACL {


	enum groups {
		USER,
		CHANNEL,
		ROLE,
		GUILD,
		NONE
	};
	
	private List<Level> levels;
	private List<Attribute> attributes;
	private Map<Level, Integer> powerLevel; // OVER 9000!!!!11!!!111 Actually though, uses a int value to prevent recusion.
	private Map<Method, Restriction> functionRestrictions;
	private Map<String, Restriction> generalRestrictions;
	
	public ModularACL() {
		levels = new LinkedList<Level>();
		attributes = new ArrayList<Attribute>();
		powerLevel = new HashMap<Level, Integer>();
		
		functionRestrictions = new HashMap<Method, Restriction>();
		generalRestrictions = new HashMap<String, Restriction>();
	}
	
	public ModularACL( List<Level> levels, List<Attribute> attributes, Map<Level, Integer> powerLevel, Map<Method, Restriction> functionRestrictions, Map<String, Restriction> generalRestrictions ) {
		
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
	
	private class Level {
		
		String name;
		List<Level> subset; // Everything that these levels can do, we can do
		
		public Level( String name ) {
			
		}
		
		public Level( String name, LinkedList<Level> subset ) {
			this.name = name;
			this.subset = subset;
		}
		
		public Level( String name, Level[] subset ) {
			this.name = name;
			this.subset = new LinkedList<Level>();
			
			for( Level l : subset ) {
				this.subset.add(l);
			}
		}
		
		public boolean hasLevel( Level Level ) {
			return hasLevel( Level.name );
		}
		
		public boolean hasLevel( String name ) { // Checks if we have the ability to do something
			if( this.name == name ) {
				return true;
			}
			
			for( Level l : subset ) { // TODO Recursive call all sublevels
				if( l.name == name ) {
					return true;
				}
			}
			
			return false;
		}
		
		public boolean addLevel( Level level ) { // Theoretically, this will be fine...
			if( powerLevel.get( this ) == powerLevel.get( level ) ) { // Uses inverse counting, 0 is best
				subset.add( level );
				powerLevel.put( level, powerLevel.get( level ) + 1 );
				
				return true;
			} else if( powerLevel.get( this ) < powerLevel.get( level ) ) {
				subset.add( level );
				
				return true;
			} else {
				return false;
			}			
		}
	}
	
	public class Restriction {
		private List<Level> levels;
		private Map<Attribute, Object> attributes;
		
		public Restriction( Level level, Attribute attribute, Object value ) {
			levels = new ArrayList<Level>();
			attributes = new HashMap<Attribute, Object>();
			
			levels.add( level );
			attributes.put( attribute, value );
		}
		
		public Restriction( Level level ) {
			this( new Level[] { level }, new Attribute[0], new Object[0] );
		}
		
		public Restriction( Level[] level ) {
			this( level, new Attribute[0], new Object[0] );
		}
		
		public Restriction( Attribute attribute, Object value ) {
			this( new Level[0], new Attribute[] { attribute }, new Object[] { value } );
		}
		
		public Restriction( Attribute[] attribute, Object[] value ) {
			this( new Level[0], attribute, value );
		}
		
		public Restriction( Level[] level, Attribute[] attribute, Object[] value ) {
			levels = new ArrayList<Level>();
			attributes = new HashMap<Attribute, Object>();
			
			for( Level l : level ) {
				levels.add( l );
			}
			
			for( int i = 0; i < attribute.length; i++ ) {
				attributes.put( attribute[i], value[i] );
			}
		}
		
		public Level[] getLevels() {
			return levels.toArray( new Level[ levels.size() ] );
		}
		
		public List<Level> getLevelList() {
			return levels;
		}
		
		public boolean containsLevel( Level level ) {
			if( levels.contains( level ) ) {
				return true;
			}
			
			return false;
		}
		
		public boolean containsLevels( Level[] level ) {
			for( Level l : level ) {
				if( levels.contains( l ) ) {
					return true;
				}
			}
			
			return false;
		}
	}
	
	private class Attribute {
		
		private String name;
		private Object valueDefault;
		private Object value;
		
		public Attribute( String name, Object valueDefault ) {
			this.name = name;
			this.valueDefault = valueDefault;
			this.value = valueDefault;
		}
		
		public void reset() {
			value = valueDefault;
		}
		
		public void setValue( Object value ) {
			this.value = value;
		}
		
		public Object getValue() {
			return value;
		}
		
		public String getName() {
			return name;
		}
		
	}
	
	public void addLevel( String name, String subset ) {
		// TODO STUFF
	}
	
	public void addAttribute( String name, Object value ) {
		attributes.add( new Attribute( name, value ) );
	}
	
	/* Confirming values */
	
	public void recalculatePowerLevel() {
		// Traverses level inheritance tree to calculate the minimum possible level for each.
		// Is computationally possibly expensive
	}
	
	public boolean hasLevel( Restriction restrict, Level level ) { // Will theoretically work
		// restrict: The given restriction of values
		// level: The current level to check against the perm
		
		for( Level l : restrict.levels ) {
			if( hasLevel( l.subset, level ) ) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean hasLevel( List<Level> levelSet, Level levelCheck ) {
		return hasLevel( levelSet.toArray( new Level[ levelSet.size() ] ), levelCheck );
	}
	
	private boolean hasLevel( Level[] levelSet, Level levelCheck ) {
		
		for( Level l : levelSet ) {
			if( l.name == levelCheck.name ) {
				return true;
			} else {
				return hasLevel( l.subset, levelCheck );
			}
		}
		
		return false;
	}

}
