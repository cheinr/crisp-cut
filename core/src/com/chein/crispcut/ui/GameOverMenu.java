package com.chein.crispcut.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.chein.crispcut.Assets;
import com.chein.crispcut.GameScreen;
import com.chein.crispcut.LogCutter;
import com.chein.crispcut.MainMenuScreen;

/**
 * The menu that appears when the game is lost.
 * @author Colin
 *
 */
public class GameOverMenu extends InputAdapter {
	float x, y, width, height;

	BitmapFont font;
	OrthographicCamera guiCam;
	LogCutter game;

	Vector3 touchPoint;

	NinePatch panelNine;
	NinePatch beigeButton;

	TextureRegion medal;

	String scoreString;
	String highScoreString;

	private boolean mainMenuButtonPressed = false;
	private boolean replayButtonPressed = false;

	// TextureRegion panel;
	TextureRegion backRegion;
	TextureRegion replayRegion;

	// Buttons
	Rectangle mainMenuButton;
	Rectangle replayButton;

	/**
	 * Creates a new Game Over menu.
	 * @param guiCam
	 * @param game
	 * @param width
	 * @param height
	 * @param score
	 * @param highScore
	 */
	public GameOverMenu(OrthographicCamera guiCam, LogCutter game, int width,
			int height, int score, int highScore) { // Constructor for gameOver
													// menu
		Gdx.input.setCatchBackKey(true);

		this.guiCam = guiCam;
		this.game = game;

		this.width = 300;
		this.height = 175;

		this.scoreString = Integer.toString(score);
		this.highScoreString = Integer.toString(highScore);

		mainMenuButton = new Rectangle(x + this.width / 2, y - 50, 100, 50);
		replayButton = new Rectangle(x + this.width - 50, y - 50, 50, 50);

		if (score >= 100) {
			medal = Assets.instance.assetGUI.platinumMedal;
		} else if (score >= 60) {
			medal = Assets.instance.assetGUI.goldMedal;
		} else if (score >= 40) {
			medal = Assets.instance.assetGUI.silverMedal;
		} else if (score >= 20) {
			medal = Assets.instance.assetGUI.bronzeMedal;
		} else {
			medal = Assets.instance.assetGUI.medalInlay;
		}

		init();
	}

	private void init() {
		this.x = guiCam.position.x - guiCam.viewportWidth / 2 - width / 2;
		this.y = guiCam.position.y - guiCam.viewportHeight / 1.5f;

		touchPoint = new Vector3();

		backRegion = Assets.instance.assetGUI.backButton;
		replayRegion = Assets.instance.assetGUI.replayButton;

		panelNine = Assets.instance.assetGUI.yellowPanelNine;
		beigeButton = Assets.instance.assetGUI.beigeButtonUp;
		font = Assets.instance.smallFont;

		update(0);
	}

	
	/**
	 * Updates the menu.
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		this.x = guiCam.position.x - width / 2 + 0.5f;
		this.y = guiCam.position.y - height / 1.5f;

		// TODO - move these
		mainMenuButton.setPosition(x + width / 2 - mainMenuButton.width / 2, y
				- mainMenuButton.height - 5);
		replayButton.setPosition(x + width / 2 - replayButton.width / 2, y
				- replayButton.height - mainMenuButton.height - 10);
	}

	/**
	 * Draws the menu.
	 * @param batch
	 * @param camera
	 */
	public void draw(SpriteBatch batch, OrthographicCamera camera) {

		batch.setProjectionMatrix(guiCam.combined);

		batch.begin();

		Assets.instance.assetGUI.yellowPanelNine.draw(batch, x, y, width,
				height);

		font.draw(batch, "SCORE: ", x + width / 2, y + height - 20);
		Assets.instance.numbers.draw(batch, scoreString, x + width / 2, y
				+ height - 40);

		font.draw(batch, "BEST: ", x + width / 2, y + height / 2.1f);
		Assets.instance.numbers.draw(batch, highScoreString, x + width / 2, y
				+ height - 115);

		font.draw(batch, "MEDAL:", x + 20, y + height - 20);
		batch.draw(medal, x + 20, y + height / 2 - 55, 100, 100);

		if (mainMenuButtonPressed == false) {
			Assets.instance.assetGUI.beigeButtonUp.draw(batch,
					mainMenuButton.getX(), mainMenuButton.getY(),
					mainMenuButton.getWidth(), mainMenuButton.getHeight());
			batch.draw(backRegion, mainMenuButton.getX(),
					mainMenuButton.getY(), mainMenuButton.getWidth(),
					mainMenuButton.getHeight());
		} else {
			Assets.instance.assetGUI.beigeButtonDown.draw(batch,
					mainMenuButton.getX(), mainMenuButton.getY(),
					mainMenuButton.getWidth(), mainMenuButton.getHeight());
			batch.draw(backRegion, mainMenuButton.getX(),
					mainMenuButton.getY() - 2, mainMenuButton.getWidth(),
					mainMenuButton.getHeight());
		}

		if (replayButtonPressed == false) {
			Assets.instance.assetGUI.sqBeigeButtonUp.draw(batch,
					replayButton.getX(), replayButton.getY(),
					replayButton.getWidth(), replayButton.getHeight());
			batch.draw(replayRegion, replayButton.getX(),
					replayButton.getY() + 2, replayButton.getWidth(),
					replayButton.getHeight());
		} else {
			Assets.instance.assetGUI.sqBeigeButtonDown.draw(batch,
					replayButton.getX(), replayButton.getY(),
					replayButton.getWidth(), replayButton.getHeight());
			batch.draw(replayRegion, replayButton.getX(), replayButton.getY(),
					replayButton.getWidth(), replayButton.getHeight());
		}

		batch.end();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		guiCam.unproject(touchPoint.set(screenX, screenY, 0));
		if (replayButton.contains(touchPoint.x, touchPoint.y)) {
			replayButtonPressed = true;
		}
		if (mainMenuButton.contains(touchPoint.x, touchPoint.y)) {
			mainMenuButtonPressed = true;
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		guiCam.unproject(touchPoint.set(screenX, screenY, 0));
		if (replayButton.contains(touchPoint.x, touchPoint.y)
				&& replayButtonPressed == true) {
			if (game.isGameMuted() == false) {
				Assets.instance.beltSound.stop();
			}
			game.setScreen(new GameScreen(game));

		}
		if (mainMenuButton.contains(touchPoint.x, touchPoint.y)
				&& mainMenuButtonPressed == true) {
			// game.setScreen(new MainMenuScreen(game));
			game.setScreenWithTransition(new MainMenuScreen(game), false);
			if (game.isGameMuted() == false) {
				Assets.instance.beltSound.stop();
				Assets.instance.whooshSound.play();
			}
		}
		replayButtonPressed = false;
		mainMenuButtonPressed = false;
		return true;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK) {
			game.setScreenWithTransition(new MainMenuScreen(game), false);
		
			if (game.isGameMuted() == false) {
				Assets.instance.beltSound.stop();
				Assets.instance.whooshSound.play();
			}
		}
		return true;
	}
}
