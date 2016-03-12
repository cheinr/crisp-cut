package com.chein.crispcut.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.chein.crispcut.Assets;
import com.chein.crispcut.Constants;

public class SawBlade extends Actor {

	float x;
	float y;
	float width;
	float height;

	float sawBladeTexHeight;

	float stateTime;

	boolean resetBlade = false;
	boolean bladeLowered = false;
	boolean bladeRaised = false;

	// Constants for math
	float totalTexHeight;
	float totalHeight;
	float sawBladeTexWidth = 221;

	TextureRegion sawBladeTexF1 = new TextureRegion();
	TextureRegion sawBladeTexF2 = new TextureRegion();
	Array<TextureRegion> keyFrames = new Array<TextureRegion>();
	Animation sawBladeAnimation;

	Sprite sawBladeSprite;

	public SawBlade(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		init();
	}

	private void init() {

		sawBladeTexF1.setRegion(Assets.instance.assetSaw.sawBladeF1);
		sawBladeTexF2.setRegion(Assets.instance.assetSaw.sawBladeF2);

		keyFrames.add(sawBladeTexF1);
		keyFrames.add(sawBladeTexF2);

		sawBladeAnimation = new Animation(0.1f, keyFrames);
		sawBladeAnimation.setPlayMode(PlayMode.LOOP);

		sawBladeSprite = new Sprite(sawBladeTexF1);
		sawBladeSprite.setBounds(x, y, width, height);

		totalTexHeight = sawBladeTexF1.getRegionHeight();
		totalHeight = height;

	}

	public void act(float delta) {
		stateTime += delta;
		sawBladeSprite.setRegion(sawBladeAnimation.getKeyFrame(stateTime));

		if (resetBlade) {
			resetBlade();
		}
	}

	public void draw(Batch batch, float parentAlpha) {
		sawBladeSprite.draw(batch);
	}

	public void slide() {
		x = Constants.WORLD_WIDTH / 2 - 3.75f;

		sawBladeSprite.setBounds(x, y, width, height);

		resetBlade = true;

	}

	public void resetBlade() {

		if (bladeLowered == false) { // lower blade
			if (height > 0) {

				height = height - 0.07f;
				sawBladeTexHeight = ((height * totalTexHeight) / totalHeight);

				sawBladeTexF1.setRegion(Assets.instance.assetSaw.sawBladeF1,
						(int) 0, (int) 0, (int) sawBladeTexWidth,
						(int) sawBladeTexHeight);
				sawBladeTexF2.setRegion(Assets.instance.assetSaw.sawBladeF2,
						(int) 0, (int) 0, (int) sawBladeTexWidth,
						(int) sawBladeTexHeight);

			} else {
				bladeLowered = true;
			}
		}
		if (bladeLowered && bladeRaised == false) { // raise blade

			x = Constants.WORLD_WIDTH / 2 + 1.25f; // puts blade back in
														// original position

			if (height < totalHeight) {
				height = height + 0.07f;
				sawBladeTexHeight = ((height * totalTexHeight) / totalHeight);

				sawBladeTexF1.setRegion(Assets.instance.assetSaw.sawBladeF1,
						(int) 0, (int) 0, (int) sawBladeTexWidth,
						(int) sawBladeTexHeight);
				sawBladeTexF2.setRegion(Assets.instance.assetSaw.sawBladeF2,
						(int) 0, (int) 0, (int) sawBladeTexWidth,
						(int) sawBladeTexHeight);

			} else {
				bladeRaised = true;
			}
		}

		if (bladeLowered && bladeRaised) {
			resetBlade = false;
			bladeLowered = false;
			bladeRaised = false;
		}
		sawBladeSprite.setBounds(x, y, width, height);
	}

}
