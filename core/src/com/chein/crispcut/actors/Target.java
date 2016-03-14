package com.chein.crispcut.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.chein.crispcut.Assets;

/**
 * A curved line on the log that is meant to be cut.
 * @author Colin
 *
 */
public class Target extends Actor {
	float x;
	float y;
	float width;
	float height;
	float targetAdj;
	float gapSize = 0.5f;

	boolean targetSplit = false;

	TextureRegion targetLineRegion = new TextureRegion();
	Sprite targetLineSprite1;

	/**
	 * Creates a new target at the given x position and the x + targetAdj position
	 * with the given width and height.
	 * @param x
	 * @param logY
	 * @param targetAdj
	 * @param width
	 * @param height
	 */
	public Target(float x, float logY, float targetAdj, float width,
			float height) {
		//TODO get rid of targetAdj.
		this.x = x;
		this.y = logY; // this will be relative to the logs y value
		this.width = width;
		this.height = height;
		this.targetAdj = targetAdj;
		init();
	}

	private void init() {
		targetLineRegion.setRegion(Assets.instance.assetTarget.targetLine);
		targetLineSprite1 = new Sprite(targetLineRegion);
		targetLineSprite1.setBounds(x, y, width, height);

	}

	/**
	 * Updates the target y position.
	 * @param logY
	 * @param parentAlpha
	 */
	public void act(float logY, float parentAlpha) {
		y = logY + targetAdj;
		targetLineSprite1.setBounds(x, logY + targetAdj, width, height);
	}

	public void draw(Batch batch, float parentAlpha) {
		targetLineSprite1.draw(batch);
	}

	/**
	 * Sets the x position of the target.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Sets the y position of the target.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Gets the y position of the target.
	 */
	public float getY() {
		return y;
	}

}
