package com.jesse.game.drawables;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.gui.TextField;

@SuppressWarnings("deprecation")
public class Chatbox {
	
	private boolean mVisible = false;
	
	private float mOriginX;
	private float mOriginY;
	
	private int mWidth;
	private int mHeight;
	
	private RoundedRectangle mBackground;
	private TextField mEntryField;
	private TextArea mChatHistory;
	
	public Chatbox(GameContainer gc, float originX, float originY, int width, int height) {
		mOriginX = originX;
		mOriginY = originY;
		
		mWidth = width;
		mHeight = height;
		
		mBackground = new RoundedRectangle(mOriginX, mOriginY, mWidth, mHeight, 10);
		mEntryField = new TextField(gc, new TrueTypeFont(new Font("Arial", Font.BOLD, 20), true), 
				(int)(mBackground.getMinX() + 6), (int)(mBackground.getMaxY() - 28), mWidth - 12, 24);
		
		mChatHistory = new TextArea(mOriginX + 6, mOriginY + 4, mWidth - 12, mHeight - 36);
		
		mEntryField.setBackgroundColor(Color.gray.brighter());
		mChatHistory.setBackgroundColor(Color.gray.brighter());
		
		mEntryField.setBorderColor(null);
		
//		mChatHistory.setText("At an OS level, I just feel like this was a system designed by like 5 different cliques of people who had a really fucked up view of how people play games. Like, this is totally evident in the way that the 3DS and WiiU and Wii electronic stores are completely independent and cannot interact with each other.");
	}
	
	public void launch() {
		mEntryField.setFocus(true);
		mVisible = true;
	}
	
	public void draw(GameContainer gc, Graphics grfx) {
		if(mVisible) {
			grfx.setColor(Color.gray);
			grfx.fill(mBackground);
			
			grfx.setColor(Color.white);
			mEntryField.render(gc, grfx);
		}
		mChatHistory.render(gc, grfx, mVisible);
	}
	
	public void setVisibility(boolean visibilty) {
		mVisible = visibilty;
		if(mVisible)
			mEntryField.setFocus(true);
	}
	
	public boolean isVisible() {
		return mVisible;
	}
	
	public boolean textEntryHasFocus() {
		return mEntryField.hasFocus();
	}
	
	public String getTextForMessage() {
		String msg = mEntryField.getText();
		mEntryField.setText("");
		mEntryField.setFocus(false);
		mVisible = false;
		return msg;
	}
	
	public void addText(String text) {
		mChatHistory.addText(text);
	}

}
