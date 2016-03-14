package com.chein.crispcut;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * A screen that displays the Users's high scores.
 * @author Colin
 *
 */
public class HighScoresScreen implements Screen{

	LogCutter game;
	
	private Stage stage;
	
	private ImageButton leaderBoardsButton;
	private ImageButton achievementsButton;
	private ImageButton backButton;
	
	private Image plankWall;
	
	
	private BitmapFont font;
	private BitmapFont numbers;
	
	private String firstScore;
	private String secondScore;
	private String thirdScore;
	
	
	public HighScoresScreen(LogCutter game) {
		
		this.game = game;
		
		stage = new Stage(new StretchViewport(Constants.VIEWPORT_WIDTH,
				Constants.VIEWPORT_HEIGHT), game.batch);
		font = Assets.instance.plainFont;
		numbers = Assets.instance.numbers;
		
		firstScore = Integer.toString(game.prefs.getInteger("HIGHSCORE"));
		secondScore = Integer.toString(game.prefs.getInteger("SECONDSCORE"));
		thirdScore = Integer.toString(game.prefs.getInteger("THIRDSCORE"));

		init();
	}
	
	public void init() {
		
		
		TextureRegionDrawable leaderBoardsUp = new TextureRegionDrawable(Assets.instance.assetGUI.leaderBoardsButtonUp);
		TextureRegionDrawable leaderBoardsDown = new TextureRegionDrawable(Assets.instance.assetGUI.leaderBoardsButtonDown);
		
		leaderBoardsButton = new ImageButton(leaderBoardsUp, leaderBoardsDown);
		leaderBoardsButton.setBounds(Constants.VIEWPORT_WIDTH/2-87.5f, 150, 175, 50);
		leaderBoardsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.actionResolver.getLeaderboardGPGS();
			}
		});
		
		
		TextureRegionDrawable achievementsUp = new TextureRegionDrawable(Assets.instance.assetGUI.achievementsButtonUp);
		TextureRegionDrawable achievementsDown = new TextureRegionDrawable(Assets.instance.assetGUI.achievementsButtonDown);
		
		achievementsButton = new ImageButton(achievementsUp, achievementsDown);
		achievementsButton.setBounds(Constants.VIEWPORT_WIDTH-50, 0, 50, 50);

		achievementsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.actionResolver.getAchievementsGPGS();
			}
		});
		
		TextureRegionDrawable backUp = new TextureRegionDrawable(Assets.instance.assetGUI.backButtonUp);
		TextureRegionDrawable backDown = new TextureRegionDrawable(Assets.instance.assetGUI.backButtonDown);
		backButton = new ImageButton(backUp, backDown);
		backButton.setBounds(Constants.VIEWPORT_WIDTH/2-50, 120, 100, 50);
		//backButton.setBounds(Constants.VIEWPORT_WIDTH/2-50, 80, 100, 50);

		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreenWithTransition(new MainMenuScreen(game), false);
			}
		});
		
		plankWall = new Image(Assets.instance.assetLevelDecoration.plankWall);
		plankWall.setBounds(0, 0, 320, 480);
		
		stage.addActor(plankWall);
		//stage.addActor(achievementsButton);
		//stage.addActor(leaderBoardsButton);
		stage.addActor(backButton);
		
		Gdx.input.setInputProcessor(stage);
		

	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		
		stage.draw();
		
		game.batch.begin();
		Assets.instance.assetGUI.yellowPanelNine.draw(game.batch, Constants.VIEWPORT_WIDTH/2 - 125, Constants.VIEWPORT_HEIGHT-260, 250, 250);
		font.draw(game.batch, "TOP SCORES", Constants.VIEWPORT_WIDTH/2 - 95, Constants.VIEWPORT_HEIGHT - 35);
		font.draw(game.batch, "1:", Constants.VIEWPORT_WIDTH/2 - 50, Constants.VIEWPORT_HEIGHT - 100);
		numbers.draw(game.batch, firstScore, Constants.VIEWPORT_WIDTH/2, Constants.VIEWPORT_HEIGHT - 95);
		font.draw(game.batch, "2:", Constants.VIEWPORT_WIDTH/2 - 50, Constants.VIEWPORT_HEIGHT - 150);
		numbers.draw(game.batch, secondScore, Constants.VIEWPORT_WIDTH/2, Constants.VIEWPORT_HEIGHT - 145);
		font.draw(game.batch, "3:", Constants.VIEWPORT_WIDTH/2 - 50, Constants.VIEWPORT_HEIGHT - 200);
		numbers.draw(game.batch, thirdScore, Constants.VIEWPORT_WIDTH/2, Constants.VIEWPORT_HEIGHT - 195);
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
