package com.chein.crispcut.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.chein.crispcut.LogCutter;

/**
 * A screen that creates a sliding effect between two other screens.
 * @author Colin
 *
 */
public class TransitionScreen implements Screen {

	
	 
	    private LogCutter game;
	    private Screen current;
	    private Screen next;
	    
	    OrthographicCamera cam;
	 
	    private FrameBuffer currentBuffer;
	    private FrameBuffer nextBuffer;
	 
	    private SpriteBatch batch;
	    int index = 0;  //how much to move the screen Sprites by.
	 
	    private Sprite currentScreenSprite;
	    private Sprite nextScreenSprite;
	    
	    private boolean forward; // true if forward, false if backwards.
	    
	    public TransitionScreen(Screen currScreen, Screen nextScreen, LogCutter game, Boolean forward) {
	    	this.current = currScreen;
	    	this.next = nextScreen;
	    	this.game = game;
	    	this.forward = forward;
	    	batch = new SpriteBatch();
	    	
	    	cam = new OrthographicCamera(320, 480);
			cam.position.set(320/2, 480/2, 0);
			
	    }
	    
	@Override
	public void render(float delta) {
	
			if(forward == true) {
				if(cam.viewportWidth - index > 0) {
				index += 10;
				nextScreenSprite.setBounds(cam.viewportWidth - cam.viewportWidth/2 - index, -cam.viewportHeight/2, cam.viewportWidth, cam.viewportHeight);
				currentScreenSprite.setBounds(-cam.viewportWidth/2 - index, -cam.viewportHeight/2, cam.viewportWidth, cam.viewportHeight);
				} else {
					game.setScreen(next);
				}
			} else {
				if(cam.viewportWidth - index > 0) {
				index += 10;
				nextScreenSprite.setBounds(-cam.viewportWidth-cam.viewportWidth/2 + index, -cam.viewportHeight/2, cam.viewportWidth, cam.viewportHeight);
				currentScreenSprite.setBounds(-cam.viewportWidth/2+index, -cam.viewportHeight/2, cam.viewportWidth, cam.viewportHeight);
				} else {
					game.setScreen(next);
				}
			}
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		currentScreenSprite.draw(batch);
		nextScreenSprite.draw(batch);
		batch.end();
		
	}

	@Override
	public void show() {
		//batch.setProjectionMatrix(cam.combined);
		index = 0;
		nextBuffer = new FrameBuffer(Pixmap.Format.RGB888, (int) cam.viewportWidth, (int) cam.viewportHeight, false);
		
		
		nextBuffer.begin();
		next.render(Gdx.graphics.getDeltaTime());
		nextBuffer.end();
		
		nextScreenSprite = new Sprite(nextBuffer.getColorBufferTexture());
		if(forward == true) {
			nextScreenSprite.setBounds(cam.viewportWidth, 0, cam.viewportWidth, cam.viewportHeight);
		} else {
			nextScreenSprite.setBounds(-cam.viewportWidth, 0, cam.viewportWidth, cam.viewportHeight);
		}
		nextScreenSprite.flip(false, true);
		
		currentBuffer = new FrameBuffer(Pixmap.Format.RGB888, (int) cam.viewportWidth, (int) cam.viewportHeight, false);
		
		currentBuffer.begin();
		current.render(Gdx.graphics.getDeltaTime());
		currentBuffer.end();
		
		currentScreenSprite = new Sprite(currentBuffer.getColorBufferTexture());
		if(forward == true) {
			nextScreenSprite.setBounds(0, 0, cam.viewportWidth, cam.viewportHeight);
		} else {
			nextScreenSprite.setBounds(0, 0, cam.viewportWidth, cam.viewportHeight);
		}
		currentScreenSprite.flip(false, true);
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
