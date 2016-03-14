package com.chein.crispcut.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.chein.crispcut.Assets;
import com.chein.crispcut.Constants;
import com.chein.crispcut.GameScreen;
import com.chein.crispcut.LogCutter;
import com.chein.crispcut.MainMenuScreen;
import com.chein.crispcut.State;


/**
 * A menu for when the user pauses the game.
 * 
 * @author Colin
 *
 */
public class GamePausedMenu extends Group {

	float x, y, width, height;

	BitmapFont font;
	OrthographicCamera guiCam;
	LogCutter game;
	GameScreen gameScreen;

	Vector3 touchPoint;


	String scoreString;
	String highScoreString;

	ImageButton resumeButton;
	ImageButton backButton;
	
	//GlyphLayout layout;
	
	boolean visible;


	/**
	 * Creates a menu using the given GameScreen for the given LogCutter game.
	 * @param screen
	 * @param logCutter
	 */
	public GamePausedMenu(GameScreen screen, LogCutter logCutter) {

		this.gameScreen = screen;
		this.game = logCutter;
		
		this.width = 300;
		this.height = 50;
		
		visible = true;
		
		TextureRegionDrawable resumeButtonUp = new TextureRegionDrawable(
				Assets.instance.assetGUI.resumeButtonUp);
		TextureRegionDrawable resumeButtonDown = new TextureRegionDrawable(
				Assets.instance.assetGUI.resumeButtonDown);

		TextureRegionDrawable backButtonUp = new TextureRegionDrawable(
				Assets.instance.assetGUI.backButtonUp);
		TextureRegionDrawable backButtonDown = new TextureRegionDrawable(
				Assets.instance.assetGUI.backButtonDown);

		resumeButton = new ImageButton(resumeButtonUp, resumeButtonDown);
		resumeButton.setBounds(Constants.VIEWPORT_WIDTH / 2 - 25,
				Constants.VIEWPORT_HEIGHT / 2 - 140, 50, 50);
		
		resumeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				gameScreen.setState(State.PLAYING);
				
				visible = false;
			}
		});
		

		backButton = new ImageButton(backButtonUp, backButtonDown);
		backButton.setBounds(Constants.VIEWPORT_WIDTH / 2 - 50,
				Constants.VIEWPORT_HEIGHT / 2 - 80, 100, 50);
		
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (game.isGameMuted() == false) {
					Assets.instance.whooshSound.play();
				}
				game.setScreenWithTransition(new MainMenuScreen(game), false);
			}
		});


		addActor(resumeButton);
		addActor(backButton);


		
		
		init();
	}

	private void init() {

		this.x = Constants.VIEWPORT_WIDTH / 2 - width / 2;
		this.y = Constants.VIEWPORT_HEIGHT / 2 - height / 2;

		font = Assets.instance.font;
		//layout = new GlyphLayout(font, "PAUSED");

		touchPoint = new Vector3();

	}

	@Override
	public void act(float deltaTime) {
		this.x = Constants.VIEWPORT_WIDTH / 2 - width / 2;
		this.y = Constants.VIEWPORT_HEIGHT / 2 - height / 2;

		if(visible == false) {
			this.setVisible(false);
		}
		
		super.act(deltaTime);
	}

	
	@Override
	public void setVisible(boolean b) {
		visible = b;
		super.setVisible(b);
	}
}
