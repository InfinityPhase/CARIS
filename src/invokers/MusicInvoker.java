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
		if( tokens.get(0).equals("music") ) {
			if( tokens.size() < 2 ) {
				response = "Syntax Error: Command not specified.";
				return build();
			}
			if( tokens.get(1).equals("play") ) {
				if( message.isEmpty() ) {
					response = "Please put the song in quotes.";
					return build();
				}
				loadAndPlay(event.getChannel(), message);
			}
			else if( tokens.get(1).equals("skip") ) {
				skipTrack(event.getChannel());
			}
			else if( tokens.get(1).equals("stop") ) {
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

	    Brain.playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
	      @Override
	      public void trackLoaded(AudioTrack track) {
	        response = "Adding to queue " + track.getInfo().title;

	        play(channel.getGuild(), musicManager, track);
	      }

	      @Override
	      public void playlistLoaded(AudioPlaylist playlist) {
	        AudioTrack firstTrack = playlist.getSelectedTrack();

	        if (firstTrack == null) {
	          firstTrack = playlist.getTracks().get(0);
	        }

	        response = "Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")";

	        play(channel.getGuild(), musicManager, firstTrack);
	      }

	      @Override
	      public void noMatches() {
	        response = "Nothing found by " + trackUrl;
	      }

	      @Override
	      public void loadFailed(FriendlyException exception) {
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

	  private static void connectToFirstVoiceChannel(IAudioManager audioManager) {
	    for (IVoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
	      if (voiceChannel.isConnected()) {
	        return;
	      }
	    }

	    for (IVoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
	      try {
	        voiceChannel.join();
	      } catch (MissingPermissionsException e) {
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
