package com.jesse.game.drawables;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

import com.jesse.game.utils.Print;

@SuppressWarnings("deprecation")
public class TextArea {

	private float mOriginX;
	private float mOriginY;
	private int mWidth;
	private int mHeight;
	
	private Color mBackgroundColor;
	private Color mTextColor = Color.white;
	
	private String mText = "Welcome to chat.";
	private TrueTypeFont mFont;
	
	public TextArea(float x, float y, int width, int height) {
		mOriginX = x;
		mOriginY = y;
		mWidth = width;
		mHeight = height;
		
		mFont = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14), true);
	}
	
	public void addText(String text) {
		Print.log("the text to enter: " + text);
		
		if(mFont.getWidth(text) < mWidth) {
			mText += "\n" + text;
			Print.log("added text normally");
		}
		else {
			StringBuilder builder = new StringBuilder();
			fittingLoop(builder, text);
			mText += builder;
		}
		
		Print.log("total text: " + mText + " :end");
	}
	
	private void fittingLoop(StringBuilder builder, String originalText) {
		String currentLineText = originalText;
		String nextLineText = "";
		while(mFont.getWidth(currentLineText) >= mWidth) {
			Print.log("attempt to shrink: " + currentLineText);
			String[] words = currentLineText.split(" ");
			nextLineText = words[words.length - 1] + " " + nextLineText;
			
			currentLineText = "";
			for (int i = 0; i < words.length - 1; i++) {
				currentLineText += words[i];
				if(i != words.length - 2)
					currentLineText += " ";
			}
		}
				
		Print.log("current line: " + currentLineText);
		Print.log("next line: " + nextLineText);
		
		builder.append("\n" + currentLineText);
		Print.log("builder content: " + builder.toString());
		
		if(nextLineText.length() > 0)
			fittingLoop(builder, nextLineText);
	}
	
	public void setBackgroundColor(Color color) {
		mBackgroundColor = color;
	}
	
	public void render(GameContainer container, Graphics grfx, boolean chatOpen) {
		Color color = grfx.getColor();
		Font font = grfx.getFont();
		
		if(chatOpen) {
			Rectangle oldClip = grfx.getClip();
			grfx.setWorldClip(mOriginX, mOriginY, mWidth, mHeight);

			if (mBackgroundColor != null) {
				grfx.setColor(mBackgroundColor.multiply(color));
				grfx.fillRect(mOriginX, mOriginY, mWidth, mHeight);
			}
			
			grfx.clearWorldClip();
			grfx.setClip(oldClip);
		}
		
		int verticalOffset = mHeight - mText.split("\n").length * grfx.getFont().getLineHeight();
		if(verticalOffset > 0)
			verticalOffset = 0;
		
		grfx.setColor(mTextColor.multiply(color));
		grfx.drawString(mText, mOriginX + 2, mOriginY + verticalOffset);
		
		grfx.setColor(color);
		grfx.setFont(font);
	}
	
}
