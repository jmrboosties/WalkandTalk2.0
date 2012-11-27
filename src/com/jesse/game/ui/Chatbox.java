package com.jesse.game.ui;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;

@SuppressWarnings("deprecation")
public class Chatbox extends UIElement {
	
	private TextField mEntryField;
	private TextArea mChatHistory;
	
	public Chatbox(GameContainer gc, float originX, float originY, int width, int height) {
		super(originX, originY, width, height);
		
		mEntryField = new TextField(gc, new TrueTypeFont(new Font("Arial", Font.BOLD, 20), true), 
				(int)(mBackground.getMinX() + 6), (int)(mBackground.getMaxY() - 28), mWidth - 12, 24);
		
		mChatHistory = new TextArea(mOriginX + 6, mOriginY + 4, mWidth - 12, mHeight - 36);
		
		mEntryField.setBackgroundColor(Color.gray.brighter());
		mChatHistory.setBackgroundColor(Color.gray.brighter());
		
		mEntryField.setBorderColor(null);
	}
	
	public void launch() {
		mEntryField.setFocus(true);
		mVisible = true;
	}
	
	@Override
	public void render(GameContainer gc, Graphics grfx) {		
		if(mVisible) {
			grfx.setColor(Color.gray);
			grfx.fill(mBackground);
			
			grfx.setColor(Color.white);
			mEntryField.render(gc, grfx);
		}
		
		mChatHistory.render(gc, grfx, mVisible);
	}
	
	@Override
	public void setVisibility(boolean visibility) {
		super.setVisibility(visibility);
		if(mVisible)
			mEntryField.setFocus(true);
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
