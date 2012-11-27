package com.jesse.game.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

@SuppressWarnings("deprecation")
public class TextArea {

	private float mOriginX;
	private float mOriginY;
	private int mWidth;
	private int mHeight;
	
	private Color mBackgroundColor;
	private Color mTextColor = Color.white;
	
	private String mText = "";
	private TrueTypeFont mFont;
	
	public TextArea(float x, float y, int width, int height) {
		mOriginX = x;
		mOriginY = y;
		mWidth = width;
		mHeight = height;
		
		mFont = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14), true);
	}
	
	public void addText(String text) {
		if(mFont.getWidth(text) < mWidth)
			if(mText.length() == 0)
				mText += text;
			else
				mText += "\n" + text;
		else {
			StringBuilder builder = new StringBuilder();
			fittingLoop(builder, text);
			mText += builder;
		}
	}
	
	private void fittingLoop(StringBuilder builder, String originalText) {
		String currentLineText = originalText;
		String nextLineText = "";
		while(mFont.getWidth(currentLineText) >= mWidth) {
			String[] words = currentLineText.split(" ");
			nextLineText = words[words.length - 1] + " " + nextLineText;
			
			currentLineText = "";
			for (int i = 0; i < words.length - 1; i++) {
				currentLineText += words[i];
				if(i != words.length - 2)
					currentLineText += " ";
			}
		}
				
		if(builder.toString().length() == 0)
			builder.append(currentLineText);
		else
			builder.append("\n" + currentLineText);
		
		if(nextLineText.length() > 0)
			fittingLoop(builder, nextLineText);
	}
	
	public void setBackgroundColor(Color color) {
		mBackgroundColor = color;
	}
	
	public void render(GameContainer container, Graphics grfx, boolean drawBackground) {
		Color color = grfx.getColor();
		Font font = grfx.getFont();
		Rectangle oldClip = grfx.getClip();
		
		grfx.setWorldClip(mOriginX, mOriginY, mWidth, mHeight);
		
		if(drawBackground && mBackgroundColor != null) {
			grfx.setColor(mBackgroundColor.multiply(color));
			grfx.fillRect(mOriginX, mOriginY, mWidth, mHeight);
		}
		
		int verticalOffset = mHeight - mText.split("\n").length * grfx.getFont().getLineHeight();
		if(verticalOffset > 0)
			verticalOffset = 0;
		
		grfx.setColor(mTextColor.multiply(color));
		grfx.drawString(mText, mOriginX + 2, mOriginY + verticalOffset);
		
		grfx.clearWorldClip();
		grfx.setClip(oldClip);
		grfx.setColor(color);
		grfx.setFont(font);
	}
	
}
