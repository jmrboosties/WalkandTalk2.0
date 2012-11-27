package come.jesse.game.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.jesse.game.GameMain;
import com.jesse.game.utils.Print;

public class LoadingScreen extends BasicGameState {
	
	private int mId;
	
	public LoadingScreen(int id) {
		mId = id;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		Print.log("hi");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics grfx) throws SlickException {
		grfx.draw(new Rectangle(0, 0, GameMain.SCREEN_WIDTH, GameMain.SCREEN_HEIGHT));
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		Print.log("update");
	}

	@Override
	public int getID() {
		return mId;
	}

}
