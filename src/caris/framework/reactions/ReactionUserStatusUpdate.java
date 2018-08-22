package caris.framework.reactions;

import caris.framework.basereactions.Reaction;
import caris.framework.library.Variables;
import caris.framework.utilities.Logger;
import sx.blah.discord.handle.obj.IUser;

public class ReactionUserStatusUpdate extends Reaction {
	
	public IUser user;
	public boolean state;
	
	public ReactionUserStatusUpdate(IUser user, boolean state) {
		this(user, state, -1);
	}
	
	public ReactionUserStatusUpdate(IUser user, boolean state, int priority) {
		super(priority);
		this.user = user;
		this.state = state;
	}
	
	@Override
	public void run() {
		Variables.globalUserInfo.get(user).hasGoneOffline = state;
		if( state ) {
			Logger.print(" User [" + user.getName() + "#" + user.getDiscriminator() + "]" + "(" + user.getLongID() + ") has gone offline.");
		} else {
			Logger.print(" User [" + user.getName() + "#" + user.getDiscriminator() + "]" + "(" + user.getLongID() + ") has come online.");
		}
	}
	
}
