package commands;

import sx.blah.discord.api.events.EventSubscriber;

import sx.blah.discord.handle.impl.events.guild.*;
import sx.blah.discord.handle.impl.events.guild.category.*;
import sx.blah.discord.handle.impl.events.guild.channel.*;
import sx.blah.discord.handle.impl.events.guild.channel.message.*;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.*;
import sx.blah.discord.handle.impl.events.guild.channel.webhook.*;
import sx.blah.discord.handle.impl.events.guild.member.*;
import sx.blah.discord.handle.impl.events.guild.role.*;
import sx.blah.discord.handle.impl.events.guild.voice.*;
import sx.blah.discord.handle.impl.events.guild.voice.user.*;
import sx.blah.discord.handle.impl.events.module.*;
import sx.blah.discord.handle.impl.events.shard.*;
import sx.blah.discord.handle.impl.events.user.*;

import sx.blah.discord.util.audio.events.*;

public class SuperEvent {
	// A catch all class for events.
	
    // Guild Events
	@EventSubscriber public void name( AllUsersReceivedEvent event ) {}
	@EventSubscriber public void name( GuildCreateEvent event ) {}
	@EventSubscriber public void name( GuildEmojisUpdateEvent event ) {}
	@EventSubscriber public void name( GuildEvent event ) {}
	@EventSubscriber public void name( GuildLeaveEvent event ) {}
	@EventSubscriber public void name( GuildTransferOwnershipEvent event ) {}
	@EventSubscriber public void name( GuildUnavailableEvent event ) {}
	@EventSubscriber public void name( GuildUpdateEvent event ) {}
	
	// Guild Category Events
	@EventSubscriber public void name( CategoryCreateEvent event ) {}
	@EventSubscriber public void name( CategoryDeleteEvent event ) {}
	@EventSubscriber public void name( CategoryEvent event ) {}
	@EventSubscriber public void name( CategoryUpdateEvent event ) {}

	// Guild Channel Events
	@EventSubscriber public void name( ChannelCategoryUpdateEvent event ) {}
	@EventSubscriber public void name( ChannelCreateEvent event ) {}
	@EventSubscriber public void name( ChannelDeleteEvent event ) {}
	@EventSubscriber public void name( ChannelEvent event ) {}
	@EventSubscriber public void name( ChannelUpdateEvent event ) {}
	@EventSubscriber public void name( TypingEvent event ) {}

	//Guild Channel Message Events
	@EventSubscriber public void name( MentionEvent event ) {}
	@EventSubscriber public void name( MessageDeleteEvent event ) {}
	@EventSubscriber public void name( MessageEditEvent event ) {}
	@EventSubscriber public void name( MessageEmbedEvent event ) {}
	@EventSubscriber public void name( MessageEvent event ) {}
	@EventSubscriber public void name( MessagePinEvent event ) {}
	@EventSubscriber public void name( MessageReceivedEvent event ) {}
	@EventSubscriber public void name( MessageSendEvent event ) {}
	@EventSubscriber public void name( MessageUnpinEvent event ) {}
	@EventSubscriber public void name( MessageUpdateEvent event ) {}

	// Guild Channel Reaction Events
	@EventSubscriber public void name( ReactionAddEvent event ) {}
	@EventSubscriber public void name( ReactionEvent event ) {}
	@EventSubscriber public void name( ReactionRemoveEvent event ) {}

	// Guild Channel Webhook Events
	@EventSubscriber public void name( WebhookCreateEvent event ) {}
	@EventSubscriber public void name( WebhookDeleteEvent event ) {}
	@EventSubscriber public void name( WebhookEvent event ) {}
	@EventSubscriber public void name( WebhookUpdateEvent event ) {}

	// Guild Member Events
	@EventSubscriber public void name( GuildMemberEvent event ) {}
	@EventSubscriber public void name( NicknameChangedEvent event ) {}
	@EventSubscriber public void name( UserBanEvent event ) {}
	@EventSubscriber public void name( UserJoinEvent event ) {}
	@EventSubscriber public void name( UserLeaveEvent event ) {}
	@EventSubscriber public void name( UserPardonEvent event ) {}
	@EventSubscriber public void name( UserRoleUpdateEvent event ) {}

	// Guild Role Events
	@EventSubscriber public void name( RoleCreateEvent event ) {}
	@EventSubscriber public void name( RoleDeleteEvent event ) {}
	@EventSubscriber public void name( RoleEvent event ) {}
	@EventSubscriber public void name( RoleUpdateEvent event ) {}

	// Guild Voice Events
	@EventSubscriber public void name( VoiceChannelCreateEvent event ) {}
	@EventSubscriber public void name( VoiceChannelDeleteEvent event ) {}
	@EventSubscriber public void name( VoiceChannelEvent event ) {}
	@EventSubscriber public void name( VoiceChannelUpdateEvent event ) {}
	@EventSubscriber public void name( VoiceDisconnectedEvent event ) {}
	@EventSubscriber public void name( VoicePingEvent event ) {}

	// Guild Voice User Events
	@EventSubscriber public void name( UserSpeakingEvent event ) {}
	@EventSubscriber public void name( UserVoiceChannelEvent event ) {}
	@EventSubscriber public void name( UserVoiceChannelJoinEvent event ) {}
	@EventSubscriber public void name( UserVoiceChannelLeaveEvent event ) {}
	@EventSubscriber public void name( UserVoiceChannelMoveEvent event ) {}

	// Module Events
	@EventSubscriber public void name( ModuleDisabledEvent event ) {}
	@EventSubscriber public void name( ModuleEnabledEvent event ) {}
	@EventSubscriber public void name( ModuleEvent event ) {}
	
	// Shard Events
	@EventSubscriber public void name( DisconnectedEvent event ) {}
	@EventSubscriber public void name( LoginEvent event ) {}
	@EventSubscriber public void name( ReconnectFailureEvent event ) {}
	@EventSubscriber public void name( ReconnectSuccessEvent event ) {}
	@EventSubscriber public void name( ResumedEvent event ) {}
	@EventSubscriber public void name( ShardEvent event ) {}
	@EventSubscriber public void name( ShardReadyEvent event ) {}
	
	// User Events
	@EventSubscriber public void name( PresenceUpdateEvent event ) {}
	@EventSubscriber public void name( UserEvent event ) {}
	@EventSubscriber public void name( UserUpdateEvent event ) {}
	
	// Audio Events
	@EventSubscriber public void name( AudioPlayerCleanEvent event ) {}
	@EventSubscriber public void name( AudioPlayerEvent event ) {}
	@EventSubscriber public void name( AudioPlayerInitEvent event ) {}
	@EventSubscriber public void name( LoopStateChangeEvent event ) {}
	@EventSubscriber public void name( PauseStateChangeEvent event ) {}
	@EventSubscriber public void name( ProcessorAddEvent event ) {}
	@EventSubscriber public void name( ProcessorRemoveEvent event ) {}
	@EventSubscriber public void name( ShuffleEvent event ) {}
	@EventSubscriber public void name( TrackFinishEvent event ) {}
	@EventSubscriber public void name( TrackQueueEvent event ) {}
	@EventSubscriber public void name( TrackSkipEvent event ) {}
	@EventSubscriber public void name( TrackStartEvent event ) {}
	@EventSubscriber public void name( VolumeChangeEvent event ) {}

}
