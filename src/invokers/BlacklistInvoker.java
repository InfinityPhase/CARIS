package invokers;

import java.util.ArrayList;
import java.util.List;

import library.Variables;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import tokens.Response;

public class BlacklistInvoker extends Invoker {

	private String[] sameChannel = { "here", "this" };

	@Override
	public Response process( MessageReceivedEvent event ) {
		setup(event);

		if( tokens.get(0).equalsIgnoreCase( "blacklist" ) ) {
			if( tokens.get(1).equalsIgnoreCase( "add" ) ) {
				if( containsIgnoreCase( sameChannel, tokens.get(2) ) ) {
					Variables.guildIndex.get( event.getGuild() ).blacklist.add( event.getChannel() );
					response = "I have blacklisted this channel from further communique.";
				} else {
					Variables.guildIndex.get( event.getGuild() ).blacklist.addAll( getChannels( event, tokens.get(2) ) );
					response = "I have blacklisted the following channels from further communique:\n" + 
							listChannels( getChannels( event, tokens.get(2) ) );
				}
			} else if( tokens.get(1).equalsIgnoreCase( "remove" ) ) {
				if( containsIgnoreCase( sameChannel, tokens.get(2) ) ) {
					Variables.guildIndex.get( event.getGuild() ).blacklist.remove( event.getChannel() );
					response = "I have un-blacklisted this channel for further communique.";
				}
			} else if( tokens.get(1).equalsIgnoreCase( "get" ) ) {
				// Return the channel list
				response = "The following channels are blacklisted:\n" +
						listChannels( Variables.guildIndex.get( event.getGuild() ).blacklist );
			}
		} else if( tokens.get(0).equalsIgnoreCase( "whitelist" ) ) {
			if( tokens.get(1).equalsIgnoreCase( "add" ) ) {
				if( containsIgnoreCase( sameChannel, tokens.get(2) ) ) {
					Variables.guildIndex.get( event.getGuild() ).whitelist.add( event.getChannel() );
					response = "I have whitelisted this channel for further communique.";
				} else {
					Variables.guildIndex.get( event.getGuild() ).whitelist.addAll( getChannels( event, tokens.get(2) ) );
					response = "I have whitelisted the following channels for further communique:\n" +
							listChannels( getChannels( event, tokens.get(2) ) );
				}
			} else if( tokens.get(1).equalsIgnoreCase( "remove" ) ) {
				if( containsIgnoreCase( sameChannel, tokens.get(2) ) ) {
					Variables.guildIndex.get( event.getGuild() ).whitelist.remove( event.getChannel() );
					response = "I have un-whitelisted this channel from further communique.";
				} else {
					Variables.guildIndex.get( event.getGuild() ).whitelist.removeAll( getChannels( event, tokens.get(2) ) );
					response = "I have un-whitelisted the following channels from further communique:\n" + 
							listChannels( getChannels( event, tokens.get(2) ) );
				}
			} else if( tokens.get(1).equalsIgnoreCase( "get" ) ) {
				// Return the channel list
				response = "The following channels are whitelisted:\n" +
						listChannels( Variables.guildIndex.get( event.getGuild() ).whitelist );
			}
		}

		return build();
	}

	private String listChannels( List<IChannel> list ) {
		String o = "";
		for( IChannel c : list ) {
			o.concat( c.getName() + ":" + c.getLongID() + "\n" );
		}
		
		if( o != null && o.length() > 0 ) {
			o = o.substring(0, o.length() - 1);
		}
		
		return o;
	}

	private List<IChannel> getChannels( MessageReceivedEvent event, String name ) {
		List<IChannel> channel = new ArrayList<IChannel>();
		try{
			channel.add( event.getGuild().getChannelByID( Long.valueOf( tokens.get(3) ) ) );
		} catch( NumberFormatException e ) {
			channel = event.getGuild().getChannelsByName( tokens.get(3) );
		}

		return channel;
	}

}
