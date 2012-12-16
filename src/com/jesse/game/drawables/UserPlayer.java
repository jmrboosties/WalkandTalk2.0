package com.jesse.game.drawables;

import java.util.Set;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.jesse.game.data.PlayerHolder;
import com.jesse.game.data.commands.MoveCommand;
import com.jesse.game.objects.Vector2i;
import com.jesse.game.utils.Constants.Direction;
import com.jesse.game.utils.Constants.State;

public class UserPlayer extends Player {

//	public UserPlayer() throws SlickException {
//		this(null, null, -1);
//	}
	
//	public UserPlayer(String name, int id) throws SlickException {
//		super(name, id);
//	}
	
	public UserPlayer(PlayerHolder holder) throws SlickException {
		super(holder);
		mSpriteSheet = new SpriteSheet("res/spritesheets/karatemansheet.png", CharacterWidth, CharacterHeight);
	}

	public MoveCommand update(Input input, int delta, Set<Vector2i> layer, int mapId) {
		MoveCommand command = null;
		if(input.isKeyDown(Input.KEY_W))
			command = startMovement(Direction.UP, input.isKeyDown(Input.KEY_LSHIFT), layer, mapId);
		else if(input.isKeyDown(Input.KEY_S))
			command = startMovement(Direction.DOWN, input.isKeyDown(Input.KEY_LSHIFT), layer, mapId);
		else if(input.isKeyDown(Input.KEY_A))
			command = startMovement(Direction.LEFT, input.isKeyDown(Input.KEY_LSHIFT), layer, mapId);
		else if(input.isKeyDown(Input.KEY_D))
			command = startMovement(Direction.RIGHT, input.isKeyDown(Input.KEY_LSHIFT), layer, mapId);	
		
		updateDraw(delta);
		
		return command;
	}
	
	protected MoveCommand startMovement(Direction direction, boolean running, Set<Vector2i> tiles, int mapId) {
		if(mMoveProgress >= 0)
			return null;
				
		mFacing = direction;
		
		Vector2i newPos = collisionCheck(mFacing, tiles);
		if(newPos == null)
			return null;
		
		if(running)
			mState = State.RUN;
		else
			mState = State.WALK;
		
		coordinates = new Vector2i(newPos);		
		mMoveProgress = 0;
		
		return new MoveCommand(direction, mState, mId, mapId);
	}
	
	private Vector2i collisionCheck(Direction direction, Set<Vector2i> tiles) {
		Vector2i newPostion = new Vector2i(coordinates);
		switch(mFacing) {
		case UP :
			newPostion.y--;
			break;
		case DOWN :
			newPostion.y++;
			break;
		case LEFT :
			newPostion.x--;
			break;
		case RIGHT :
			newPostion.x++;
			break;
		}
				
//		if(newPostion.x * 16 * MyNerdoGame.SCALE >= MyNerdoGame.SCREN_WIDTH || newPostion.x < 0
//				|| newPostion.y * 16 * MyNerdoGame.SCALE >= MyNerdoGame.SCREEN_HEIGHT || newPostion.y < 0)
//			return null;
//		
//		int tileId = layer.getTileID((int)newPostion.x, (int)newPostion.y);
//		if(tileId != 0)
		if(tiles.contains(newPostion))
			return null;
		else
			return newPostion;
	}
	
//	public boolean isInitialized() {
//		return mName != null && mId >= 0 && coordinates != null && drawnCoordinates != null;
//	}
	
}
