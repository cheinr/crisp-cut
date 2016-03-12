package com.chein.crispcut.actors;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.chein.crispcut.Assets;
import com.chein.crispcut.Message;

public class CutLabels extends Group{
	
	private Label perfectLabel;
	private Label goodLabel;
	private Label badLabel;
	private Label missLabel;
	
	ArrayList<Label> labels = new ArrayList<Label>();
	
	public CutLabels() {
		
		Label.LabelStyle redStyle = new LabelStyle(Assets.instance.redFont, Color.WHITE);
		Label.LabelStyle yellowStyle = new LabelStyle(Assets.instance.yellowFont, Color.WHITE);
		Label.LabelStyle greenStyle = new LabelStyle(Assets.instance.greenFont, Color.WHITE);
		
		perfectLabel = new Label("+3", greenStyle);
		goodLabel = new Label("+1", yellowStyle);
		badLabel = new Label("BAD", redStyle);
		missLabel = new Label("MISS", redStyle);
		
		labels.add(perfectLabel);
		labels.add(goodLabel);
		labels.add(badLabel);
		labels.add(missLabel);
		
		
		for(int i=0; i<labels.size(); i++) {
			addActor(labels.get(i));
			labels.get(i).setPosition(215, 175);
			labels.get(i).addAction(Actions.alpha(0f));
			labels.get(i).act(0);// applies above action
		}
		
	}
	
	public void showLabel(Message message) {
		switch(message) {
		case BAD:
			badLabel.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.fadeOut(.5f)));
			break;
		case GOOD:
			goodLabel.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.fadeOut(.5f)));
			break;
		case MISS:
			missLabel.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.fadeOut(.75f)));
			break;
		case NONE:
			break;
		case PERFECT:
			perfectLabel.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.fadeOut(.5f)));
			break;
		default:
			break;
			
		}
			
	}

	public boolean isLabelBeingDrawn() {
		for(int i=0; i<labels.size(); i++) {
			if(labels.get(i).getColor().a != 0) {
				return true;
			}
		}
		return false;
	}

}
