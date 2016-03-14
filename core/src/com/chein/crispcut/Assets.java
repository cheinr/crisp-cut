package com.chein.crispcut;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.chein.crispcut.Constants;


	/** 
	 * A singleton class that handles asset loading.
	 * @author Colin
	 *
	 */
	public class Assets implements Disposable, AssetErrorListener {
		
		public static final String TAG = Assets.class.getName();
		public static final Assets instance = new Assets();
		
		private AssetManager assetManager;
		
		public AssetSaw assetSaw;
		public AssetLog assetLog;
		
		public AssetGUI assetGUI;
		public AssetLevelDecoration assetLevelDecoration;
		public AssetTarget assetTarget;
		
		public BitmapFont font;
		public BitmapFont blueFont;
		public BitmapFont redFont;
		public BitmapFont yellowFont;
		public BitmapFont greenFont;
		//public BitmapFont brownFont;
		public BitmapFont smallFont;
		public BitmapFont numbers;
		public BitmapFont titleFont;
		public BitmapFont gameOverFont;
		public BitmapFont plainFont;
		
		public BitmapFontData titleFontData;
		public BitmapFontData gameOverFontData;
		public BitmapFontData plainBigFontData;
		public TextureRegion brownFontRegion;
		public TextureRegion fontRegion;
		
		public Sound whooshSound;
		public Sound beltSound;
		public Sound sawSound;
	
		// singleton: prevent instantiation from other classes
		private Assets () {}

		public void init (AssetManager assetManager) {
			this.assetManager = assetManager;
			// set asset manager error handler
			assetManager.setErrorListener(this);
			// load texture atlas
			assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
			assetManager.load("font.fnt", BitmapFont.class);
			assetManager.load("smallFont.fnt", BitmapFont.class);
			assetManager.load("numbers.fnt", BitmapFont.class);
			assetManager.load("blueFont.fnt", BitmapFont.class);
			assetManager.load("redFont.fnt", BitmapFont.class);
			assetManager.load("yellowFont.fnt", BitmapFont.class);
			assetManager.load("greenFont.fnt", BitmapFont.class);
			//assetManager.load("brownFont.fnt", BitmapFontData.class);
			assetManager.load("brownFont.png", Texture.class);
			assetManager.load("font.png", Texture.class);

			assetManager.load("conveyorBelt.png", Texture.class);
			assetManager.load("conveyorBelt.ogg", Sound.class);
			assetManager.load("whooshSound.wav", Sound.class);
			assetManager.load("sawSound.ogg", Sound.class);
			assetManager.finishLoading();
			
			

			Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);

			TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
			Texture conveyorBelt = assetManager.get("conveyorBelt.png", Texture.class);
			font = assetManager.get("font.fnt", BitmapFont.class);
			smallFont = assetManager.get("smallFont.fnt", BitmapFont.class);
			numbers = assetManager.get("numbers.fnt", BitmapFont.class);
			
			blueFont = assetManager.get("blueFont.fnt", BitmapFont.class);
			redFont = assetManager.get("redFont.fnt", BitmapFont.class);
			yellowFont = assetManager.get("yellowFont.fnt", BitmapFont.class);
			greenFont = assetManager.get("greenFont.fnt", BitmapFont.class);
			//titleFontData = assetManager.get("brownFont.fnt", BitmapFontData.class);
			brownFontRegion = new TextureRegion(assetManager.get("brownFont.png", Texture.class));
			fontRegion = new TextureRegion(assetManager.get("font.png", Texture.class));
			whooshSound = assetManager.get("whooshSound.wav", Sound.class);
			beltSound = assetManager.get("conveyorBelt.ogg", Sound.class);
			sawSound = assetManager.get("sawSound.ogg", Sound.class);
			
			

			
			for (String a : assetManager.getAssetNames()) Gdx.app.debug(TAG, "asset: " + a);
			
			for(Texture t : atlas.getTextures()) {
				t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			}
			
			titleFontData = new BitmapFontData(Gdx.files.getFileHandle("brownFont.fnt", FileType.Internal), false);
			gameOverFontData = new BitmapFontData(Gdx.files.getFileHandle("brownFont.fnt", FileType.Internal), false);
			plainBigFontData = new BitmapFontData(Gdx.files.getFileHandle("font.fnt", FileType.Internal), false);
			
			plainBigFontData.setScale(0.75f);
			gameOverFontData.setScale(0.5f, 0.75f);
			titleFontData.setScale(0.6f, 1.2f);
			
			titleFont = new BitmapFont(titleFontData, brownFontRegion, false);
			gameOverFont = new BitmapFont(gameOverFontData, brownFontRegion, false);
			plainFont = new BitmapFont(plainBigFontData, fontRegion, false);
			
			assetLog = new AssetLog(atlas);
			assetSaw = new AssetSaw(atlas);
			assetLevelDecoration = new AssetLevelDecoration(atlas, conveyorBelt);
			assetTarget = new AssetTarget(atlas);
			assetGUI = new AssetGUI(atlas);
			
			//assetManager.unload(Constants.TEXTURE_ATLAS_OBJECTS);
				
			}
		
		
			@Override
			public void dispose () {
				assetManager.dispose();
			}

			@Override
			public void error(@SuppressWarnings("rawtypes") AssetDescriptor asset, Throwable throwable) {
				Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
			}
			
			public class AssetSaw {
				public final AtlasRegion sawBladeF1;
				public final AtlasRegion sawBladeF2;
				public final AtlasRegion sawRail;
				public AssetSaw (TextureAtlas atlas) {
					sawBladeF1 = atlas.findRegion("sawBladeF1");
					sawBladeF2 = atlas.findRegion("sawBladeF2");
					sawRail = atlas.findRegion("sawRail");
				}
			}
			
			public class AssetGUI {
				public final AtlasRegion playButton;
				public final AtlasRegion backButton;
				public final AtlasRegion replayButton;
				public final AtlasRegion pauseButton;
				
				public final AtlasRegion rateText;
				public final AtlasRegion leaderBoardsButton;
				
				public final AtlasRegion roundButton;
				
				public final AtlasRegion bronzeMedal;
				public final AtlasRegion silverMedal;
				public final AtlasRegion goldMedal;
				public final AtlasRegion platinumMedal;
				public final AtlasRegion medalInlay;
				
				public final AtlasRegion backButtonUp;
				public final AtlasRegion backButtonDown;
				public final AtlasRegion replayButtonUp;
				public final AtlasRegion replayButtonDown;
				public final AtlasRegion resumeButtonUp;
				public final AtlasRegion resumeButtonDown;
				public final AtlasRegion achievementsButtonUp;
				public final AtlasRegion achievementsButtonDown;
				public final AtlasRegion highScoresButtonUp;
				public final AtlasRegion highScoresButtonDown;
				public final AtlasRegion helpButtonDown;
				public final AtlasRegion helpButtonUp;
				public final AtlasRegion leaderBoardsButtonDown;
				public final AtlasRegion leaderBoardsButtonUp;
				
				public final AtlasRegion touchIcon;
				public final AtlasRegion plus1Icon;
				public final AtlasRegion plus3Icon;
				
				public final AtlasRegion soundOnButton;
				public final AtlasRegion soundOffButton;
				
				public final NinePatch beigeButtonUp;
				public final NinePatch beigeButtonDown;
				
				public final NinePatch sqBeigeButtonUp;
				public final NinePatch sqBeigeButtonDown;
				
				public final NinePatch yellowPanelNine;
				 
				
				public AssetGUI (TextureAtlas atlas) {
					yellowPanelNine = new NinePatch(atlas.findRegion("yellowPanel"), 58, 10, 27, 34);
					
					beigeButtonUp = new NinePatch(atlas.findRegion("beigeButtonUp"), 44, 94, 16, 14);
					beigeButtonDown = new NinePatch(atlas.findRegion("beigeButtonDown"), 44, 94, 16, 14);
					
					sqBeigeButtonUp = new NinePatch(atlas.findRegion("sqBeigeButtonUp"), 8, 8, 36, 36);
					sqBeigeButtonDown = new NinePatch(atlas.findRegion("sqBeigeButtonDown"), 8, 8, 36, 36);
					
					soundOnButton = atlas.findRegion("soundOnButton");
					soundOffButton = atlas.findRegion("soundOffButton");
					
					playButton = atlas.findRegion("playButton");
					backButton = atlas.findRegion("backButton");
					replayButton = atlas.findRegion("replayButton");
					pauseButton = atlas.findRegion("pauseButton");
					rateText = atlas.findRegion("rateText");
					leaderBoardsButton = atlas.findRegion("leaderBoardsButton");
					
					replayButtonDown = atlas.findRegion("replayButtonDown");
					replayButtonUp = atlas.findRegion("replayButtonUp");
					backButtonDown = atlas.findRegion("backButtonDown");
					backButtonUp = atlas.findRegion("backButtonUp");
					resumeButtonUp = atlas.findRegion("resumeButtonUp");
					resumeButtonDown = atlas.findRegion("resumeButtonDown");
					achievementsButtonUp = atlas.findRegion("achievementsButtonUp");
					achievementsButtonDown = atlas.findRegion("achievementsButtonDown");
					highScoresButtonUp = atlas.findRegion("highScoresButtonUp");
					highScoresButtonDown = atlas.findRegion("highScoresButtonDown");
					helpButtonUp = atlas.findRegion("helpButtonUp");
					helpButtonDown = atlas.findRegion("helpButtonDown");
					leaderBoardsButtonUp = atlas.findRegion("leaderBoardsButtonUp");
					leaderBoardsButtonDown = atlas.findRegion("leaderBoardsButtonDown");
					
					roundButton = atlas.findRegion("roundButton");
					
					touchIcon = atlas.findRegion("touchIcon");
					plus1Icon = atlas.findRegion("plus1Icon");
					plus3Icon = atlas.findRegion("plus3Icon");
					
					bronzeMedal = atlas.findRegion("bronzeMedal");
					silverMedal = atlas.findRegion("silverMedal");
					goldMedal = atlas.findRegion("goldMedal");
					platinumMedal = atlas.findRegion("platinumMedal");
					medalInlay = atlas.findRegion("medalInlay");
				}
			}
			
			public class AssetTarget {
				public final AtlasRegion targetLine;
				public AssetTarget (TextureAtlas atlas) {
					targetLine = atlas.findRegion("targetLine");
				}
			}
			
			public class AssetLog {
				public final AtlasRegion logBottom;
				public final AtlasRegion logTopPatch;
				public final AtlasRegion log;
				
				public final AtlasRegion logTopGreen;
				public final AtlasRegion logBottomGreen;
				public final AtlasRegion logBottomSimple;
				
				public AssetLog(TextureAtlas atlas) {
					logBottom = atlas.findRegion("logBottom");
					logTopPatch = atlas.findRegion("logTopPatch");
					log = atlas.findRegion("log");
					logBottomSimple = atlas.findRegion("logBottomSimple");
					
					logTopGreen = atlas.findRegion("logTopGreen");
					logBottomGreen = atlas.findRegion("logBottomGreen");
				}
			}
			
			public class AssetLevelDecoration {
				public final AtlasRegion plankWall;
				
				public final Texture conveyorBelt;
				public final AtlasRegion gradient;
				
				public AssetLevelDecoration(TextureAtlas atlas, Texture conveyorBelt) {
					
					plankWall = atlas.findRegion("plankWall");
					
					this.conveyorBelt = conveyorBelt;
					gradient =  atlas.findRegion("gradient");
				}
			}
}
	

