package com.jesse.game;

import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.Layer;

import com.jesse.game.drawables.Player;
import com.jesse.game.objects.Vector2i;
import com.jesse.game.tiled.TiledMapUsable;

public class MyNerdoGame extends BasicGame {

	private TiledMapUsable mMap;
	private Layer mCollisionLayer;
	private Player mPlayer;
//	private Music mMusic;
	public static Set<Vector2i> mCollisionTiles;
	
	public static final int SCREN_WIDTH = 1024;
	public static final int SCREEN_HEIGHT = 576;
	public static final int SCALE = 2;
	
	public MyNerdoGame(String title) {
		super(title);		
	}

	@Override
	public void render(GameContainer arg0, Graphics grfx) throws SlickException {
		grfx.scale(SCALE, SCALE);
		mMap.render(0, 0, 2);
		mPlayer.draw();
		mMap.render(0, 0, 3);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		mMap = new TiledMapUsable("res/maps/testslick.tmx", "res/tilesets/");
		mCollisionLayer = mMap.getLayers().get(0);
		mPlayer = new Player("Jesse", new Vector2i());
		mCollisionTiles = new HashSet<Vector2i>();
		for (int i = 0; i < mCollisionLayer.width; i++) 
			for (int j = 0; j < mCollisionLayer.height; j++) 
				if(mCollisionLayer.getTileID(i, j) != 0)
					mCollisionTiles.add(new Vector2i(i, j));
//		mMusic = new Music("res/audio/music/samurai.ogg");
//		mMusic.play();
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		mPlayer.update(gc.getInput(), delta, mCollisionLayer);
	}
	
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new MyNerdoGame("Test"));
		app.setDisplayMode(SCREN_WIDTH, SCREEN_HEIGHT, false);
        app.start();
	}

}
