package com.jesse.game;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class SlickExample {

	private Texture mTexture;
	private Audio mMusic;
	private TextField text;
	
	public void start() {
		initGL(800, 600);
		init();
		
		while(true) {
			update();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			render();
			
			Display.update();
			Display.sync(100);
			
			if(Display.isCloseRequested()) {
				Display.destroy();
				AL.destroy();
				System.exit(0);
			}
			
		}
	}

	private void initGL(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			Display.setVSyncEnabled(true);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glClearColor(0f, 0f, 0f, 0f);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glViewport(0, 0, width, height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		
	}

	private void init() {
		try {
			
			mMusic = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource("res/audio/music/samurai.ogg"));
			mMusic.playAsMusic(1f, 1f, true);
			
			mTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/images/turtles.jpg"));
			
			System.out.println("Texture loaded: " + mTexture);
			System.out.println(">> Image width: " + mTexture.getImageWidth());
			System.out.println(">> Image height: " + mTexture.getImageHeight());
			System.out.println(">> Texture width: " + mTexture.getTextureWidth());
			System.out.println(">> Texture height: " + mTexture.getTextureHeight());
			System.out.println(">> Texture ID: " + mTexture.getTextureID());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void render() {
		Color.white.bind();
		mTexture.bind();
		
		Vector2f bounds = calcBounds(mTexture);
		
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(100, 100);
		GL11.glTexCoord2f(bounds.x, 0);
		GL11.glVertex2f(100 + mTexture.getImageWidth(), 100);
		GL11.glTexCoord2f(bounds.x, bounds.y);
		GL11.glVertex2f(100 + mTexture.getImageWidth(), 100 + mTexture.getImageHeight());
		GL11.glTexCoord2f(0, bounds.y);
		GL11.glVertex2f(100, 100 + mTexture.getImageHeight());
		
		GL11.glEnd();
	}
	
	private Vector2f calcBounds(Texture texture) {
		Vector2f vector = new Vector2f();
		vector.x = (float)mTexture.getImageWidth() / (float)mTexture.getTextureWidth();
		vector.y = (float)mTexture.getImageHeight() / (float)mTexture.getTextureHeight();
		return vector;
	}
	
	private void update() {
		while (Keyboard.next())
			if (Keyboard.getEventKey() == Keyboard.KEY_W)
				if (Keyboard.getEventKeyState())
					System.out.println("W is pressed");
		
		
		SoundStore.get().poll(0);
	}
	
	public static void main(String[] args) {
		SlickExample example = new SlickExample();
		example.start();
	}
	
}
