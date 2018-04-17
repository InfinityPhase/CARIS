package modules.invokers;

import java.util.ArrayList;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import tokens.Response;

public class LocationInvoker extends Invoker {

	public LocationInvoker() {
		this( Status.ENABLED );
	}

	public LocationInvoker( Status status ) {
		this.status = status;
		name = "cLocation";
		prefix = "cLocation";
		help = "**__cLocation__**"  +
				 "\nThis command allows you to keep track of where everyone is.\n"  +
				 "\nUsage ` cLocation: <location> <function> <person 1> .. <person N> `"  +
				 "\n` <location> `: the name of the location you are modifying" +
				 "\n` <function> `: the way you are modifying the location" +
				 "\n\t\t` add `\t\t-\t\t*Adds people to the location*"  +
				 "\n\t\t` remove `\t\t-\t\t*Removes people from the location*"  +
				 "\n\t\t` reset `\t\t-\t\t*Resets the location*"  +
				 "\n\t` <person 1..N> : the name(s) to add/remove to the location\n"  +
				 "\n```cLocation: School add \"Alina Kim\" Anthony```";
	}

	public Response process(MessageReceivedEvent event) {	
		linesetSetup(event);

		if( command.tokens.size() == 1 ) { // No arguments passed
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
		} else if( command.tokens.size() > 1 ) { // Has arguments		
			String location = command.tokens.get(1);
			if( location.isEmpty() ) {
				log.indent(2).log("Syntax Error. Aborting.");
				response = "Please enter a valid location name.";
				return build();
			} else {
				if( command.tokens.size() < 3 ) {
					log.indent(2).log("Syntax Error. Aborting.");
					response = "Please enter a valid function.";
					return build();
				}
				if( command.tokens.get(2).equalsIgnoreCase("add") ) {
					if( command.tokens.size() < 4 ) {
						log.indent(2).log("Syntax Error. Aborting.");
						response = "Please enter at least one target.";
						return build();
					}
					int max = command.tokens.size();
					for( int f=3; f<max; f++ ) {
						String person = command.tokens.get(f);
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
					}
				} else if( command.tokens.get(2).equalsIgnoreCase("remove") ) {
					if( command.tokens.size() < 4 ) {
						log.indent(2).log("Syntax Error. Aborting.");
						response = "Please enter at least one target.";
						return build();
					}
					int max = command.tokens.size();
					for( int f=3; f<max; f++ ) {
						String person = command.tokens.get(f);
						log.indent(3).log("Command \"remove\" detected.");
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
					}
				} else if( command.tokens.get(2).equalsIgnoreCase("reset") ) {
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
		} else {
			log.indent(2).log("LocationInvoker unactivated.");
		}
		log.indent(1).log("LocationInvoker processed.");
		return build();
	}

}
