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

package com.mygdx.stage;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.SerializationException;
import com.mygdx.Manager.EventManager;
import com.mygdx.collision.Collision;
import com.mygdx.constant.GameConstants;
import com.mygdx.enums.Difficulty;
import com.mygdx.enums.GameSaves;
import com.mygdx.enums.GameTexts;
import com.mygdx.model.ModelInterface;
import com.mygdx.model.Slime;
import com.mygdx.model.Knight;
import com.mygdx.music.SoundInterface;
import com.mygdx.screen.GameScreen;

/**
 * Load Stage when trying to load a save slot.
 * @author Adrian Reimer
 *
 */
public class LoadStage extends Stage{
	
	private static final float WINDOW_SPACE_RIGHT = 50f;

	private boolean visible = false;
	private boolean lastStageWasMainMenu = false;
	private LoadInterface loadInterface;
	private Queue<Difficulty> difficultyQueue;
	private TmxMapLoader loader = new TmxMapLoader();
	private StageSwitchInterface stageSwitchInterface;
	private ModelInterface modelInterface;
	private EventManager eventManager;
	private TiledMap tmMain;
	private TextButton back;
	private Label date1;
	private Label date2;
	private Label date3;
	private Label date4;
	private Label date5;

	/**
	 * LoadStage constructor.
	 * @param stageSwitchInterface | {@link StageSwitchInterface}.
	 * @param soundInterface | {@link SoundInterface}.
	 * @param gameSaveInterface | {@link GameSaveInterface}.
	 * @param loadInterface | {@link LoadInterface}.
	 * @param difficultyQueue | {@link Queue} of {@link Difficulty}s.
	 */
	public LoadStage(final StageSwitchInterface stageSwitchInterface,final SoundInterface soundInterface, final GameSaveInterface gameSaveInterface,final ModelInterface modelInterface, final LoadInterface loadInterface, EventManager eventManager, Queue<Difficulty> difficultyQueue) {
		this.loadInterface = loadInterface;
		this.difficultyQueue = difficultyQueue;
		this.stageSwitchInterface = stageSwitchInterface;
		this.modelInterface = modelInterface;
		this.eventManager = eventManager;
		Skin skin = GameScreen.SKIN;
		Table rootTable = new Table(skin);
		rootTable.background(GameConstants.TABLE_BACKGROUND);
		rootTable.setFillParent(true);
		Window window = new Window(GameTexts.LOAD_STAGE_LABEL.get(), skin, GameConstants.DIALOG_WINDOW_STYLE);
		window.setMovable(false);
		
		date1 = new Label(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_1_FILEPATH)),skin);
		date2 = new Label(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_2_FILEPATH)),skin);
		date3 = new Label(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_3_FILEPATH)),skin);
		date4 = new Label(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_4_FILEPATH)),skin);
		date5 = new Label(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_5_FILEPATH)),skin);

		TextButton loadSpace1 = new TextButton(GameTexts.LOAD_STAGE_BUTTON1.get(), skin);
		loadSpace1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				loadTmxMap(gameSaveInterface.getPath(GameSaves.MAP_SAVE_1_FILEPATH),gameSaveInterface.getPath(GameSaves.SAVE_DATA_1_FILEPATH));
			}
		});
		TextButton loadSpace2 = new TextButton(GameTexts.LOAD_STAGE_BUTTON2.get(), skin);
		loadSpace2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				loadTmxMap(gameSaveInterface.getPath(GameSaves.MAP_SAVE_2_FILEPATH),gameSaveInterface.getPath(GameSaves.SAVE_DATA_2_FILEPATH));
			}
		});
		TextButton loadSpace3 = new TextButton(GameTexts.LOAD_STAGE_BUTTON3.get(), skin);
		loadSpace3.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				loadTmxMap(gameSaveInterface.getPath(GameSaves.MAP_SAVE_3_FILEPATH),gameSaveInterface.getPath(GameSaves.SAVE_DATA_3_FILEPATH));
			}
		});
		TextButton loadSpace4 = new TextButton(GameTexts.LOAD_STAGE_BUTTON4.get(), skin);
		loadSpace4.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				loadTmxMap(gameSaveInterface.getPath(GameSaves.MAP_SAVE_4_FILEPATH),gameSaveInterface.getPath(GameSaves.SAVE_DATA_4_FILEPATH));
			}
		});
		TextButton loadSpace5 = new TextButton(GameTexts.LOAD_STAGE_BUTTON5.get(), skin);
		loadSpace5.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				loadTmxMap(gameSaveInterface.getPath(GameSaves.MAP_SAVE_5_FILEPATH),gameSaveInterface.getPath(GameSaves.SAVE_DATA_5_FILEPATH));
			}
		});
		back = new TextButton(GameTexts.LOAD_STAGE_BACK_BUTTON.get(), skin);
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				escapeEvent();
			}
		});
		window.add(loadSpace1).spaceRight(WINDOW_SPACE_RIGHT);
		window.add(date1);
		window.row();
		window.add(loadSpace2).spaceRight(WINDOW_SPACE_RIGHT);
		window.add(date2);
		window.row();
		window.add(loadSpace3).spaceRight(WINDOW_SPACE_RIGHT);
		window.add(date3);
		window.row();
		window.add(loadSpace4).spaceRight(WINDOW_SPACE_RIGHT);
		window.add(date4);
		window.row();
		window.add(loadSpace5).spaceRight(WINDOW_SPACE_RIGHT);
		window.add(date5);
		window.row();
		window.add(back).align(Align.left);
		rootTable.add(window);
		addActor(rootTable);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.ESCAPE)
			escapeEvent();
		return true;
	}
	
	/**
	 * handles event when esc-key is pressed.
	 */
	private void escapeEvent() {
		visible = false;
		if(lastStageWasMainMenu)
			stageSwitchInterface.switchToMainMenu();
		else	
			stageSwitchInterface.switchToGameStage(false);
	}
	
	/**
	 * updates the dates for the save spaces.
	 * @param gameSaveInterface
	 */
	public void updateDates (final GameSaveInterface gameSaveInterface) {
		date1.setText(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_1_FILEPATH)));
		date2.setText(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_2_FILEPATH)));
		date3.setText(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_3_FILEPATH)));
		date4.setText(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_4_FILEPATH)));
		date5.setText(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_5_FILEPATH)));
	}
	
	/**
	 * loads the selected {@link TiledMap}.
	 * @param mapPath | path where the {@link TiledMap} is stored.
	 * @param dataPath | path where the related data of that Level is stored.
	 */
	private void loadTmxMap (String mapPath, String dataPath) {
		try {
			tmMain = loader.load(mapPath);
		} catch (SerializationException e) {
			Gdx.app.log("LoadTmxMap", "Error loading TmxMap",e);
			return;
		}
		loadInterface.getWorldManager().dispose();
		loadInterface.getWorldManager().setTiledMap(tmMain);
		loadInterface.getWorldManager().setTiledMapRenderer(new OrthogonalTiledMapRenderer(tmMain));	
		loadSaveData(dataPath);
		eventManager.setOnFinish(false);
		visible = false;
		stageSwitchInterface.switchToGameStage(false);
	}
	
	/**
	 * Loads the date from the save-data .json file.
	 * @param filepath | path of the save-data .json file.
	 * @return date as a {@link String}.
	 */
	private String loadSaveDataDate (String filepath) {
		JSONParser parser = new JSONParser();
		String date = "";
		try {     
            Object obj = parser.parse(new FileReader(filepath));
            JSONObject jsonObject =  (JSONObject) obj;
            JSONObject metadata = (JSONObject)jsonObject.get("metadata");
            date = (String) metadata.get("date"); 
        } catch (IOException | ParseException e) {
        	Gdx.app.log("LoadSaveDataDate", "Error loading SaveDataDate",e);
        }
		return date;
	}
	
	/**
	 * Loads the save-data for the related {@link TiledMap}
	 * @param filepath | path of the save-data .json file.
	 */
	private void loadSaveData (String filepath) {
		loadInterface.getEventManager().killMonsters();
        JSONParser parser = new JSONParser();
        try(FileReader fr = new FileReader(filepath)) {
        	difficultyQueue.clear();
            Object obj = parser.parse(fr);
            int modelNumber = 1;
            JSONObject jsonObject =  (JSONObject) obj;
            // load level data
            JSONObject levelobj = (JSONObject)jsonObject.get("leveldata");
            loadInterface.setGold((int)(long)levelobj.get("gold"));
            String difficultyQueueString = (String) levelobj.get("difficultyQueue");
            difficultyQueueString = difficultyQueueString.replaceAll(" ", "");
            difficultyQueueString = difficultyQueueString.substring(1, difficultyQueueString.length()-1);
            String [] difficultyarray =  difficultyQueueString.split(",");
            for(String str : difficultyarray) {
            	difficultyQueue.addLast(Difficulty.valueOf(str));
            }
            loadInterface.setDifficulty(difficultyQueue.last());
            // load knight data
            JSONObject knightObj = (JSONObject)jsonObject.get("knight");
            Knight knight = loadInterface.getSpriteManager().getKnight();
            knight.setHealth((int)(long)knightObj.get("health"));
            knight.setMaxHealth((int)(long)knightObj.get("maxHealth"));
            knight.setMaxStamina((int)(long)knightObj.get("maxStamina"));
            knight.setStamina((int)(long)knightObj.get("stamina"));
            knight.getSprite().setX((float)(double)knightObj.get("x"));
            knight.getSprite().setY((float)(double)knightObj.get("y"));
            loadInterface.getSpriteManager().addModel(knight);
            // load all slimes
            for(JSONObject demonObj = (JSONObject)jsonObject.get("slime"+modelNumber);demonObj != null;demonObj = (JSONObject)jsonObject.get("slime"+modelNumber) ) {
            	Slime slime = new Slime(loadInterface.getSpriteManager(),loadInterface.getWorldManager().getTiledMap(),0,0,modelInterface);
            	slime.getSprite().setX((float)(double)demonObj.get("x"));
            	slime.getSprite().setY((float)(double)demonObj.get("y"));
            	slime.setHealth((int)(long)demonObj.get("health"));
            	slime.setAttackDamage(((int)(long)demonObj.get("attackDamage")));
            	loadInterface.getEventManager().getMonsters().addLast(slime);
            	loadInterface.getSpriteManager().addModel(slime);
            	modelNumber++;
            }
        } catch (IOException | ParseException e) {
        	Gdx.app.log("LoadSaveData", "Error loading SaveData",e);
        }
        loadInterface.getEventManager().setCollision(new Collision(loadInterface.getSpriteManager().getKnight(),loadInterface.getWorldManager().getTiledMap()));
	}

	@Override
	public void draw() {
		if (visible) {
			act(Gdx.graphics.getDeltaTime());
			super.draw();
		}
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Queue<Difficulty> getDifficultyQueue() {
		return difficultyQueue;
	}

	public void setDifficultyQueue(Queue<Difficulty> difficultyQueue) {
		this.difficultyQueue = difficultyQueue;
	}

	public void setLastStageWasMainMenu(boolean lastStageWasMainMenu) {
		this.lastStageWasMainMenu = lastStageWasMainMenu;
	}

}
