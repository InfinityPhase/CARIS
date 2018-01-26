package invokers;

import java.util.ArrayList;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import tokens.LineSet;
import tokens.Response;

public class LocationInvoker extends Invoker_Multiline {
	
	public LocationInvoker() {
		this( Status.ENABLED );
	}
	
	public LocationInvoker( Status status ) {
		this.status = status;
		name = "Location";
		prefix = "cLoc";
	}

	public Response process(MessageReceivedEvent event) {	
		multilineSetup(event);
		
		if( event.getMessage().getContent().equals("cLoc") || event.getMessage().getContent().equals("cLocation") ) {
			log.indent(1).log("LocationInvoker triggered.");
			EmbedBuilder builder = new EmbedBuilder();
			builder.withTitle("**__Active Locations__**");
			for( String location : variables.locations.keySet() ) {
				String people = "";
				for( String person : variables.locations.get(location) ) {
					people += person + ", ";
				}
				if( people.length() >= 2 ) {
					people = people.substring(0, people.length()-2);
					builder.appendField(location, people, false);
				}
			}
			embed = builder;
		}
		
		if( tokens.get(0).equals("cLoc:") || tokens.get(0).equals("cLocation:") ) {
			log.indent(1).log("LocationInvoker triggered.");
			String location = remainder(primaryLineSet.tokens.get(0), primaryLineSet.line);
			if( location.isEmpty() ) {
				log.indent(2).log("Syntax Error. Aborting.");
				response = "Please enter a valid location name.";
				return build();
			} else {
				for( LineSet ls : auxiliaryLineSets ) {
					if( ls.tokens.get(0).equalsIgnoreCase("add") ) {
						String person = remainder(ls.tokens.get(0), ls.line);
						log.indent(3).log("Command \"add\" detected.");
						if( variables.locations.containsKey(location.toLowerCase()) ) {
							log.indent(4).log("Location \"" + location + "\" found.");
							if( variables.locations.get(location.toLowerCase()).contains(person.toLowerCase()) ) {
								response = person + " is already at " + location + ".";
								return build();
							} else {
								log.indent(4).log("New Location \"" + location + "\" generated.");
								for( String l : variables.locations.keySet() ) {
									ArrayList<String> locals = variables.locations.get(l);
									if( locals.contains(person.toLowerCase()) ) {
										locals.remove(person.toLowerCase());
									}
								}
								log.indent(4).log("Removing previous location references.");
								variables.locations.get(location.toLowerCase()).add(person.toLowerCase());
								variables.people.put(person.toLowerCase(), location.toLowerCase());
								response = person + "'s location has been set to " + location + ".";
							}
						} else {
							log.indent(4).log("Removing previous location references.");
							for( String l : variables.locations.keySet() ) {
								ArrayList<String> locals = variables.locations.get(l);
								if( locals.contains(person.toLowerCase()) ) {
									locals.remove(person.toLowerCase());
								}
							}
							ArrayList<String> locals = new ArrayList<String>();
							locals.add(person.toLowerCase());
							variables.locations.put(location.toLowerCase(), locals);
							variables.people.put(person.toLowerCase(), location.toLowerCase());
							response = person + "'s location has been set to " + location + ".";
							log.indent(4).log("Location set successfully.");
						}
					} else if( ls.tokens.get(0).equalsIgnoreCase("remove") ) {
						log.indent(3).log("Command \"remove\" detected.");
						String person = remainder(ls.tokens.get(0), ls.line);
						if( variables.people.keySet().contains(person.toLowerCase()) ) {
							if( variables.locations.containsKey(location.toLowerCase()) ) {
								log.indent(4).log("Location \"" + location + "\" found.");
								if( variables.locations.get(location.toLowerCase()).contains(person.toLowerCase()) ) {
									variables.locations.get(location.toLowerCase()).remove(person.toLowerCase());
									variables.people.remove(person.toLowerCase());
									log.indent(4).log("Person removed from location.");
									response = person + " removed from " + location + ".";
								} else {
									response = person + " is not at " + location + ".";
									return build();
								}
							} else {
								response = location + " is not a valid location.";
								return build();
							}
						} else {
							response = person + " is not set to a location.";
							return build();
						}
					} else if( ls.tokens.get(0).equalsIgnoreCase("reset") ) {
						log.indent(3).log("Command \"reset\" detected.");
						if( variables.locations.containsKey(location.toLowerCase()) ) {
							for( String p : variables.locations.get(location.toLowerCase()) ) {
								if( variables.people.containsKey(p) ) {
									variables.people.remove(p);
								} else {
									log.indent(4).log("Weird error; \"" + p + "\"" + " wasn't set to location but was in \"" + location + "\".");
								}
							}
							variables.locations.put(location.toLowerCase(), new ArrayList<String>());
							log.indent(4).log("Location reset successfully.");
							response = location + " has been reset.";
						} else {
							response = location + " does not exist.";
							return build();
						}
					} else {
						log.indent(3).log("Invalid command.");
					}
				}
			}
		} else {
			log.indent(2).log("LocationInvoker unactivated.");
		}
		log.indent(1).log("LocationInvoker processed.");
		return build();
	}

}
