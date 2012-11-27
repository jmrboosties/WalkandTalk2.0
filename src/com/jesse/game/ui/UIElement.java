package com.jesse.game.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.RoundedRectangle;

public abstract class UIElement {
	
	protected boolean mVisible = false;
	
	protected float mOriginX;
	protected float mOriginY;
	
	protected int mWidth;
	protected int mHeight;
	
	protected RoundedRectangle mBackground;
	
	public UIElement(float x, float y, int width, int height) {
		mOriginX = x;
		mOriginY = y;
		
		mWidth = width;
		mHeight = height;
		
		mBackground = new RoundedRectangle(mOriginX, mOriginY, mWidth, mHeight, 10);
	}
	
	public void setVisibility(boolean visibilty) {
		mVisible = visibilty;
	}
	
	public boolean isVisible() {
		return mVisible;
	}
	
	public abstract void render(GameContainer gc, Graphics grfx);
}
