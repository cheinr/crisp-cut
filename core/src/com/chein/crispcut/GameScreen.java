package com.chein.crispcut;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.chein.crispcut.actors.CutLabels;
import com.chein.crispcut.ui.GameOverMenu;
import com.chein.crispcut.ui.Hud;

/**
 * The main screen of the game.
 * @author Colin
 *
 */
public class GameScreen extends ScreenAdapter implements InputProcessor {

	private LogCutter game;

	OrthographicCamera guiCam;
	Vector3 touchPoint;
	World world;
	Preferences prefs;
	State state;

	Label readyLabel;
	Label startLabel;
	Label gameOverLabel;
	CutLabels cutLabels;

	InputMultiplexer inputMultiplexer;

	Hud hud;

	Stage stage;
	Stage gui;

	private long beltSoundID;
	// private long soundID;

	private boolean startAnimationDrawn = false;
	private boolean startLabelAnimationDone = false;
	private boolean gameOverAnimationFinished = false;
	private boolean gameOverAnimationStarted = false;

	GameOverMenu gameOverMenu;

	// Rectangle pauseButton;

	public GameScreen(LogCutter game) {
		this.game = game; // you can access this to switch gamestates

		world = new World();

		guiCam = new OrthographicCamera(320, 480);
		state = State.STARTING;
		world.setState(state);

		prefs = Gdx.app.getPreferences(Constants.SHARED_PREFERENCES);

		touchPoint = new Vector3();

		// world.addListener(new InputListener());

		stage = new Stage(new StretchViewport(Constants.WORLD_WIDTH,
				Constants.WORLD_HEIGHT), game.batch);
		gui = new Stage(new StretchViewport(320, 480), game.batch);

		stage.addActor(world);

		Label.LabelStyle style = new LabelStyle(Assets.instance.blueFont,
				Color.WHITE);

		readyLabel = new Label("READY", style);
		readyLabel.setPosition(75, 240);
		readyLabel.addAction(Actions.alpha(0f));
		readyLabel.act(0);// applies above action
		readyLabel.addAction(Actions.sequence(Actions.fadeIn(1f),
				Actions.fadeOut(1f)));

		startLabel = new Label("START!", style);
		startLabel.setPosition(70, 240);
		startLabel.addAction(Actions.alpha(0f));
		startLabel.act(0);

		Label.LabelStyle brownStyle = new LabelStyle(
				Assets.instance.gameOverFont, Color.WHITE);

		gameOverLabel = new Label("GAME OVER", brownStyle);
		gameOverLabel.setPosition(70, 240);
		gameOverLabel.addAction(Actions.alpha(0));
		gameOverLabel.act(0);

		cutLabels = new CutLabels();

		hud = new Hud(world.log, this, game);

		gui.addActor(readyLabel);// startLabel gets added later
		gui.addActor(cutLabels);
		gui.addActor(gameOverLabel);
		gui.addActor(startLabel);
		gui.addActor(hud);

		if (game.isGameMuted() == false) {
			beltSoundID = Assets.instance.beltSound.loop();
		}

		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(gui);
		inputMultiplexer.addProcessor(this);

		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.input.setCatchBackKey(true);
	}

	public void update(float deltaTime) {

		switch (state) {
		case GAMEOVER:
			updateGameOver(deltaTime);
			break;
		case NONE:
			break;
		case PLAYING:
			updatePlaying(deltaTime);
			break;
		case STARTING:
			updateStarting(deltaTime);
			break;
		case PAUSED:
			updatePaused(deltaTime);
			break;
		default:
			break;
		}
	}

	private void updatePaused(float deltaTime) {
		// pausedMenu.update(deltaTime, this);

		gui.act(deltaTime);

	}

	private void updateGameOver(float deltaTime) {
		stage.act(deltaTime);
		gui.act();

		if (gameOverAnimationStarted == false) {
			gameOverLabel.addAction(Actions.sequence(Actions.fadeIn(1),
					Actions.moveTo(70, 275, 0.25f)));
			gameOverAnimationStarted = true;
		} else if (gameOverLabel.getActions().size == 0) {
			gameOverMenu.update(deltaTime);
			Gdx.input.setInputProcessor(gameOverMenu);
			gameOverAnimationFinished = true;

		}

	}

	private void updatePlaying(float deltaTime) {

		gui.act();
		stage.act();

		if (world.log.getLogIgnored() == true) {
			cutLabels.showLabel(world.log.getMessage());
			world.log.setLogIgnored(false);
			world.log.setEventHandled(true);
		}

		if (world.log.speedChanged()) {
			float beltPitch; // needs to be between 0.5 and 2.0;
			beltPitch = (world.log.getFallSpeed() * 0.0375f) + 0.5f;
			Assets.instance.beltSound.setPitch(beltSoundID, beltPitch);
			world.log.setSpeedChanged(false);
		}

		if (world.log.getNumPerfectCuts() >= 20) {
			game.actionResolver
					.unlockAchievementGPGS(Constants.TWENTYPLUSACHIEVEMENTID);
		} else if (world.log.getNumPerfectCuts() >= 15) {
			game.actionResolver
					.unlockAchievementGPGS(Constants.FIFTEENPLUSACHIEVEMENTID);
		} else if (world.log.getNumPerfectCuts() >= 10) {
			game.actionResolver
					.unlockAchievementGPGS(Constants.TENPLUSACHIEVEMENTID);
		} else if (world.log.getNumPerfectCuts() >= 5) {
			game.actionResolver
					.unlockAchievementGPGS(Constants.FIVEPLUSACHIEVEMENTID);
		}

		if (world.isGameOver() == true) {

			gameOverMenu = new GameOverMenu(guiCam, game, 4, 3,
					world.log.getScore(), prefs.getInteger("HIGHSCORE"));

			if (cutLabels.isLabelBeingDrawn() == false
					&& world.log.onScreen() == false) {

				if (game.actionResolver.getSignedInGPGS()) {
					game.actionResolver.submitScoreGPGS(world.log.getScore());
				} else {
					game.actionResolver.loginGPGS();
				}

				if (world.log.getScore() >= 100) {
					game.actionResolver
							.unlockAchievementGPGS(Constants.PLATINUMMEDALACHIEVEMENTID);
				}
				if (world.log.getScore() >= 60) {
					game.actionResolver
							.unlockAchievementGPGS(Constants.GOLDMEDALACHIEVEMENTID);
				}
				if (world.log.getScore() >= 40) {
					game.actionResolver
							.unlockAchievementGPGS(Constants.SILVERMEDALACHIEVEMENTID);
				}
				if (world.log.getScore() >= 20) {
					game.actionResolver
							.unlockAchievementGPGS(Constants.BRONZEMEDALACHIEVEMENTID);
				}

				if (world.log.getScore() > prefs.getInteger("HIGHSCORE", 0)) {
					prefs.putInteger("HIGHSCORE", world.log.getScore());
					prefs.flush();
				} else if (world.log.getScore() > prefs.getInteger(
						"SECONDSCORE", 0)) {
					prefs.putInteger("SECONDSCORE", world.log.getScore());
					prefs.flush();
				} else if (world.log.getScore() > prefs.getInteger(
						"THIRDSCORE", 0)) {
					prefs.putInteger("THIRDSCORE", world.log.getScore());
					prefs.flush();
				}

				state = State.GAMEOVER;
				world.setState(state);
			}
		}
	}

	private void updateStarting(float deltaTime) {

		stage.act();
		gui.act();

		if (startAnimationDrawn == true) {
			state = State.PLAYING;
			world.setState(state);
		} else {

			if (readyLabel.getActions().size == 0) {
				if (startLabelAnimationDone == false) {
					startLabel.addAction(Actions.sequence(Actions.fadeIn(1f),
							Actions.fadeOut(1f)));
					startLabelAnimationDone = true;
				} else {
					if (startLabel.getActions().size == 0) {
						startAnimationDrawn = true;
					}
				}
			}
		}
	}

	@Override
	public void render(float delta) {
		update(delta);

		stage.draw();

		if (state == State.GAMEOVER && gameOverAnimationFinished) {
			gameOverMenu.draw(game.batch, guiCam);
		}

		gui.draw();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	public void setState(State state) {
		if (state == State.PLAYING && this.state == State.PAUSED
				&& game.isGameMuted() == false) {
			beltSoundID = Assets.instance.beltSound.loop();
			float beltPitch; // needs to be between 0.5 and 2.0;
			beltPitch = (world.log.getFallSpeed() * 0.0375f) + 0.5f;
			Assets.instance.beltSound.setPitch(beltSoundID, beltPitch);
			world.log.setSpeedChanged(false);

		}

		this.state = state;
		world.setState(state);

	}

	@Override
	public void dispose() {
		stage.dispose();
		gui.dispose();
		super.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK) {
			if (state == State.PAUSED) {
				game.setScreenWithTransition(new MainMenuScreen(game), false);
				if (game.isGameMuted() == false) {
					Assets.instance.beltSound.stop();
					Assets.instance.whooshSound.play();
				}
			} else {
				hud.showGamePausedMenu();
				setState(State.PAUSED);
				if (game.isGameMuted() == false) {
					Assets.instance.beltSound.stop();// Stop() then re-play() as
														// pause() and resume()
														// don't play nicely in
														// android.
				}
			}
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (!world.log.isCut() && state != State.PAUSED) {
			if (world.log.getFalling() == true) {
				world.log.setFalling(false);
				world.log.split();
				world.sawBlade.slide();
				cutLabels.showLabel(world.log.getMessage());
				if (game.isGameMuted() == false) {
					Assets.instance.sawSound.play(1);
				}
			}
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public InputMultiplexer getInputMultiplexer() {
		return inputMultiplexer;
	}

	public State getState() {
		return state;
	}

	public void switchScreens(Screen screen) {
		game.setScreen(screen);

	}

	public long getSoundID() {
		return beltSoundID;
	}

}
