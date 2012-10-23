package jesse.com.game.server;

import jesse.com.game.server.Constants.Facing;
import jesse.com.game.server.Constants.State;

public class MoveCommand extends Command {
	
	private Facing mFacing;
	private State mState;
	
	public MoveCommand(Facing facing, State state, int playerId) {
		mFacing = facing;
		mState = state;
		mPlayerId = playerId;
	}
	
	public int getPlayerId() {
		return mPlayerId;
	}

	@Override
	public void execute(PlayerHolder player) {
		//TODO no collision check yet
		switch(mFacing) {
		case DOWN :
			player.coordinates.y++;
			break;
		case RIGHT :
			player.coordinates.x++;
			break;
		case LEFT :
			player.coordinates.x--;
			break;
		case UP :
			player.coordinates.y--;
			break;
		}
		player.setState(mState);
	}
	
	
	
}
