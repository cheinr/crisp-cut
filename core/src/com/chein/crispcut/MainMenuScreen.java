package com.chein.crispcut;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.chein.crispcut.ui.HelpMenu;

public class MainMenuScreen extends ScreenAdapter implements InputProcessor {

	LogCutter game;
	OrthographicCamera guiCam;

	Vector3 touchPoint;

	boolean playButtonPressed = false;
	boolean rateButtonPressed = false;
	boolean highScoresButtonPressed = false;

	Label title;

	Stage buttons;
	Stage stage;
	InputMultiplexer inputMultiplexer;

	// Buttons
	Rectangle play;
	Rectangle rate;
	Rectangle mute;
	// Rectangle highScores;

	ImageButton achievementsButton;
	ImageButton highScoresButton;
	ImageButton helpButton;

	TextureRegion playText;
	NinePatch playButtonPatch;

	private HelpMenu helpMenu;

	int buttonWidth = 125;
	int buttonHeight = 50;
	int buttonGap = 15; // gap between buttons

	public MainMenuScreen(LogCutter game) {

		this.game = game;

		game.actionResolver.loginGPGS();
		
		
		game.setGameMuted(true); //TODO remove this for android

		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);

		this.play = new Rectangle(320 / 2 - (buttonWidth / 2), 220,
				buttonWidth, buttonHeight);
		/*this.play = new Rectangle(320 / 2 - (buttonWidth / 2), 240,
				buttonWidth, buttonHeight);*/
		this.rate = new Rectangle(320 / 2 - (buttonWidth / 2), 240
				- (buttonHeight * 2) - buttonGap * 2, buttonWidth, buttonHeight);
		this.mute = new Rectangle(320 - 60, 17, 32, 32);

		this.playText = Assets.instance.assetGUI.playButton;
		this.playButtonPatch = Assets.instance.assetGUI.beigeButtonUp;

		LabelStyle style = new LabelStyle(Assets.instance.titleFont,
				Color.WHITE);
		title = new Label("CRISP CUT", style);
		title.setPosition(60, 320);

		touchPoint = new Vector3();

		stage = new Stage(new StretchViewport(Constants.VIEWPORT_WIDTH,
				Constants.VIEWPORT_HEIGHT), game.batch);
		helpMenu = new HelpMenu();
		stage.addActor(helpMenu);

		init();

		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.input.setCatchBackKey(false);
	}

	private void init() {

		TextureRegionDrawable achievementsButtonUp = new TextureRegionDrawable(
				Assets.instance.assetGUI.achievementsButtonUp);
		TextureRegionDrawable achievementsButtonDown = new TextureRegionDrawable(
				Assets.instance.assetGUI.achievementsButtonDown);

		TextureRegionDrawable highScoresButtonUp = new TextureRegionDrawable(
				Assets.instance.assetGUI.highScoresButtonUp);
		TextureRegionDrawable highScoresButtonDown = new TextureRegionDrawable(
				Assets.instance.assetGUI.highScoresButtonDown);

		achievementsButton = new ImageButton(achievementsButtonUp,
				achievementsButtonDown);
		achievementsButton.setBounds(320 / 2 - (buttonWidth / 2), 240 - buttonHeight
				- buttonGap, 50, 50);
		achievementsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.actionResolver.getAchievementsGPGS();
			}
		});

		highScoresButton = new ImageButton(highScoresButtonUp,
				highScoresButtonDown);
		highScoresButton.setBounds(320 / 2 - (buttonWidth / 2), 220
				- buttonHeight - buttonGap, 50, buttonHeight);
		/*highScoresButton.setBounds(320 / 2 - (buttonWidth / 2), 240
				- buttonHeight - buttonGap, 50, buttonHeight);*/
		highScoresButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//game.actionResolver.getLeaderboardGPGS();
				game.setScreenWithTransition(new HighScoresScreen(game), true);

			}
		});

		helpButton = new ImageButton(new TextureRegionDrawable(
				Assets.instance.assetGUI.helpButtonUp),
				new TextureRegionDrawable(
						Assets.instance.assetGUI.helpButtonDown));
		helpButton.setBounds((320f / 2.0f) - 10, 220 - buttonHeight
				- buttonGap, 72, buttonHeight);
		helpButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				helpMenu.show();
			}
		});

		buttons = new Stage(new StretchViewport(Constants.VIEWPORT_WIDTH,
				Constants.VIEWPORT_HEIGHT));
		//buttons.addActor(achievementsButton);
		buttons.addActor(highScoresButton);
		buttons.addActor(helpButton);

		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(buttons);
		inputMultiplexer.addProcessor(stage);

	}

	public void update() {
		if (helpMenu.isVisible() == false) {
			buttons.act();
		} else {
			stage.act();
		}

	}

	@Override
	public void render(float delta) {
		update();

		guiCam.update();

		game.batch.setProjectionMatrix(guiCam.combined);

		game.batch.begin();

		game.batch.draw(Assets.instance.assetLevelDecoration.plankWall, 0, 0,
				320, 480);
		Assets.instance.assetGUI.yellowPanelNine.draw(game.batch, 50, 315, 220,
				100);
		title.draw(game.batch, 1f);

		/*if (helpMenu.isVisible() == false) {
			game.batch.draw(Assets.instance.assetGUI.roundButton, 320 - 75, 0,
					64, 64);
			if (game.isGameMuted()) {
				game.batch.draw(Assets.instance.assetGUI.soundOffButton,
						mute.x, mute.y, mute.width, mute.height);
			} else {
				game.batch.draw(Assets.instance.assetGUI.soundOnButton, mute.x,
						mute.y, mute.width, mute.height);
			}
		}*/

		/*if (rateButtonPressed) {
			Assets.instance.assetGUI.beigeButtonDown.draw(game.batch, rate.x,
					rate.y, rate.width, rate.height);
			game.batch.draw(Assets.instance.assetGUI.rateText, rate.getX(),
					rate.getY() - 4, buttonWidth, buttonHeight);
		} else {
			Assets.instance.assetGUI.beigeButtonUp.draw(game.batch, rate.x,
					rate.y, rate.width, rate.height);
			game.batch.draw(Assets.instance.assetGUI.rateText, rate.getX(),
					rate.getY(), buttonWidth, buttonHeight);
		}*/

		if (playButtonPressed) {
			Assets.instance.assetGUI.beigeButtonDown.draw(game.batch,
					play.getX(), play.getY(), play.width, play.height);
			game.batch.draw(playText, play.getX(), play.getY() - 4,
					buttonWidth, buttonHeight);
		} else {
			Assets.instance.assetGUI.beigeButtonUp.draw(game.batch,
					play.getX(), play.getY(), play.width, play.height);
			game.batch.draw(playText, play.getX(), play.getY(), buttonWidth,
					buttonHeight);
		}

		// Assets.instance.font.draw(game.batch, "BY COLIN HEINRICHS", 320/2 -
		// Assets.instance.font.getBounds("BY COLIN HEINRICHS").width/2, 50);

		game.batch.end();

		buttons.draw();
		stage.draw();
	}

	@Override
	public void dispose() {
		buttons.dispose();
		stage.dispose();
		super.dispose();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (helpMenu.isVisible() == false) {
			guiCam.unproject(touchPoint.set(screenX, screenY, 0));
			if (play.contains(touchPoint.x, touchPoint.y)) {
				playButtonPressed = true;
			}
			if (rate.contains(touchPoint.x, touchPoint.y)) {
				rateButtonPressed = true;
			}
			/*if (mute.contains(touchPoint.x, touchPoint.y)) {
				if (game.isGameMuted() == false) {
					game.setGameMuted(true);
				} else {
					game.setGameMuted(false);
				}
			}*/
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (helpMenu.isVisible() == false) {
			guiCam.unproject(touchPoint.set(screenX, screenY, 0));
			if (play.contains(touchPoint.x, touchPoint.y)
					&& playButtonPressed == true) {

				if (game.isGameMuted() == false) {
					Assets.instance.whooshSound.play();
				}
				playButtonPressed = false;

				game.setScreenWithTransition(new GameScreen(game), true);
				dispose();

			}
		/*	if (rate.contains(touchPoint.x, touchPoint.y) && rateButtonPressed == true) {
				Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.chein.crispcut.android");
				//helpMenu.show();
			}*/
		}
		playButtonPressed = false;
		rateButtonPressed = false;
		highScoresButtonPressed = false;

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
