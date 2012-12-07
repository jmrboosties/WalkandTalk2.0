package com.jesse.game.ui;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;

import com.jesse.game.listeners.OnEnterPressedListener;

@SuppressWarnings("deprecation")
public class EntryPopup extends UIElement {

	private TextArea mCaption;
	private TextField mEntryField;
	
	private int mId = -1;
	
	public OnEnterPressedListener mListener;
	
	public EntryPopup(GameContainer gc, int id, float x, float y, int width, int height) {
		super(x, y, width, height);

		mId = id;
		
		mEntryField = new TextField(gc, new TrueTypeFont(new Font("Arial", Font.BOLD, 20), true), 
				(int)(mBackground.getMinX() + 6), (int)(mBackground.getMaxY() - 28), mWidth - 12, 24);
		
		mEntryField.setBackgroundColor(Color.gray.brighter());
		mEntryField.setBorderColor(null);
		
		mCaption = new TextArea(gc, mOriginX + 6, mOriginY + 4, mWidth - 12, mHeight - 36);
		mCaption.setBackgroundColor(null);
	}

	@Override
	public void render(GameContainer gc, Graphics grfx) {
		if(mVisible) {
			grfx.setColor(Color.gray);
			grfx.fill(mBackground);
			
			grfx.setColor(Color.white);
			mEntryField.render(gc, grfx);
			mCaption.render(gc, grfx, false);
		}
	}
	
	public void setText(String text) {
		mCaption.addText(text);
	}
	
	public void enterPressed() {
		if(mListener.onEnterPressed(mId, mEntryField.getText()))
			mEntryField.setText("");
	}
	
	@Override
	public void setVisibility(boolean visibility) {
		super.setVisibility(visibility);
		mEntryField.setText("");
	}
	
	public void launch() {
		setVisibility(true);
		mEntryField.setFocus(true);
	}
	
	public void setOnEnterPressedListener(OnEnterPressedListener listener) {
		mListener = listener;
	}
	
	public boolean textEntryHasFocus() {
		return mEntryField.hasFocus();
	}
	
	public String getEntryText() {
		return mEntryField.getText();
	}

}
