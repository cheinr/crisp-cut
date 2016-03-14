package com.chein.crispcut.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.chein.crispcut.Assets;
import com.chein.crispcut.Constants;
import com.chein.crispcut.GameScreen;
import com.chein.crispcut.LogCutter;
import com.chein.crispcut.Message;
import com.chein.crispcut.State;
import com.chein.crispcut.actors.Log;

/**
 * Overlay for the game screen.
 * @author Colin
 *
 */
public class Hud extends Group {

	private boolean isGameOver;
	//private String scoreStr;

	private Log log;
	private GameScreen gameScreen;
	private LogCutter game;

	private GamePausedMenu gamePausedMenu;
	//private GameOverMenu gameOverMenu; //TODO maybe redo this class?

	ImageButton pauseButton;
	//GlyphLayout scoreLayout;

	/**
	 * Creates a new Hud.
	 * @param log
	 * @param screen
	 * @param logCutter
	 */
	public Hud(Log log, GameScreen screen, LogCutter logCutter) {
		this.log = log;
		this.gameScreen = screen;
		game = logCutter;
		init();
	}

	private void init() {

		isGameOver = false;

		gamePausedMenu = new GamePausedMenu(gameScreen, game);
		gamePausedMenu.setVisible(false);

		TextureRegionDrawable pauseButtonRegion = new TextureRegionDrawable(
				Assets.instance.assetGUI.pauseButton);
		pauseButton = new ImageButton(pauseButtonRegion, pauseButtonRegion);
		pauseButton.setBounds(0, Constants.VIEWPORT_HEIGHT-50, 50, 50);
		pauseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gamePausedMenu.setVisible(true);
				gameScreen.setState(State.PAUSED);
				if (game.isGameMuted() == false) {
					Assets.instance.beltSound.stop();//Stop() then re-play() as pause() and resume() don't play nicely in android.
				}
				super.clicked(event, x, y);
			}
		});

		addActor(pauseButton);
		addActor(gamePausedMenu);

		
		//scoreLayout = new GlyphLayout(Assets.instance.numbers, "0");
	}

	@Override
	public void act(float deltaTime) {
		super.act(deltaTime);
		if (log.getMessage() == Message.MISS) {
			isGameOver = true;
		}

		if (gamePausedMenu.isVisible())
			gamePausedMenu.act(deltaTime);

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		//scoreStr = Integer.toString(log.getScore());
		//scoreLayout.setText(Assets.instance.numbers, scoreStr);
		
		/*Assets.instance.numbers.draw(batch, scoreLayout,
				160 - scoreLayout.width / 2,
				480);
*/
		pauseButton.draw(batch, parentAlpha);
		if (gamePausedMenu.isVisible())
			gamePausedMenu.draw(batch, parentAlpha);
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void showGamePausedMenu() {
		gamePausedMenu.setVisible(true);
		
	}
}
