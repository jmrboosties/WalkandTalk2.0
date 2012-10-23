package com.jesse.game.drawables;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.Layer;

import com.jesse.game.MyNerdoGame;
import com.jesse.game.objects.Vector2i;

public class Player {

	public Vector2i coordinates;
	public Vector2f drawnCoordinates;
	public String name;	

	private SpriteSheet mSpriteSheet;
	
	private State mState = State.IDLE;
	private Facing mFacing = Facing.DOWN;
	private Step mStep = Step.IDLE;
	
	//All time in millis
	protected int mMoveProgress = -1;
    protected static final int WalkAnimationTime = 500;
    protected static final int RunAnimationTime = 250;
	
    //Sprite dimensions
    private static final int CharacterHeight = 21;
    private static final int CharacterWidth = 16;
        
    //Must be in this order for sprite sheet ordinal
	protected enum Facing {
		DOWN, RIGHT, LEFT, UP
	}
	
	protected enum State {
		IDLE, RUN, WALK
	}
	
	protected enum Step {
		ONE, IDLE, TWO
	}
	
	public Player(String name) throws SlickException {
		this(name, new Vector2i());
	}
	
	public Player(String name, Vector2i coordinates) throws SlickException {
		this.name = name;
		this.coordinates = coordinates;
		this.drawnCoordinates = new Vector2f(coordinates.x, coordinates.y);
		mSpriteSheet = new SpriteSheet("res/spritesheets/karatemansheet.png", CharacterWidth, CharacterHeight);
	}
	
	public void draw() {
		mSpriteSheet.getSprite(mStep.ordinal(), mFacing.ordinal()).draw(drawnCoordinates.x * 16, drawnCoordinates.y * 16 - 5);
	}
	
	public void update(Input input, int delta, Layer layer) {
		if(input.isKeyDown(Input.KEY_W))
			startMovement(Facing.UP, input.isKeyDown(Input.KEY_LSHIFT), layer);
		else if(input.isKeyDown(Input.KEY_S))
			startMovement(Facing.DOWN, input.isKeyDown(Input.KEY_LSHIFT), layer);
		else if(input.isKeyDown(Input.KEY_A))
			startMovement(Facing.LEFT, input.isKeyDown(Input.KEY_LSHIFT), layer);
		else if(input.isKeyDown(Input.KEY_D))
			startMovement(Facing.RIGHT, input.isKeyDown(Input.KEY_LSHIFT), layer);	
		
		switch(mState) {
		case WALK :
		case RUN :
			float time = 0;
			if(mState == State.WALK)
				time = WalkAnimationTime;
			else
				time = RunAnimationTime;
			
			mMoveProgress += delta;
			
			
			if (mMoveProgress <= time / 3)
                mStep = Step.ONE;            
            else if (mMoveProgress <= 2 * time / 3)
                mStep = Step.IDLE;
            else if (mMoveProgress < time)
                mStep = Step.TWO;
            else if(mMoveProgress >= time)
				endMovement();
			
//            if (mMoveProgress >= time)
//            {
//                mMoveProgress = -1;
//                mState = ActionState.Idle;
//                if (!IsStillMoving())
//                {
//                    mStepState = StepState.Idle;
//                }
//            }
			
			float movementAdjustX = 0;
            float movementAdjustY = 0;
			if(mMoveProgress >= 0) {
				switch (mFacing) {
                    case LEFT :
                        movementAdjustX = ((1 - (mMoveProgress / time)));
                        break;
                    case RIGHT :
                        movementAdjustX = -((1 - (mMoveProgress / time)));
                        break;
                    case UP :
                        movementAdjustY = ((1 - (mMoveProgress / time)));
                        break;
                    case DOWN :
                        movementAdjustY = -((1 - (mMoveProgress / time)));
                        break;
                }
			}
			setDrawnPosition(movementAdjustX, movementAdjustY);
			break;
		case IDLE:
			break;
		default:
			break;
		}
	}
	
	private void setDrawnPosition(float movementAdjustX, float movementAdjustY) {
		drawnCoordinates.x = (coordinates.x + movementAdjustX);
        drawnCoordinates.y = (coordinates.y + movementAdjustY);
	}

	protected void startMovement(Facing direction, boolean running, Layer layer) {
		if(mMoveProgress >= 0)
			return;
				
		mFacing = direction;
		
		Vector2i newPos = collisionCheck(mFacing, layer);
		if(newPos == null)
			return;
		
		if(running)
			mState = State.RUN;
		else
			mState = State.WALK;
		
		coordinates = new Vector2i(newPos);		
		mMoveProgress = 0;
	}
	
	private Vector2i collisionCheck(Facing direction, Layer layer) {
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
		if(MyNerdoGame.mCollisionTiles.contains(newPostion))
			return null;
		else
			return newPostion;
	}

	protected void endMovement() {
		mState = State.IDLE;
		mMoveProgress = -1;
		mStep = Step.IDLE;
		//is key still down?
	}
	
	//for derived classes
//	switch(key) {
//	case Input.KEY_W :
//		
//		break;
//	case Input.KEY_S :
//		
//		break;
//	case Input.KEY_A :
//		
//		break;
//	case Input.KEY_D :
//		
//		break;
//	}
	
//	protected int[] determineSprite() {		
//		int[] value = new int[2];
//		int[FACING] = mFacing.ordinal();
//		int[STEP] = m
//		return value;
//	}
	
}
