package com.jesse.game.states;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.Layer;

import com.jesse.game.GameMain;
import com.jesse.game.data.GameSnapshot;
import com.jesse.game.data.MessageCommand;
import com.jesse.game.data.MoveCommand;
import com.jesse.game.data.PlayerHolder;
import com.jesse.game.drawables.PeerPlayer;
import com.jesse.game.drawables.UserPlayer;
import com.jesse.game.listeners.OnUpdateReceivedListener;
import com.jesse.game.net.ServerSender;
import com.jesse.game.objects.Vector2i;
import com.jesse.game.tiled.TiledMapUsable;
import com.jesse.game.ui.Chatbox;
import com.jesse.game.utils.Print;

public class GameplayState extends BasicGameState implements OnUpdateReceivedListener {

	private int mId;
	
	private GameMain mGame;
	
	private UserPlayer mUserPlayer;
	private HashMap<Integer, PeerPlayer> mPlayers;
	
	private TiledMapUsable mMap;
	private Set<Vector2i> mCollisionTiles;
	
	private Shape mTransitionBlack;
	
	private Chatbox mChatBox;
	
	public GameplayState(int id) {
		mId = id;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		mGame = (GameMain) game;
		mGame.setOnUpdateListener(this);
		mPlayers = new HashMap<Integer, PeerPlayer>();
		PlayerHolder holder = mGame.getUserPlayerHolder();
		mUserPlayer = new UserPlayer(holder.getName(), holder.coordinates, holder.getId());
		mMap = new TiledMapUsable("res/maps/littleplace.tmx", "res/tilesets/");
		Layer collisionLayer = mMap.getLayers().get(0);
		mCollisionTiles = new HashSet<Vector2i>();
		for (int i = 0; i < collisionLayer.width; i++) 
			for (int j = 0; j < collisionLayer.height; j++) 
				if(collisionLayer.getTileID(i, j) != 0)
					mCollisionTiles.add(new Vector2i(i, j));
		
		mChatBox = new Chatbox(gc, 10, (GameMain.SCREEN_HEIGHT * 3 / 4) - 10, GameMain.SCREEN_WIDTH / 4, GameMain.SCREEN_HEIGHT / 4);
		
		mTransitionBlack = new Rectangle(0, 0, GameMain.SCREEN_WIDTH, GameMain.SCREEN_WIDTH);
//		mTransitionBlack.
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics grfx) throws SlickException {
		grfx.scale(GameMain.SCALE, GameMain.SCALE);
		mMap.render(0, 0, 1);
		mMap.render(0, 0, 2);
		
		for (PeerPlayer peer : mPlayers.values())
			peer.draw();
		
		mUserPlayer.draw();
		mMap.render(0, 0, 3);
		
		grfx.scale(.5f, .5f);
		mChatBox.render(gc, grfx);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		
		for (PeerPlayer peer : mPlayers.values())
			peer.update(delta);
		
		for (Entry<PlayerHolder, String> entry : mGame.getMessageQueue().entrySet()) {
			if(entry.getKey() != null)
				mChatBox.addText(entry.getKey().getName() + ": " + entry.getValue());
			else
				mChatBox.addText(entry.getValue());
		}
		
		mGame.getMessageQueue().clear();
		
		if(!mChatBox.isVisible()) {
			MoveCommand command = mUserPlayer.update(gc.getInput(), delta, mCollisionTiles);
			if(command != null)
				new Thread(new ServerSender(mGame.outWriter, command, mUserPlayer.getId())).start();
		}
		
		handleOtherInput(gc.getInput());
	}
	
	private void sendMessage() {
		String textMsg = mChatBox.getTextForMessage();
		if(textMsg != null && textMsg.length() > 0) {
			MessageCommand msgCommand = new MessageCommand(mUserPlayer.getId(), textMsg);
			Print.log("sending command: " + msgCommand.toString());
			new Thread(new ServerSender(mGame.outWriter, msgCommand, mUserPlayer.getId())).start();
		}
	}

	@Override
	public int getID() {
		return mId;
	}

	@Override
	public void onUpdateReceived(GameSnapshot snapshot) {
		updatePlayerArray(snapshot);
	}
	
	private void handleOtherInput(Input input) {
		if(input.isKeyPressed(Keyboard.KEY_Y) && !mChatBox.isVisible())
			mChatBox.setVisibility(true);
		
		if(input.isKeyPressed(Keyboard.KEY_RETURN) && mChatBox.textEntryHasFocus())
			sendMessage();
	}
	
	private void updatePlayerArray(GameSnapshot snapshot) {
		ArrayList<Integer> playersToDump = new ArrayList<Integer>();
		
		int key;
		PlayerHolder holder;
		for (Entry<Integer, PlayerHolder> entry : snapshot.getPlayers().entrySet()) { //THIS THROWS EXCEPTION
			key = entry.getKey();
			if(key == mUserPlayer.getId())
				continue;
			
			holder = entry.getValue();
			
			if(holder != null) {
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
			else {
				mPlayers.remove(key);
				playersToDump.add(key);
//				snapshot.removePlayer(key); //THIS IS THE LINE CAUSING EXCEPTION
			}
		}
		
		for (Integer id : playersToDump) {
			snapshot.removePlayer(id);
		}
		
	}
	
	public void loadPlayer(PlayerHolder holder) throws SlickException {
		if(holder == null)
			mUserPlayer = null;
		else {
			mUserPlayer.setId(holder.getId());
			mUserPlayer.loadCoordinates(holder.coordinates);
			mUserPlayer.setName(holder.getName());
		}
	}
	
	private void changeLevels() {
		
		
	}
	
}
