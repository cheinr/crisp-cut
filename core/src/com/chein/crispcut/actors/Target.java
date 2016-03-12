package com.chein.crispcut.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.chein.crispcut.Assets;

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

	public Target(float x, float logY, float targetAdj, float width,
			float height) {
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

	public void act(float logY, float parentAlpha) {
		y = logY + targetAdj;
		targetLineSprite1.setBounds(x, logY + targetAdj, width, height);
	}

	public void draw(Batch batch, float parentAlpha) {
		targetLineSprite1.draw(batch);
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getY() {
		return y;
	}

}
