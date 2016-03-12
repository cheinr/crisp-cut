package com.chein.crispcut;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chein.crispcut.ui.TransitionScreen;

public class LogCutter extends Game{
	
	ActionResolver actionResolver;
	
	public SpriteBatch batch;
	private boolean paused;
	private boolean gameMuted;
	Preferences prefs;
	
	public LogCutter(ActionResolver actionResolver) {
		this.actionResolver = actionResolver;
	}
	
	public LogCutter() {
		this.actionResolver = new ActionResolverDesktop();
	}
	
	@Override
	public void create() {
		
		batch = new SpriteBatch();
		Assets.instance.init(new AssetManager());
		prefs = Gdx.app.getPreferences(Constants.SHARED_PREFERENCES);
		gameMuted = prefs.getBoolean("GAME_MUTED");
		gameMuted = true;//TODO delete this when not working with html.
		setScreen(new MainMenuScreen(this));
		//setScreenWithTransition(new GameScreen(this));
		paused = false;
		
		//----used in example------
		//settings.load();
		//assets.load();
		//-------------------------
	}
	
	@Override
	public void render() {
		
		// Sets the clear screen color to: Emerald
		//Gdx.gl.glClearColor(46/255.0f, 204/255.0f, 113/255.0f, 0/255.0f);
		
		// Sets the clear screen color to: Wet Asphalt
		Gdx.gl.glClearColor(52/255.0f, 73/255.0f, 94/255.0f, 0/255.0f);
		
		
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Render game world to screen
		if(paused != true) {
			super.render();
		}
	}
	
	@Override
	public void resume() {
		paused = false;
		Assets.instance.init(new AssetManager());

	}
	
	@Override
	public void pause() {
		paused = true;
	}
	

	@Override
	public void dispose() {
		Assets.instance.dispose();
		batch.dispose();
		super.dispose();
	}
	
	public void setScreenWithTransition(Screen screen, boolean forward) {
	    TransitionScreen transition = new TransitionScreen(getScreen(), screen, this, forward);
	    setScreen(transition);
	}
	
	public boolean isGameMuted() {
		return gameMuted;
	}
	
	public void setGameMuted(Boolean b) {
		gameMuted = b;
		prefs.putBoolean("GAME_MUTED", b);
		prefs.flush();
	}



}
