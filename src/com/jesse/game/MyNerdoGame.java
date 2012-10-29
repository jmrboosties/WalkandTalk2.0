package com.jesse.game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.Layer;

import com.jesse.game.data.GameState;
import com.jesse.game.data.JoinCommand;
import com.jesse.game.data.MoveCommand;
import com.jesse.game.data.PlayerHolder;
import com.jesse.game.drawables.PeerPlayer;
import com.jesse.game.drawables.UserPlayer;
import com.jesse.game.net.ServerReceiver;
import com.jesse.game.net.ServerSender;
import com.jesse.game.objects.Vector2i;
import com.jesse.game.tiled.TiledMapUsable;

public class MyNerdoGame extends BasicGame {

	private TiledMapUsable mMap;
	private Layer mCollisionLayer;
	private UserPlayer mPlayer;
	private Music mMusic;
	public static Set<Vector2i> mCollisionTiles;
	private GameState mGameState;
	private GameState mUpdateState;
	private PlayerHolder mThisPlayer;
	
	private HashMap<Integer, PeerPlayer> mPlayers;
	
	private PrintWriter mOut;
	
	public static final int SCREN_WIDTH = 1024;
	public static final int SCREEN_HEIGHT = 576;
	public static final int SCALE = 2;
	
	public MyNerdoGame(String title) {
		super(title);		
	}

	@Override
	public void render(GameContainer arg0, Graphics grfx) throws SlickException {
		grfx.scale(SCALE, SCALE);
		mMap.render(0, 0, 1);
		mMap.render(0, 0, 2);
		for (PeerPlayer peer : mPlayers.values()) {
			peer.draw();
		}
		mPlayer.draw();
		mMap.render(0, 0, 3);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		mGameState = new GameState();
		mThisPlayer = new PlayerHolder(1, new Vector2i(17, 16), "meh");
		mGameState.addPlayer(mThisPlayer);
		mPlayers = new HashMap<Integer, PeerPlayer>();
		
		Socket socket = null;
		BufferedReader in = null;
		
		try {
			socket = new Socket("localhost", 7377);
			mOut = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		new Thread(new ServerReceiver(in, this)).start();
		
		JoinCommand join = new JoinCommand(mThisPlayer);
		mOut.println(join.getGson());
		
		mMap = new TiledMapUsable("res/maps/littleplace.tmx", "res/tilesets/");
		mCollisionLayer = mMap.getLayers().get(0);
		mPlayer = new UserPlayer(mThisPlayer.getName(), mThisPlayer.coordinates, mThisPlayer.getId());
		mCollisionTiles = new HashSet<Vector2i>();
		for (int i = 0; i < mCollisionLayer.width; i++) 
			for (int j = 0; j < mCollisionLayer.height; j++) 
				if(mCollisionLayer.getTileID(i, j) != 0)
					mCollisionTiles.add(new Vector2i(i, j));
//		mMusic = new Music("res/audio/music/godhand.wav");
//		mMusic.play();
		
	}

	@Override
	public synchronized void update(GameContainer gc, int delta) throws SlickException {
//		while(mUpdateState == null) {
//			try {
//				wait();
//			} catch(InterruptedException e) { }
//		}
//		mGameState.update(mUpdateState);
//		mUpdateState = null;
//		notifyAll();
		
		if(mUpdateState != null) {
			mGameState.update(mUpdateState);
			updatePlayerArray();
			mUpdateState = null;
			notifyAll();
		}
		
		for (PeerPlayer peer : mPlayers.values()) {
			peer.update(delta);
		}
		
		MoveCommand command = mPlayer.update(gc.getInput(), delta, mCollisionLayer);
		if(command != null)
			new Thread(new ServerSender(mOut, command, mPlayer.getId())).start();
	}
	
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new MyNerdoGame("Test"));
		app.setDisplayMode(SCREN_WIDTH, SCREEN_HEIGHT, false);
        app.start();
	}
	
	public void setUpdateState(GameState update) {
		mUpdateState = update;
	}
	
	public GameState getUpdateState() {
		return mUpdateState;
	}
	
	private void updatePlayerArray() {
		int key;
		PlayerHolder holder;
		for (Entry<Integer, PlayerHolder> entry : mGameState.getPlayers().entrySet()) {			
			key = entry.getKey();
			if(key == mPlayer.getId())
				continue;
			
			holder = entry.getValue();

			if(mPlayers.containsKey(key)) 
				mPlayers.get(key).updatePosition(holder);
			else {
				try {
					PeerPlayer newPeer = new PeerPlayer(holder);
					mPlayers.put(key, newPeer);
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
