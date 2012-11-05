package come.jesse.game.states;

import java.awt.Font;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.jesse.game.Game;

@SuppressWarnings("deprecation")
public class SplashPage extends BasicGameState {
	
	private int mId;
	private Image mLogo;
	
	private TextField mStart;
	private TextField mExit;
	
	private int mSelectedOption;
	
	private static final int START = 0;
	private static final int EXIT = 1;

	public SplashPage(int id) {
		mId = id;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		mLogo = new Image("res/images/pokemonlogo.png");
		mStart = new TextField(gc, new TrueTypeFont(new Font("Arial", Font.BOLD, 14), true), 
				Game.SCREEN_WIDTH / 8, (int) (Game.SCREEN_HEIGHT / 2.5), 100, 100);
		
		mExit = new TextField(gc, new TrueTypeFont(new Font("Arial", Font.BOLD, 14), true), 
				Game.SCREEN_WIDTH * 11 / 32, (int) (Game.SCREEN_HEIGHT / 2.5), 100, 100);
		
		mStart.setBackgroundColor(null);
		mStart.setBorderColor(null);
		mExit.setBackgroundColor(null);
		mExit.setBorderColor(null);

		setChoice(START);
		
//		mExit = new TextField(gc, new TrueTypeFont(new Font("Arial", Font.BOLD, 14), true), 
//				Game.SCREEN_WIDTH / 4 + mLogo.getWidth() / 4, 
//				Game.SCREEN_HEIGHT / 4 - mLogo.getHeight() / 4, 100, 100);
		
		mStart.setText("Start");
		mExit.setText("Exit");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics grfx) throws SlickException {
		grfx.scale(Game.SCALE, Game.SCALE);
		mLogo.draw(Game.SCREEN_WIDTH / 4 - mLogo.getWidth() / 4, Game.SCREEN_HEIGHT / 4 - mLogo.getHeight() / 4, .5f);
		mStart.render(gc, grfx);
		mExit.render(gc, grfx);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		if(gc.getInput().isKeyDown(Keyboard.KEY_A)) {
			setChoice(START);
		}
		else if(gc.getInput().isKeyDown(Keyboard.KEY_D)) {
			setChoice(EXIT);
		}
		else if(gc.getInput().isKeyDown(Keyboard.KEY_RETURN)) {
			if(mSelectedOption == EXIT)
				System.exit(0);
			else if(mSelectedOption == START) {
				//handle
			}
		}
	}

	@Override
	public int getID() {
		return mId;
	}
	
	private void setChoice(int choice) {
//		if(mSelectedOption == choice)
//			return;
		
		switch(choice) {
		case START :
			mStart.setTextColor(Color.yellow);
			mExit.setTextColor(Color.white);
			break;
		case EXIT :
			mExit.setTextColor(Color.yellow);
			mStart.setTextColor(Color.white);
			break;
		}
		
		mSelectedOption = choice;
	}

}
