/*
 * Copyright 2019 Adrian Reimer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mygdx.screen;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.Manager.SpriteManager;
import com.mygdx.Manager.WorldManager;
import com.mygdx.enums.Difficulty;
import com.mygdx.enums.GameSaves;
import com.mygdx.enums.GameStageImages;
import com.mygdx.enums.Models;
import com.mygdx.enums.Musics;
import com.mygdx.enums.Sounds;
import com.mygdx.model.Knight;
import com.mygdx.model.ModelInterface;
import com.mygdx.music.SoundInterface;
import com.mygdx.stage.DeathStage;
import com.mygdx.stage.GameSaveInterface;
import com.mygdx.stage.GameStage;
import com.mygdx.stage.GameStageInterface;
import com.mygdx.stage.HighscoreStage;
import com.mygdx.stage.LoadStage;
import com.mygdx.stage.LoadingStage;
import com.mygdx.stage.MainMenuStage;
import com.mygdx.stage.OptionStage;
import com.mygdx.stage.SaveStage;
import com.mygdx.stage.StageSwitchInterface;

/**
 * Screen the Dungeon Game runs in.
 * @author Adrian Reimer
 *
 */
public class GameScreen implements Screen,StageSwitchInterface,SoundInterface,GameStageInterface,GameSaveInterface,ModelInterface  {
	
	public static final Skin SKIN = new Skin(Gdx.files.internal("data/GUI/skin/uiskin.json"));
	public static final String USER_HOME = "user.home"; // Documents folder on (Win+Linux+OSX)
	
	private static final String PASTE_LOCATION = "/Documents/DungeonGame/saves/";	
	private static final String GAME_CURSOR_LOCATION = "my_game_cursor.png";

	private boolean isPaused;
    private MainMenuStage mainMenuStage; // Stages
    private GameStage gameStage; // -
    private SaveStage saveStage; // -
    private LoadStage loadStage; // -
    private OptionStage optionStage; // -
    private LoadingStage loadingStage; // -
    private DeathStage deathStage; // -
    private HighscoreStage highscoreStage;
    private ObjectMap<GameStageImages, Image> gameStageImageMap; // Assets (AssetManager if too much)
    private ObjectMap<Models, Texture> modelImageMap; // -
    private ObjectMap<Sounds, Sound> soundMap; // -
    private ObjectMap<Musics, Music> musicMap; // -
    private ObjectMap<GameSaves, String> saveMap; // - (Save Locations)
    private Queue<Difficulty> difficultyQueue; // Difficulty/Level

    /**
     * GameScreen constructor.
     */
    public GameScreen () {
    	// create save directory
    	File dir = new File(System.getProperty(USER_HOME)+PASTE_LOCATION);
    	dir.mkdirs();
    	// copy tiledMapLoader files
    	FileHandle from = Gdx.files.internal("data/DungeonGame/saves/tiles.tsx");
    	from.copyTo(Gdx.files.external(PASTE_LOCATION));
    	from = Gdx.files.internal("data/DungeonGame/saves/lava.tsx");
    	from.copyTo(Gdx.files.external(PASTE_LOCATION));
    	from = Gdx.files.internal("data/DungeonGame/saves/grey.tsx");
    	from.copyTo(Gdx.files.external(PASTE_LOCATION));
    	from = Gdx.files.internal("data/DungeonGame/saves/dungeon_objects.tsx");
    	from.copyTo(Gdx.files.external(PASTE_LOCATION));
    	from = Gdx.files.internal("data/DungeonGame/saves/dungeon_objects.png");
    	from.copyTo(Gdx.files.external(PASTE_LOCATION));
    	from = Gdx.files.internal("data/DungeonGame/saves/dungeon_tileset.png");
    	from.copyTo(Gdx.files.external(PASTE_LOCATION));
    	from = Gdx.files.internal("data/DungeonGame/saves/grey.png");
    	from.copyTo(Gdx.files.external(PASTE_LOCATION));
    	from = Gdx.files.internal("data/DungeonGame/saves/lava.png");
    	from.copyTo(Gdx.files.external(PASTE_LOCATION));
    	// load GameScreen assets
        gameStageImageMap = GameStageImages.loadGameStageImages();
        modelImageMap = Models.loadModelImages();
        soundMap = Sounds.loadAllSounds();
        musicMap = Musics.loadAllMusics();
        saveMap = GameSaves.loadGameSaves();
        difficultyQueue = Difficulty.loadDifficulty();
        // switch Game Cursor
        Pixmap pm = new Pixmap(Gdx.files.internal(GAME_CURSOR_LOCATION));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        // create Game Stages 
        gameStage = new GameStage(this,this,this,this);
    	mainMenuStage = new MainMenuStage(this,this,gameStage.getWorldManager(),gameStage.getSpriteManager());
    	saveStage = new SaveStage(this,this,this,gameStage,difficultyQueue);
    	loadStage = new LoadStage(this,this,this,this,gameStage,difficultyQueue);
    	optionStage = new OptionStage(this,gameStage.getSoundManager());
    	loadingStage = new LoadingStage(this,gameStage);
    	deathStage = new DeathStage(this,this);
    	highscoreStage = new HighscoreStage(this,this);
    	// Shutdown Hook --> releases textures
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
    	    @Override
    	    public void run() {
    	    	difficultyQueue.clear();
    	    	saveMap.clear();
    	    	musicMap.clear();
    	    	soundMap.clear();
    	    	modelImageMap.clear();
    	    	gameStageImageMap.clear();
    	    }
    	}){});
    	// set input processor
        Gdx.input.setInputProcessor(mainMenuStage);
    }

    public void render (float delta) {
    	if(!isPaused) {
	    	// clear screen
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	        // draw stages
	        mainMenuStage.draw();
	        gameStage.draw();
	        saveStage.draw();
	        loadStage.draw();
	        optionStage.draw();
	        loadingStage.draw();
	        deathStage.draw();
    	}
    }
    
    @Override
    public void switchToGameStage(boolean makeNewLevel) {
    	if(makeNewLevel) {
    		// set everything to default
    		if(gameStage.getDifficulty() != null)
    			gameStage.setDifficulty(Difficulty.LEVEL1);
    		difficultyQueue = Difficulty.loadDifficulty();
    		gameStage.setGold(0);
    		gameStage.getSpriteManager().getKnight().setDead(false);
    		gameStage.getSpriteManager().getKnight().setHealth(Knight.DEFAULT_HEALTH_VALUE);
    		gameStage.getSpriteManager().getKnight().setMaxHealth(Knight.DEFAULT_HEALTH_VALUE);
    		gameStage.getSpriteManager().getKnight().setStamina(Knight.DEFAULT_STAMINA_VALUE);
    		gameStage.getSpriteManager().getKnight().setMaxStamina(Knight.DEFAULT_STAMINA_VALUE);
    		gameStage.getEventManager().killMonsters();
    		gameStage.getSpriteManager().getKnight().setMovementSpeed(Knight.DEFAULT_MOVEMENT_SPEED);
    		switchToLoadingStage(difficultyQueue.last().getMonsterSpawnChance(), difficultyQueue.last().getRoomSpawnChance());
    	}
    	gameStage.getSoundManager().getSoundscape().playBackgroundMusic();
        gameStage.setVisible(true);
        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void switchToMainMenu(boolean hasCurrentLevel) {
    	gameStage.getSoundManager().getSoundscape().stopBackgroundMusic();
    	mainMenuStage.getResumeButton().setVisible(hasCurrentLevel);
    	mainMenuStage.setVisible(true);
        Gdx.input.setInputProcessor(mainMenuStage);
    }
    
    @Override
    public void switchToLoadStage(SpriteManager spriteManager, WorldManager worldManager, boolean lastStageWasMainMenu) {
    	loadStage.updateDates(this);
        loadStage.setVisible(true);
        loadStage.setLastStageWasMainMenu(lastStageWasMainMenu);
        Gdx.input.setInputProcessor(loadStage);
    }
    
    @Override
    public void switchToSaveStage(SpriteManager spriteManager, WorldManager worldManager) {
        saveStage.setVisible(true);
        saveStage.setGameStage(spriteManager, worldManager);
        Gdx.input.setInputProcessor(saveStage);
    }
    
    @Override
    public void switchToOptionStage(boolean lastStageWasMainMenu) {
    	optionStage.setLastStageWasMainMenu(lastStageWasMainMenu);
    	optionStage.setVisible(true);
    	Gdx.input.setInputProcessor(optionStage);
    }
    
	@Override
	public void switchToLoadingStage(int monsterSpawnChance, int roomSpawnChance) {
		loadingStage.setVisible(true);
		Gdx.input.setInputProcessor(loadingStage);
		loadingStage.setMonsterSpawnChance(monsterSpawnChance);
		loadingStage.setRoomSpawnChance(roomSpawnChance);
		loadingStage.getTimer().start();
	}
	
	@Override
	public void switchToDeathStage() {
		gameStage.getSoundManager().getSoundscape().stopBackgroundMusic();
		deathStage.setVisible(true);
		Gdx.input.setInputProcessor(deathStage);
	}
	
	@Override
	public void switchToHighscoreStage() {
		highscoreStage.setVisible(true);
		Gdx.input.setInputProcessor(highscoreStage);
	}

    @Override
    public void dispose () {
    	mainMenuStage.dispose();
        gameStage.dispose();
        saveStage.dispose();
    }

	@Override
	public void resize(int width, int height) {
		gameStage.getWorldManager().getCamera().resize(width, height);
		mainMenuStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		gameStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		saveStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		loadStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		optionStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		loadingStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		deathStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
	}

	@Override
	public void pause() {
		isPaused = true;
		gameStage.getSoundManager().getSoundscape().stopBackgroundMusic();
	}

	@Override
	public void resume() {
		isPaused = false;
		gameStage.getSoundManager().getSoundscape().playBackgroundMusic();
	}

	@Override
	public void show() {
		Gdx.app.log("show", this.getClass().toString() + " is now the current Screen for the Game");
	}

	@Override
	public void hide() {
		Gdx.app.log("hide", this.getClass().toString() + " is no longer the current Screen for the Game");
	}
	
	@Override
	public Image getImage(GameStageImages image) {
	    return gameStageImageMap.get(image);
	}
	
	@Override
	public Texture getModelTexture(Models texture) {
		return modelImageMap.get(texture);
	}
	
	@Override
	public Sound getSound(Sounds sound) {
	    return soundMap.get(sound);
	}
	
	@Override
	public Music getMusic(Musics music) {
		return musicMap.get(music);
	}

	@Override
	public String getPath(GameSaves fileHandle) {
		return saveMap.get(fileHandle);
	}

	@Override
	public Difficulty getDifficulty() {
		if(difficultyQueue == null) {
			Gdx.app.log("GetDifficulty", "difficultyQueue should not be null!",new NullPointerException());
			return Difficulty.LEVEL1; // set to first Level
		}
		return (difficultyQueue.size >= 1) ? difficultyQueue.last() : Difficulty.LEVEL20;
	}

	@Override
	public void removeDifficulty() {
		if(difficultyQueue == null)
			Gdx.app.log("removeDifficulty", "difficultyQueue should not be null!",new NullPointerException());
		else if(difficultyQueue.size > 1)
			difficultyQueue.removeLast();
	}

	public GameStage getGameStage() {
		return gameStage;
	}

}