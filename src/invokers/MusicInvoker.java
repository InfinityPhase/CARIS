package invokers;

import lavaplayer.player.AudioLoadResultHandler;
import lavaplayer.tools.FriendlyException;
import lavaplayer.track.AudioPlaylist;
import lavaplayer.track.AudioTrack;
import main.Brain;
import music.GuildMusicManager;
import sx.blah.discord.handle.audio.IAudioManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.MissingPermissionsException;
import tokens.Response;

public class MusicInvoker extends Invoker {

	public Response process(MessageReceivedEvent event) {
		messageSetup(event);
		if( tokens.get(0).equals("cMusic:") ) {
			log.indent(1).log("MusicInvoker triggered.");
			if( tokens.size() < 2 ) {
				response = "Syntax Error: Command not specified.";
				return build();
			}
			if( tokens.get(1).equals("play") ) {
				log.indent(2).log("Play command detected.");
				if( message.isEmpty() ) {
					response = "Please put the song in quotes.";
					return build();
				}
				log.indent(2).log("Attempting to play song " + message);
				loadAndPlay(event.getChannel(), message);
				log.indent(3).log("Attempt completed.");
			}
			else if( tokens.get(1).equals("skip") ) {
				log.indent(2).log("Skip command detected.");
				skipTrack(event.getChannel());
			}
			else if( tokens.get(1).equals("stop") ) {
				log.indent(2).log("Stop command detected.");
				leaveVoiceChannel(event);
			}
		}
		return build();
	}
	
	private synchronized GuildMusicManager getGuildAudioPlayer(IGuild guild) {
	    long guildId = guild.getLongID();
	    GuildMusicManager musicManager = Brain.musicManagers.get(guildId);

	    if (musicManager == null) {
	      musicManager = new GuildMusicManager(Brain.playerManager);
	      Brain.musicManagers.put(guildId, musicManager);
	    }

	    guild.getAudioManager().setAudioProvider(musicManager.getAudioProvider());

	    return musicManager;
	  }
	
	private void loadAndPlay(final IChannel channel, final String trackUrl) {
	    GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
	    if( musicManager != null ) {
	    	log.indent(3).log("Music Manager initialized.");
	    } else {
	    	log.indent(3).log("Music Manager failed to initialize.");
	    }
	    Brain.playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
	      @Override
	      public void trackLoaded(AudioTrack track) {
	    	log.indent(3).log("Track loaded successfully.");
	        response = "Adding to queue " + track.getInfo().title;
	        play(channel.getGuild(), musicManager, track);
	      }

	      @Override
	      public void playlistLoaded(AudioPlaylist playlist) {
	    	log.indent(3).log("Playlist loaded successfully.");
	        AudioTrack firstTrack = playlist.getSelectedTrack();

	        if (firstTrack == null) {
	          firstTrack = playlist.getTracks().get(0);
	        }

	        response = "Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")";

	        play(channel.getGuild(), musicManager, firstTrack);
	      }

	      @Override
	      public void noMatches() {
	    	log.indent(3).log("Nothing found.");
	        response = "Nothing found by " + trackUrl;
	      }

	      @Override
	      public void loadFailed(FriendlyException exception) {
	    	log.indent(3).log("Load failed.");
	        response = "Could not play: " + exception.getMessage();
	      }
	    });
	  }

	  private void play(IGuild guild, GuildMusicManager musicManager, AudioTrack track) {
	    connectToFirstVoiceChannel(guild.getAudioManager());

	    musicManager.scheduler.queue(track);
	  }

	  private void skipTrack(IChannel channel) {
	    GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
	    musicManager.scheduler.nextTrack();

	    response = "Skipped to next track.";
	  }

	  private void connectToFirstVoiceChannel(IAudioManager audioManager) {
		log.indent(3).log("Connecting to voice channel.");
	    for (IVoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
	      if (voiceChannel.isConnected()) {
	    	log.indent(3).log("Already connected.");
	        return;
	      }
	    }

	    for (IVoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
	      try {
	    	log.indent(4).log("Attempting to join voice channel.");
	        voiceChannel.join();
	      } catch (MissingPermissionsException e) {
	    	log.indent(4).log("Connection failed.");
	        System.out.println("Cannot enter voice channel {}" + voiceChannel.getName());
	      }
	    }
	  }
	  
	private static void leaveVoiceChannel(MessageReceivedEvent event) {
        IVoiceChannel botVoiceChannel = event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel();
        if(botVoiceChannel == null) {
            return;
        }
        botVoiceChannel.leave();
	}
}
