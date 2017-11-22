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
	@EventSubscriber public void onAllUsersReceivedEvent( AllUsersReceivedEvent event ) {}
	@EventSubscriber public void onGuildCreateEvent( GuildCreateEvent event ) {}
	@EventSubscriber public void onGuildEmojisUpdateEvent( GuildEmojisUpdateEvent event ) {}
	@EventSubscriber public void onGuildEvent( GuildEvent event ) {}
	@EventSubscriber public void onGuildLeaveEvent( GuildLeaveEvent event ) {}
	@EventSubscriber public void onGuildTransferOwnershipEvent( GuildTransferOwnershipEvent event ) {}
	@EventSubscriber public void onGuildUnavailableEvent( GuildUnavailableEvent event ) {}
	@EventSubscriber public void onGuildUpdateEvent( GuildUpdateEvent event ) {}
	
	// Guild Category Events
	@EventSubscriber public void onCategoryCreateEvent( CategoryCreateEvent event ) {}
	@EventSubscriber public void onCategoryDeleteEvent( CategoryDeleteEvent event ) {}
	@EventSubscriber public void onCategoryEvent( CategoryEvent event ) {}
	@EventSubscriber public void onCategoryUpdateEvent( CategoryUpdateEvent event ) {}

	// Guild Channel Events
	@EventSubscriber public void onChannelCategoryUpdateEvent( ChannelCategoryUpdateEvent event ) {}
	@EventSubscriber public void onChannelCreateEvent( ChannelCreateEvent event ) {}
	@EventSubscriber public void onChannelDeleteEvent( ChannelDeleteEvent event ) {}
	@EventSubscriber public void onChannelEvent( ChannelEvent event ) {}
	@EventSubscriber public void onChannelUpdateEvent( ChannelUpdateEvent event ) {}
	@EventSubscriber public void onTypingEvent( TypingEvent event ) {}

	//Guild Channel Message Events
	@EventSubscriber public void onMentionEvent( MentionEvent event ) {}
	@EventSubscriber public void onMessageDeleteEvent( MessageDeleteEvent event ) {}
	@EventSubscriber public void onMessageEditEvent( MessageEditEvent event ) {}
	@EventSubscriber public void onMessageEmbedEvent( MessageEmbedEvent event ) {}
	@EventSubscriber public void onMessageEvent( MessageEvent event ) {}
	@EventSubscriber public void onMessagePinEvent( MessagePinEvent event ) {}
	@EventSubscriber public void onMessageReceivedEvent( MessageReceivedEvent event ) {}
	@EventSubscriber public void onMessageSendEvent( MessageSendEvent event ) {}
	@EventSubscriber public void onMessageUnpinEvent( MessageUnpinEvent event ) {}
	@EventSubscriber public void onMessageUpdateEvent( MessageUpdateEvent event ) {}

	// Guild Channel Reaction Events
	@EventSubscriber public void onReactionAddEvent( ReactionAddEvent event ) {}
	@EventSubscriber public void onReactionEvent( ReactionEvent event ) {}
	@EventSubscriber public void onReactionRemoveEvent( ReactionRemoveEvent event ) {}

	// Guild Channel Webhook Events
	@EventSubscriber public void onWebhookCreateEvent( WebhookCreateEvent event ) {}
	@EventSubscriber public void onWebhookDeleteEvent( WebhookDeleteEvent event ) {}
	@EventSubscriber public void onWebhookEvent( WebhookEvent event ) {}
	@EventSubscriber public void onWebhookUpdateEvent( WebhookUpdateEvent event ) {}

	// Guild Member Events
	@EventSubscriber public void onGuildMemberEvent( GuildMemberEvent event ) {}
	@EventSubscriber public void onNicknameChangedEvent( NicknameChangedEvent event ) {}
	@EventSubscriber public void onUserBanEvent( UserBanEvent event ) {}
	@EventSubscriber public void onUserJoinEvent( UserJoinEvent event ) {}
	@EventSubscriber public void onUserLeaveEvent( UserLeaveEvent event ) {}
	@EventSubscriber public void onUserPardonEvent( UserPardonEvent event ) {}
	@EventSubscriber public void onUserRoleUpdateEvent( UserRoleUpdateEvent event ) {}

	// Guild Role Events
	@EventSubscriber public void onRoleCreateEvent( RoleCreateEvent event ) {}
	@EventSubscriber public void onRoleDeleteEvent( RoleDeleteEvent event ) {}
	@EventSubscriber public void onRoleEvent( RoleEvent event ) {}
	@EventSubscriber public void onRoleUpdateEvent( RoleUpdateEvent event ) {}

	// Guild Voice Events
	@EventSubscriber public void onVoiceChannelCreateEvent( VoiceChannelCreateEvent event ) {}
	@EventSubscriber public void onVoiceChannelDeleteEvent( VoiceChannelDeleteEvent event ) {}
	@EventSubscriber public void onVoiceChannelEvent( VoiceChannelEvent event ) {}
	@EventSubscriber public void onVoiceChannelUpdateEvent( VoiceChannelUpdateEvent event ) {}
	@EventSubscriber public void onVoiceDisconnectedEvent( VoiceDisconnectedEvent event ) {}
	@EventSubscriber public void onVoicePingEvent( VoicePingEvent event ) {}

	// Guild Voice User Events
	@EventSubscriber public void onUserSpeakingEvent( UserSpeakingEvent event ) {}
	@EventSubscriber public void onUserVoiceChannelEvent( UserVoiceChannelEvent event ) {}
	@EventSubscriber public void onUserVoiceChannelJoinEvent( UserVoiceChannelJoinEvent event ) {}
	@EventSubscriber public void onUserVoiceChannelLeaveEvent( UserVoiceChannelLeaveEvent event ) {}
	@EventSubscriber public void onUserVoiceChannelMoveEvent( UserVoiceChannelMoveEvent event ) {}

	// Module Events
	@EventSubscriber public void onModuleDisabledEvent( ModuleDisabledEvent event ) {}
	@EventSubscriber public void onModuleEnabledEvent( ModuleEnabledEvent event ) {}
	@EventSubscriber public void onModuleEvent( ModuleEvent event ) {}
	
	// Shard Events
	@EventSubscriber public void onDisconnectedEvent( DisconnectedEvent event ) {}
	@EventSubscriber public void onLoginEvent( LoginEvent event ) {}
	@EventSubscriber public void onReconnectFailureEvent( ReconnectFailureEvent event ) {}
	@EventSubscriber public void onReconnectSuccessEvent( ReconnectSuccessEvent event ) {}
	@EventSubscriber public void onResumedEvent( ResumedEvent event ) {}
	@EventSubscriber public void onShardEvent( ShardEvent event ) {}
	@EventSubscriber public void onShardReadyEvent( ShardReadyEvent event ) {}
	
	// User Events
	@EventSubscriber public void onPresenceUpdateEvent( PresenceUpdateEvent event ) {}
	@EventSubscriber public void onUserEvent( UserEvent event ) {}
	@EventSubscriber public void onUserUpdateEvent( UserUpdateEvent event ) {}
	
	// Audio Events
	@EventSubscriber public void onAudioPlayerCleanEvent( AudioPlayerCleanEvent event ) {}
	@EventSubscriber public void onAudioPlayerEvent( AudioPlayerEvent event ) {}
	@EventSubscriber public void onAudioPlayerInitEvent( AudioPlayerInitEvent event ) {}
	@EventSubscriber public void onLoopStateChangeEvent( LoopStateChangeEvent event ) {}
	@EventSubscriber public void onPauseStateChangeEvent( PauseStateChangeEvent event ) {}
	@EventSubscriber public void onProcessorAddEvent( ProcessorAddEvent event ) {}
	@EventSubscriber public void onProcessorRemoveEvent( ProcessorRemoveEvent event ) {}
	@EventSubscriber public void onShuffleEvent( ShuffleEvent event ) {}
	@EventSubscriber public void onTrackFinishEvent( TrackFinishEvent event ) {}
	@EventSubscriber public void onTrackQueueEvent( TrackQueueEvent event ) {}
	@EventSubscriber public void onTrackSkipEvent( TrackSkipEvent event ) {}
	@EventSubscriber public void onTrackStartEvent( TrackStartEvent event ) {}
	@EventSubscriber public void onVolumeChangeEvent( VolumeChangeEvent event ) {}

}
