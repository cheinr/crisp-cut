package com.chein.crispcut.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.chein.crispcut.Assets;

/**
 * The bottom part of a log after it has been cut.
 * Hoping to get rid of this class.
 * @author Colin
 *
 */
public class LogSplit extends Actor{

	float x;
	float y;
	float width;
	float height;
	
	float splitTexHeight;
	float texWidth;
	float splitTexy;
	
	float logBottomSpriteScreenHeight;
	float logTopPatchScreenHeight;
	float logBotPatchScreenHeight;
	
	float fallSpeed;
	
	TextureRegion logSplitRegion = new TextureRegion();
	TextureRegion logTopPatchRegion = new TextureRegion();
	TextureRegion logBotPatchRegion = new TextureRegion();
	
	Sprite logSplitSprite;
	Sprite logTopPatchSprite;
	Sprite logBotPatchSprite;
	
	boolean falling = true;
	boolean drawBotPatch = false;
	boolean perfectCut;
	
	public LogSplit(float logSplitx, float splity,
					float width, float splitHeight,
					float splitTexHeight, float splitTexy,				   //	all calculated in Log class 
					float logTopPatchScreenHeight,						   //	- probs not the best way to do it. 
					float logBotPatchScreenHeight, float fallSpeed,			//	TODO - try and get rid of some of these and do more
					boolean drawBotPatch, boolean perfectCut) { 								//	do more calculations inside this class
		
		this.x = logSplitx;
		this.y = splity;
		this.width = width;
		this.height = splitHeight;
		this.splitTexHeight = splitTexHeight;
		this.logTopPatchScreenHeight = logTopPatchScreenHeight;
		this.logBotPatchScreenHeight = 1.7f;
		this.splitTexy = splitTexy;
		this.drawBotPatch = drawBotPatch;
		this.fallSpeed = fallSpeed;
		this.perfectCut = perfectCut;
		this.setVisible(true);
		init();
		
	}

	/**
	 * Default Constructor - does nothing.
	 */
	public LogSplit() {
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		this.splitTexHeight = 0;
		this.logTopPatchScreenHeight = 0;
		this.logBotPatchScreenHeight = 0;
		this.splitTexy = 0;
		this.drawBotPatch = false;
		this.fallSpeed = 0;
		this.perfectCut = false;
		
		this.setVisible(false);
	}


	private void init() {
		
		texWidth = Assets.instance.assetLog.log.getRegionWidth();
		logSplitRegion.setRegion(Assets.instance.assetLog.log);
		logSplitRegion.setRegion(logSplitRegion, (int) 0, (int) splitTexy, (int) texWidth, (int) splitTexHeight);
		
		if(perfectCut == true) {
			logTopPatchRegion.setRegion(Assets.instance.assetLog.logTopGreen);
		} else {
			logTopPatchRegion.setRegion(Assets.instance.assetLog.logTopPatch);
		}

		
		logSplitSprite = new Sprite(logSplitRegion);
		logTopPatchSprite = new Sprite(logTopPatchRegion);
		
		
		logSplitSprite.setBounds(x, y, width, height);
		logTopPatchSprite.setBounds( x, y+height-(logTopPatchScreenHeight/5), width, logTopPatchScreenHeight);
		
		logBotPatchRegion.setRegion(Assets.instance.assetLog.logBottomSimple);
		logBotPatchSprite = new Sprite(logBotPatchRegion);
		logBotPatchSprite.setBounds( x, y-(logBotPatchScreenHeight/2.6f), width, logBotPatchScreenHeight);
		
		act(0); //act once before drawing(fixes log glitch/artifact)
	}

	public void act(float delta) {
		 if(falling) { 
			y = (y-fallSpeed*delta);
		}

		logSplitSprite.setBounds((float)x, (float)y, (float)width, (float)height);
		logTopPatchSprite.setBounds((float) x, (float) (y+height-(logTopPatchScreenHeight/5)), (float) width, (float) logTopPatchScreenHeight);
		logBotPatchSprite.setBounds((float) x, (float) y, (float) width, (float) logBotPatchScreenHeight);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		logSplitSprite.draw(batch);
		logTopPatchSprite.draw(batch);
		logBotPatchSprite.draw(batch);
	}


	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	public float getTextureHeight() {
		return (float) splitTexHeight;
	}

	public float getY() {
		return y;
	}

}
