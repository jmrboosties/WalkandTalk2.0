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

import com.jesse.game.GameMain;

@SuppressWarnings("deprecation")
public class SplashPage extends BasicGameState {
	
	private int mId;
	private Image mLogo;
	
	public GameMain mGame;
	
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
		mGame = (GameMain) game;
		mLogo = new Image("res/images/pokemonlogo.png");
		mStart = new TextField(gc, new TrueTypeFont(new Font("Arial", Font.BOLD, 30), true), 
				GameMain.SCREEN_WIDTH / 4, (int) (GameMain.SCREEN_HEIGHT / 1.25), 100, 100);
		
		mExit = new TextField(gc, new TrueTypeFont(new Font("Arial", Font.BOLD, 30), true), 
				GameMain.SCREEN_WIDTH * 2 / 3, (int) (GameMain.SCREEN_HEIGHT / 1.25), 100, 100);
		
		mStart.setBackgroundColor(null);
		mStart.setBorderColor(null);
		mExit.setBackgroundColor(null);
		mExit.setBorderColor(null);

		setChoice(START);
		
		mStart.setText("Start");
		mExit.setText("Exit");
		
	}
	
//	@Override
//	protected RootPane createRootPane() {
//		RootPane rp = super.createRootPane();
//		rp.setTheme("theme");
//
//		mStart = new Button("Start");
//		mStart.addCallback(new Runnable() {
//			
//			@Override
//			public void run() {
//				mGame.connectToServer();
//			}
//		});
//		
//		mExit = new Button("Exit");
//		mExit.addCallback(new Runnable() {
//			
//			@Override
//			public void run() {
////				System.exit(0);
//				mPopup.openPopupCentered();
//			}
//		});
//		
////		mPopup = new PopupWindow(rp);
////		Button gay = new Button("gay");
////		mPopup.add(gay);
//		
//		rp.add(mStart);
//		rp.add(mExit);
////		rp.add(mPopup);
//		createPopupWindow(rp);
//		return rp;
//	}
//	
//	@Override
//	public void layoutRootPane() {
//		mStart.adjustSize();
//		mExit.adjustSize();
//		
//		mStart.setPosition(GameMain.SCREEN_WIDTH / 3, (int) (GameMain.SCREEN_HEIGHT * 4 / 5));
//		mExit.setPosition(GameMain.SCREEN_WIDTH * 2 / 3, (int) (GameMain.SCREEN_HEIGHT * 4 / 5));
//	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics grfx) throws SlickException {
		grfx.scale(GameMain.SCALE, GameMain.SCALE);
		mLogo.draw(GameMain.SCREEN_WIDTH / 4 - mLogo.getWidth() / 4, GameMain.SCREEN_HEIGHT / 4 - mLogo.getHeight() / 4, .5f);
		
		grfx.scale(.5f, .5f);
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
				mGame.connectToServer();
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
	
//	public void createPopupWindow(RootPane rp){
//	      
//        DialogLayout dialog = new DialogLayout();
//        
//        Button btnOK = new Button("Yes");
//        btnOK.setTheme("button");
//        btnOK.addCallback(new Runnable() {
//            public void run() {
//            	System.exit(0);
//            }
//        });
//        
//        Button btnNo = new Button("No");
//        btnNo.setTheme("button");
//        btnNo.addCallback(new Runnable() {
//            public void run() {
//            	mPopup.closePopup();
//            }
//        });
//        
//        Label loadQuestion = new Label();
//        loadQuestion.setText("Are you sure you want to quit?");
//        
//        DialogLayout.Group popupLabelH = dialog.createSequentialGroup(loadQuestion);
//        DialogLayout.Group popupBoxH = dialog.createSequentialGroup()
//              .addGap()
//              .addWidget(btnOK)
//              .addGap(20)
//              .addWidget(btnNo)
//              .addGap(); 
//        
//        DialogLayout.Group popupLabelV = dialog.createParallelGroup(loadQuestion);
//        DialogLayout.Group popupBoxV = dialog.createParallelGroup(btnOK, btnNo); 
//        
//        dialog.setHorizontalGroup(dialog.createParallelGroup(popupLabelH, popupBoxH));
//        dialog.setVerticalGroup(dialog.createSequentialGroup(popupLabelV, popupBoxV));
//        
//        mPopup = new PopupWindow(rp);
//        mPopup.setTheme("popupmenu");
//        mPopup.add(new ResizableFrame());
//        mPopup.add(dialog);
//        
//   }

}
