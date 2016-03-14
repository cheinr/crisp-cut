package com.chein.crispcut.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.chein.crispcut.Assets;
import com.chein.crispcut.Constants;

/**
 * A menu meant to show the user how to play.
 * @author Colin
 *
 */
public class HelpMenu extends Group {

	private ImageButton backButton;
	private BitmapFont font;


	private int x, y, width, height;

	private boolean visible;

	public HelpMenu() {

		width = 240;
		height = 310;
		
		x= (int) (Constants.VIEWPORT_WIDTH/2 - width/2);
		y = (int) (Constants.VIEWPORT_HEIGHT/2 - height/2) + 22;
		
		backButton = new ImageButton(new TextureRegionDrawable(
				Assets.instance.assetGUI.backButtonUp),
				new TextureRegionDrawable(
						Assets.instance.assetGUI.backButtonDown));
		
		backButton.setBounds(x+width/2 - 50, y-65, 100, 50);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
			}
		});
		
		font = Assets.instance.smallFont;
		
		
		addActor(backButton);
		
		
		
		
	}

	@Override
	public void act(float deltaTime) {
		if (visible == true) {
			backButton.act(deltaTime);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (visible == true) {
			Assets.instance.assetGUI.yellowPanelNine.draw(batch, x, y, width,
					height);
			
			
			backButton.draw(batch, parentAlpha);
			
			
			font.draw(batch, "HOW TO PLAY:", x+25, y+height-25);
			
			font.draw(batch, "TAP TO MOVE THE SAW.", x+60, y+height-65);
			batch.draw(Assets.instance.assetGUI.touchIcon, x+20, y+height-75, 25, 32);
			
			font.draw(batch, "GET AS CLOSE TO THE LINE", x+60, y+height-105);
			font.draw(batch, "AS YOU CAN WITHOUT", x+60, y+height-120);
			font.draw(batch, "GOING OVER.", x+60, y+height-135);

			font.draw(batch, "GETTING CLOSE TO THE", x+60, y+height-175);
			font.draw(batch, "LINE WITHOUT GOING OVER", x+60, y+height-190);
			font.draw(batch, "GIVES ONE POINT.", x+60, y+height-205);
			batch.draw(Assets.instance.assetGUI.plus1Icon, x+15, y+height-200, 35, 25);

			font.draw(batch, "CUTTING THE LINE", x+60, y+height-245);
			font.draw(batch, "PERFECTLY GIVES THREE", x+60, y+height-260);
			font.draw(batch, "POINTS.", x+60, y+height-275);
			batch.draw(Assets.instance.assetGUI.plus3Icon, x+15, y+height-270, 35, 25);


			
		}
	}
	
	@Override
	public boolean isVisible() {
		return visible;
	}

	public void show() {
		visible = true;
	}

	public void hide() {
		visible = false;
	}

}
