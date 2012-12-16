package com.jesse.game.drawables;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.jesse.game.data.PlayerHolder;
import com.jesse.game.objects.Vector2i;

public class PeerPlayer extends Player {

//	public PeerPlayer(String name, int id) throws SlickException {
//		super(name, id);
//	}
	
//	public PeerPlayer(String name, Vector2i coords, int id) throws SlickException {
//		super(name, coords, id);
//	}
	
	public PeerPlayer(PlayerHolder holder) throws SlickException {
		super(holder);
		mSpriteSheet = new SpriteSheet("res/spritesheets/hikersheet.png", CharacterWidth, CharacterHeight);
	}
	
	public void updatePosition(PlayerHolder holder) {
		if(holder.coordinates.touching(coordinates)) {
			mFacing = coordinates.getDirectionToCoordinate(holder.coordinates);
			coordinates = new Vector2i(holder.coordinates);
			mState = holder.getState();
			mMoveProgress = 0;
		}
		else {
			//coordinates are not touching, oh no!
		}
	}
	
	public void update(int delta) {
		updateDraw(delta);
	}
}
