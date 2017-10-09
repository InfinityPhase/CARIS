import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public interface Command {
	// Creates an interface for mapping commands.
	// NOTE: Needs to be reviewed for comprehension
	void runCommand(MessageReceivedEvent event, List<String> args);
}
