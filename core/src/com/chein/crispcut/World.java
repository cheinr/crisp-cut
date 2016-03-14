package com.chein.crispcut;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.chein.crispcut.actors.BackDrop;
import com.chein.crispcut.actors.Log;
import com.chein.crispcut.actors.SawBlade;
import com.chein.crispcut.ui.Hud;

/**
 * 
 * @author Colin
 *
 */
public class World extends Group {

	public Log log;
	public SawBlade sawBlade;
	public BackDrop backDrop;
	public Hud hud;

	private boolean gameOver;
	
	State state;

	public World() {
		state = State.NONE;
		initGameObjects();
	}

	private void initGameObjects() {

		log = new Log(Constants.WORLD_WIDTH / 2 - 1.25f, 15, 2.5f, 12.5f);
		sawBlade = new SawBlade(Constants.WORLD_WIDTH / 2 + 1.25f, 3.5f, 2.5f, 0.75f);
		backDrop = new BackDrop(Constants.WORLD_WIDTH / 2 - 5, 0, 10f, 15); // TODO
																				// rename
																				// this


		addActor(backDrop);
		// addActor(sawRail);//TODO - put this in backDrop
		addActor(log);
		addActor(sawBlade);

	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public void act(float deltaTime) {
		backDrop.setFallSpeed(log.getFallSpeed());

		backDrop.act(deltaTime);
		if(state == State.PLAYING || log.onScreen() && state != State.STARTING) {
			if (log.logSplit.isVisible()) {
				log.logSplit.act(deltaTime);
			}
			log.act(deltaTime);
		}
			
		sawBlade.act(deltaTime);
		
		if(log.getMessage() == Message.MISS) {
			gameOver = true;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		backDrop.draw(batch, parentAlpha);
		log.draw(batch, parentAlpha);
		sawBlade.draw(batch, parentAlpha);
		if (log.logSplit.isVisible())
			log.logSplit.draw(batch, parentAlpha);
		if (log.target.isVisible())
			log.target.draw(batch, parentAlpha);
	}
	
	public boolean isGameOver() {
		return gameOver;
	}

}
