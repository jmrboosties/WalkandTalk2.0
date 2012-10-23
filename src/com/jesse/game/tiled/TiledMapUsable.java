package com.jesse.game.tiled;

import java.io.InputStream;
import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.Layer;
import org.newdawn.slick.tiled.TiledMap;

public class TiledMapUsable extends TiledMap {

	public TiledMapUsable(InputStream in) throws SlickException {
		super(in);
	}
	
	public TiledMapUsable(InputStream in, String location) throws SlickException {
		super(in, location);
	}
	
	public TiledMapUsable(String ref) throws SlickException {
		super(ref);
	}
	
	public TiledMapUsable(String ref, boolean loadTileSets) throws SlickException {
		super(ref, loadTileSets);
	}
	
	public TiledMapUsable(String ref, String location) throws SlickException {
		super(ref, location);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Layer> getLayers() {
		return layers;
	}
	
}
