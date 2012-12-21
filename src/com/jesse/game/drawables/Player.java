package com.jesse.game.drawables;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import com.jesse.data.PlayerHolder;
import com.jesse.game.objects.Vector2i;
import com.jesse.game.utils.Constants.Direction;
import com.jesse.game.utils.Constants.State;

public abstract class Player {

	public Vector2i coordinates;
	public Vector2f drawnCoordinates;
	protected String mName;
	protected int mId = -1;

	protected SpriteSheet mSpriteSheet;
	
	protected State mState = State.IDLE;
	protected Direction mFacing = Direction.DOWN;
	protected Step mStep = Step.IDLE;
	
	//All time in millis
	protected int mMoveProgress = -1;
    protected static final int WalkAnimationTime = 500;
    protected static final int RunAnimationTime = 250;
	
    //Sprite dimensions
    protected static final int CharacterHeight = 21;
    protected static final int CharacterWidth = 16;
        
	protected enum Step {
		ONE, IDLE, TWO
	}
	
//	public Player(String name, int id) throws SlickException {
//		this(name, new Vector2i(), id);
//	}
	
	public Player(PlayerHolder holder) throws SlickException {
		mId = holder.getId();
		mName = holder.getName();
		coordinates = new Vector2i(holder.coordinates);
		drawnCoordinates = new Vector2f(coordinates.x, coordinates.y);			
	}
	
	public void loadCoordinates(Vector2i coordinates) {
		this.coordinates = new Vector2i(coordinates);
		this.drawnCoordinates = new Vector2f(coordinates.x, coordinates.y);
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public void draw() {
		mSpriteSheet.getSprite(mStep.ordinal(), mFacing.ordinal()).draw(drawnCoordinates.x * 16, drawnCoordinates.y * 16 - 5);
	}
	
	protected void setDrawnPosition(float movementAdjustX, float movementAdjustY) {
		drawnCoordinates.x = (coordinates.x + movementAdjustX);
        drawnCoordinates.y = (coordinates.y + movementAdjustY);
	}
	
	protected void endMovement() {
		mState = State.IDLE;
		mMoveProgress = -1;
		mStep = Step.IDLE;
		//is key still down?
	}
	
	public int getId() {
		return mId;
	}
	
	public void setId(int id) {
		mId = id;
	}
	
	protected void updateDraw(int delta) {
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
	
}
