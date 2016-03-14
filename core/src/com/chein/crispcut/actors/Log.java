package com.chein.crispcut.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.chein.crispcut.Assets;
import com.chein.crispcut.Message;

/**
 * A falling log with a target to be cut at.
 * @author Colin
 *
 */
public class Log extends Group{
	
	float cutAccuracy;// the distance a cut is made from the target line.
	
	
	//Object bounds variables
	private float x;
	private float y;
	private float baseY; //this one doesn't move around so much and is used to calculate target position
	private float width;
	private float height;
	
	private float fallSpeed = 10;
		
	//Texture Variables
	float logBotTexHeight = 24; //The height of the logBottom section of the total log texture
	float logTopPatchTexHeight = 12;//The height of the logTopPatch section of the total log texture

	float bodyTexHeight;  //variable Texture height for the log body
	float totalTexHeight; //entire log Texture Height - constant
		
	//Sprite Bounds variables
	float totalHeight;    //original log height used for calculations.
		
	float splity;
	float logBoty;
		
	float logSplitx = 0;
		
	float splitHeight;
	float logBottomSpriteScreenHeight;
	float logTopPatchScreenHeight;
	float mainLogHeight;
	
	float targetYAdj;
		
	public static float gapSize = 0.5f; //the space between the two logs when cut
	//private static int ONE_LIFE = 10;
		
	//private int lifePoints;
	private int score = 0;
	private int numPerfectCuts;
	
	TextureRegion logBodyRegion = new TextureRegion();
	TextureRegion logBottomRegion = new TextureRegion();
	
	private Sprite logBodySprite;
	private Sprite logBottomSprite;

	public LogSplit logSplit;
	public Target target;
	private Message message;
	
	private boolean falling = true;
	private boolean logCut = false;
	private boolean logIgnored = false;
	private boolean perfectCut;
	private boolean speedChanged = false;
	
	private boolean eventHandled = false;
	
	java.util.Random r = new java.util.Random();
	

	/**
	 * Creates a new log with the position and dimensions given.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Log(float x, float y, float width, float height){
		this.x = x;
		this.y = y;
		this.baseY = y;
		this.width = width;
		this.height = height;
		this.totalHeight = height;
		this.mainLogHeight = height;
		
		init();
	}
	
	private void init() {
		assembleLog();
		message = Message.NONE;
		targetYAdj = MathUtils.random(2.5f, 11.25f);
		logSplit = new LogSplit();
		target = new Target(x, y, targetYAdj, 2.5f, 0.5f);
		logIgnored = false;
		eventHandled = false;
		
		addActor(target);
		addActor(logSplit);
		
	}
	
	
	private void assembleLog() {
		
		logBodyRegion.setRegion(Assets.instance.assetLog.log); //	Sets logMiddleRegion to get initial measurements
		logBottomRegion.setRegion(Assets.instance.assetLog.logBottom); //Shouldn't need measurements... Hopefully
		
		//Measurements
		totalTexHeight = logBodyRegion.getRegionHeight(); //Constant
		bodyTexHeight = logBodyRegion.getRegionHeight();	//This one changes 
		
		logBottomSpriteScreenHeight = (logBotTexHeight*height)/totalTexHeight;
		logTopPatchScreenHeight = (logTopPatchTexHeight*height)/totalTexHeight;
		logBoty = y - logBottomSpriteScreenHeight/2.6f; // changes if the log is cut to look normal
		
		//Sprites
		logBodySprite = new Sprite(logBodyRegion);
		logBottomSprite = new Sprite(logBottomRegion);
		
		logBodySprite.setBounds((float) x, (float) y, (float) width, (float) height);
		logBottomSprite.setBounds((float) x, (float) logBoty, (float) width, (float) logBottomSpriteScreenHeight);
		
	}
	
	

	private void respawn() { //moves the log back to the top of the screen and resets it's size.
		y = 15;
		baseY = 15; 
		mainLogHeight = 12.5f;
		height = 12.5f;
		logCut = false;
		perfectCut = false;
		
		fallSpeed = r.nextInt(30) + 10;
		target.setVisible(true);
		
		speedChanged = true;
		init();
	}

	/**
	 * Checks if the log is on the screen.
	 * @return
	 */
	public boolean onScreen() {
		if(y < 0 - height){
			return false;
		} else {
			return true;
		}
	}
	

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		logBodySprite.draw(batch);
		if(logCut) {
			logBottomSprite.draw(batch); 
		}
	}
	
	@Override
	public void act(float deltaTime) {
	
		target.act(baseY, deltaTime);
		
		if(message == Message.PERFECT) {
			target.setVisible(false);
		} else {
			target.setVisible(true);
		}
		
		//makes the log fall
		if(falling) {
			if(onScreen()) {
				y = (y - fallSpeed*deltaTime);
				baseY = (baseY - fallSpeed*deltaTime);
				logBoty = y-logBottomSpriteScreenHeight/2.6f;
			} else { 
				if(message != Message.MISS) {
					respawn();
				}
			}
			if(logCut != true && y+height < 4 && eventHandled == false) {
				scoreCut();
				logIgnored = true;
			}
		}
		//updates the log's body position and size to be drawn to the screen
		logBodySprite.setBounds((float) x, (float) y, (float) width, (float) mainLogHeight);
		logBottomSprite.setBounds((float) x, (float) logBoty, (float) width, (float) logBottomSpriteScreenHeight);
	}

	/**
	 * Cut the log at the position that is at the y position '4'.
	 */
	public void split() {
		int splitTexHeight;
		
		//Check to see if log is over the cutting line at y=4
		if(y<3.1f && (y+mainLogHeight)>4.5f) {
			
			scoreCut();
				
			//Gdx.app.log("Scorer", "CutAccuracy == " + cutAccuracy);
			
			//Sprite heights TODO - Make another variable to take over here instead of changing y directly
			mainLogHeight = (y+mainLogHeight) - 4;
			splitHeight = 4 - y - gapSize; //should just be needed for measurements
			height = height - splitHeight;
			
			//Texture heights
			bodyTexHeight = (mainLogHeight*totalTexHeight)/totalHeight;
			splitTexHeight = (int) ((splitHeight*totalTexHeight)/totalHeight);

			
			//TODO - Make another variable to take over here instead of changing y directly
			
			splity = y;
			y = 4;
			logBoty = y - logBottomSpriteScreenHeight/2.6f; // changes if the log is cut to look normal

			
			//logSplitx = MathUtils.random(-0.5f, 0.5f); // random
			logSplitx = x;
		
			//Textures: chop regions
			logBodyRegion.setRegion(Assets.instance.assetLog.log, 0, 0, logBodyRegion.getRegionWidth(), (int) bodyTexHeight);
		
			//Sprites: add chopped regions
			logBodySprite.setRegion(logBodyRegion);
		
			//Sprites: change positions
			logBodySprite.setBounds( x, y, width, mainLogHeight);
			logBottomSprite.setBounds( x, logBoty, width, logBottomSpriteScreenHeight);

			
			if(!logCut) {
				
				logSplit = new LogSplit( logSplitx, splity, width, splitHeight, splitTexHeight, totalTexHeight - splitTexHeight, logTopPatchScreenHeight, logBottomSpriteScreenHeight, fallSpeed, false, perfectCut); //hopefully this last variable is right
				logCut = true;
				
			}
			logCut = true;
		}
		falling = true;
	}

	
	
	private void scoreCut() {
		cutAccuracy = (target.getY()+target.getHeight()/2) - 4;
		
		message = Message.MISS;
		
		if(Math.abs(cutAccuracy) < 0.75f) {
			message = Message.PERFECT;
			logBottomRegion.setRegion(Assets.instance.assetLog.logBottomGreen);
			logBottomSprite.setRegion(logBottomRegion);
			perfectCut = true;
			score += 3;
			numPerfectCuts += 1;
			//lifePoints += 5;
		}
		if((cutAccuracy) >= 0.75f && cutAccuracy < 3) {
			message = Message.GOOD;
			score += 1;
			numPerfectCuts = 0;
			//lifePoints += 2;
		} else if((cutAccuracy) >= 3) {
			message = Message.BAD;
			numPerfectCuts = 0;
		}
		
		if(message == Message.MISS) {
			//lifePoints -= ONE_LIFE;
			numPerfectCuts = 0;
		}
		 
	}
	
	
	//Getters and setters
	/**
	 * Gets the number of times the log has been cut perfectly on the line.
	 * @return
	 */
	public int getNumPerfectCuts() {
		return numPerfectCuts;
	}

	/**
	 * Checks is the log has been cut.
	 * @return
	 */
	public boolean isCut() {
		return logCut;
	}
	
	/**
	 * Gets the current score for the user.
	 * @return
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Checks if the log is falling.
	 * @return
	 */
	public boolean getFalling() {
		return falling;
	}
	
	/**
	 * Sets whether the log is falling or not.
	 * @param b
	 */
	public void setFalling(boolean b) {
		falling = b;
	}

	/**
	 * Gets the y position of the log.
	 */
	public float getY() {
		return this.y;
	}
	
	/**
	 * Gets the x position of the log.
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * Gets the fall speed of the log.
	 * @return
	 */
	public float getFallSpeed() {
		return this.fallSpeed;
	}
	
	/**
	 * Checks whether the last cut was considered "perfect".
	 * @return
	 */
	public boolean isPerfectCut() {
		return perfectCut;
	}

	/**
	 * Gets the message that should be displayed about the last cut.
	 * @return
	 */
	public  Message getMessage() {
		return message;
	}

	/**
	 * Sets the message that should be displayed about the last cut.
	 * @param a
	 */
	public void setMessage(Message a) {
		message = a;
	}
	
	/**
	 * @return Whether or not the last log was cut at all.
	 */
	public boolean getLogIgnored() {
		return logIgnored;
	}
	
	/**
	 * @param b
	 *  - Whether of not the last log was cut at all.
	 */
	public void setLogIgnored(boolean b) {
		this.logIgnored = b;
	}

	public boolean eventHandled() {
		return eventHandled;
	}

	public void setEventHandled(boolean b) {
		eventHandled = b;
		
	}

	/**
	 * Checks whether the log's speed has changed.
	 * @return
	 */
	public boolean speedChanged() {
		return speedChanged;
	}
	
	/**
	 * Sets whether the log's speed has changed.
	 * @param b
	 */
	public void setSpeedChanged(boolean b) {
		speedChanged = b;
	}
}
