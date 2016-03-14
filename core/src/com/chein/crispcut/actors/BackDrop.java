package com.chein.crispcut.actors;

import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.chein.crispcut.Assets;
import com.chein.crispcut.Constants;

/**
 * The Background of the game; includes the conveyor belt.
 * @author Colin
 *
 */
public class BackDrop extends Actor{
	
	float x, y, width, height, fallSpeed, scrollSpeed;

	TextureRegion lightConcreteRegion;
	TextureRegion gradientRegion;
	
	TextureRegion conveyorBeltRegion;
	
	
	
	Sprite lightConcreteSprite;
	Sprite conveyorBeltSprite;
	Sprite conveyorBeltSprite2;
	Sprite sawRailSprite;
	Sprite gradient1, gradient2;
	
	
	
	float scrollTimer = 0;
	
	/**
	 * Creates a new Backdrop with the position and dimensions given.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public BackDrop(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		init();
	}
	
	private void init() {
		
		fallSpeed = 10;
		
		lightConcreteRegion = Assets.instance.assetLevelDecoration.plankWall;
		gradientRegion = Assets.instance.assetLevelDecoration.gradient;
		
		conveyorBeltRegion =  new TextureRegion(Assets.instance.assetLevelDecoration.conveyorBelt);
		Assets.instance.assetLevelDecoration.conveyorBelt.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
		lightConcreteSprite = new Sprite(lightConcreteRegion);
		lightConcreteSprite.setBounds(x, y, width, height);
		
		conveyorBeltSprite = new Sprite(conveyorBeltRegion);
		conveyorBeltSprite.setBounds(x+2.5f, 4.2f, 5, height);
		
		conveyorBeltSprite2 = new Sprite(conveyorBeltRegion);
		conveyorBeltSprite2.setBounds(x+2.5f, 3-height, 5, height);
		
		sawRailSprite = new Sprite(Assets.instance.assetSaw.sawRail);
		sawRailSprite.setBounds(Constants.WORLD_WIDTH / 2 - 4.25f, 3.2f, 8.5f, 0.8f);
		
		gradient1 = new Sprite(gradientRegion);
		gradient2 = new Sprite(gradientRegion);
		
		gradient1.setBounds(x+2.5f, 3-2, 5, 2);
		gradient2.setBounds(x+2.5f, 4.2f, 5, 2);
		gradient2.flip(false, true);
		
		
	}

	
	public void act(float deltaTime) {
		scrollSpeed = fallSpeed * 0.65f/10.0f;
		
		scrollTimer -= deltaTime*scrollSpeed;
		
	
		conveyorBeltSprite.setV(scrollTimer);
		conveyorBeltSprite.setV2((scrollTimer+1));
		
		conveyorBeltSprite2.setV(scrollTimer+.5f);
		conveyorBeltSprite2.setV2(scrollTimer+1.5f);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		lightConcreteSprite.draw(batch);
		conveyorBeltSprite.draw(batch);
		conveyorBeltSprite2.draw(batch);
		sawRailSprite.draw(batch);
		gradient1.draw(batch);
		gradient2.draw(batch);
	}
	
	/**
	 * Changes the speed of the conveyor belt.
	 * @param speed
	 */
	public void setFallSpeed(float speed) {
		this.fallSpeed = speed;
	}
	
}
