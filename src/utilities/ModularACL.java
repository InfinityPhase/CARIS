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
	
	/* If a Class is provided, emulate it */
	private Class klass;
	private List<Method> methods;
	
	private List<Level> levels;
	private List<Attribute> attributes;
	private Map<Level, Integer> powerLevel; // OVER 9000!!!!11!!!111 Actually though, uses a int value to prevent recusion.
	private Map<Method, Restriction> functionRestrictions;
	private Map<String, Restriction> generalRestrictions;
	
	public ModularACL() {
		this( new LinkedList<Level>(), new ArrayList<Attribute>(), new HashMap<Level, Integer>(), new HashMap<Method, Restriction>(), new HashMap<String, Restriction>() );
	}
	
	public ModularACL( List<Level> levels, List<Attribute> attributes, Map<Level, Integer> powerLevel, Map<Method, Restriction> functionRestrictions, Map<String, Restriction> generalRestrictions ) {
		this.levels = levels;
		this.attributes = attributes;
		this.powerLevel = powerLevel;
		
		this.functionRestrictions = functionRestrictions;
		this.generalRestrictions = generalRestrictions;
	}
	
	public ModularACL( Class klass ) {
		this();
		
		this.klass = klass;
		this.methods = new ArrayList<Method>();
		
		for( Method m : klass.getDeclaredMethods() ) {
			this.methods.add( m );
		}	
	}
	
	public ModularACL( Class klass, List<Level> levels, List<Attribute> attributes, Map<Level, Integer> powerLevel, Map<Method, Restriction> functionRestrictions, Map<String, Restriction> generalRestrictions ) {
		this( klass ); // Don't worry about it.
		
		this.levels = levels;
		this.attributes = attributes;
		this.powerLevel = powerLevel;
		
		this.functionRestrictions = functionRestrictions;
		this.generalRestrictions = generalRestrictions;
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
			return hasLevel( levels, level );
		}
		
		public boolean containsLevels( Level[] level ) {
			for( Level l : level ) {
				if( hasLevel( levels, l ) ) {
					return true;
				}
			}
			
			return false;
		}
		
		public Map<Attribute, Object> getAttributes() {
			return attributes;
		}
		
		public boolean containsAttribute( Attribute attribute ) {
			if( attributes.containsKey( attribute ) ) {
				return true;
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
	
	public void addAttribute( String name, Object value ) {
		attributes.add( new Attribute( name, value ) );
	}
	
	/* Confirming values */
	
	public boolean canUse( Method m, String level ) {
		Level levelObj = null;
		levelObj = getLevel( level );
		
		if( levelObj != null ) {
			if( hasLevel( functionRestrictions.get(m), levelObj ) ) {
				//Check attributes
				for( Attribute a : functionRestrictions.get(m).getAttributes().keySet() ) {
					if( a.valueDefault == functionRestrictions.get(m).attributes.get(a) ) {
						// If the default value of every attribute is enough to use the method
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean canUse( Method m, Level level ) {
		return hasLevel( functionRestrictions.get(m), level );
	}
	
	public boolean canUse( String ability, String level ) {
		Level levelObj = null;
		for( Level l : levels ) {
			if( l.name == level ) {
				levelObj = l;
				break;
			}
		}
		
		if( levelObj != null ) {
			return hasLevel( generalRestrictions.get( ability ), levelObj );
		}
		
		return false;
	}
	
	public boolean canUse( String ability, Level level ) {
		return hasLevel( generalRestrictions.get( ability ), level );
	}
	
	/**
	 * @param levelName The name of the level of access that is desired
	 * @return The Level object with the name given in the parameters
	 */
	public Level getLevel( String levelName ) {
		for( Level l : levels ) {
			if( l.name == levelName ) {
				return l;
			}
		}
		
		return null;
	}
	
	/* Experimental function stuff */
	
	public Method use( Method m, Level level ) {
		if( canUse( m, level ) ) {
			return m;
		}
		
		return null;
	}
	
	/* Enable the restricting of stuff */
	
	public void restrict( String ability, String level ) {
		restrict( ability, getLevel( level ) );
	}
	
	public void restrict( String ability, Level level ) {
		// Restrict access to ability via the level given
		generalRestrictions.put( ability, new Restriction( level ) );
	}
	
	public void restrict( String ability, Attribute attribute ) {
		restrict( ability, attribute, attribute.valueDefault );
	}
	
	public void restrict( String ability, Attribute attribute, Object value ) {
		generalRestrictions.put( ability, new Restriction(attribute, value) );
	}
	
	/* Ugh, standard stuff... */
	
	public void recalculatePowerLevel() {
		// Traverses level inheritance tree to calculate the minimum possible level for each.
		// Is computationally possibly expensive
	}
	
	/* If you passed a class to ModularACL */
	
	public Class getWrappedClass() {
		return klass;
	}
	
	private Attribute getAttributeByName( String attribute, Restriction restriction ) {
		Attribute[] av = (Attribute[]) restriction.attributes.keySet().toArray();
		
		for( Attribute a : restriction.attributes.keySet() ) { 
			if( a.name == returnAttributeByName( av ).get( attribute ) ) {
				return a;
			}
		}
		return null;
	}
	
	public boolean canUseMethod( String method, String level, String[] attributes, Object values ) {
		if( hasLevel( functionRestrictions.get( getMethod(method) ), getLevel( level ) ) && hasAttributes( getAttributeByName(attributes), values, functionRestrictions.get( getMethod(method) ) ) ) {
			
		}
		
		return false;
	}
	
	public Method useMethod( String name, String level, String[] attribute, Object[] value ) {
		
		if( hasLevel( functionRestrictions.get( returnAttributeByName( name ) ).getLevels(), level ) ) {
			return getMethod( name );
		} else if ( functionRestrictions.get( returnAttributeByName( name ). ) )
		
		
		return null;
	}
	
	public Method getMethod( String name ) {
		// Not restricted, do not use
		for( Method m : methods ) {
			if( m.getName() == name ) {
				return m;
			}
		}
		
		return null;
	}
	
	/* Recursive stuff that noone else needs */
	
	private boolean hasLevel( Restriction restrict, Level level ) { // Will theoretically work
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
	
	private boolean hasLevel( Level[] levelSet, String levelCheck ) {
		return hasLevel( levelSet, getLevel( levelCheck ) );
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
	
	private Method getMethodFromString( String name ) {
		for( Method m : functionRestrictions.keySet() ) {
			if( m.getName() == name ) {
				return m;
			}
		}
		return null;
	}
	
	private boolean defaultAttributesWork( Map<Attribute, Object> requiredAttributes ) {
		// Checks if the defaut value of attributes is enough to use something
		for( Attribute a : requiredAttributes.keySet() ) {
			if( a.valueDefault != requiredAttributes.get(a) ) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean defaultAttributesWork( Restriction restriction ) {
		return defaultAttributesWork( restriction.attributes );
	}
	
	private boolean hasAttributes( Attribute[] attributes, Object[] values, Restriction restriction ) {
		for( Attribute a : restriction.attributes.keySet() ) {
			if( a.value != values[ indexOfElelment( attributes, a ) ] ) {
				return false;
			}
		}
		
		return true;
	}
	
	private Map<String, Attribute> returnAttributeByName( Attribute[] attribute ) {
		Map<String, Attribute> result = new HashMap<String, Attribute>();
		for( Attribute a : attribute ) {
			result.put( a.name, a );
		}
		
		return result;
	}
	
	private int indexOfElelment( Attribute[] attribute, Attribute value ) {
		for( int i = 0; i < attribute.length; i++ ) {
			if( attribute[i] == value ) {
				return i;
			}
		}
		
		return -1;
	}

}
