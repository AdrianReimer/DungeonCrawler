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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
import com.mygdx.Manager.SpriteManager;
import com.mygdx.Manager.WorldManager;
import com.mygdx.constant.GameConstants;
import com.mygdx.enums.Difficulty;
import com.mygdx.enums.GameSaves;
import com.mygdx.enums.GameTexts;
import com.mygdx.enums.TileLayers;
import com.mygdx.music.SoundInterface;
import com.mygdx.screen.GameScreen;
import com.mygdx.writer.SaveDataWriter;
import com.mygdx.writer.TmxWriter;

public class SaveStage extends Stage {
	
	private static final float WINDOW_SPACE_RIGHT = 50f;

	private boolean visible = false;
	private StageSwitchInterface stageSwitchInterface;
	private Queue<Difficulty> difficultyQueue;
	private SpriteManager spriteManager;
	private WorldManager worldManager;
	private TmxWriter tmxWriter;
	private SaveDataWriter saveDataWriter;
	private String floorLayer;
	private String objLayer;
	private Label date1;
	private Label date2;
	private Label date3;
	private Label date4;
	private Label date5;

	public SaveStage(final StageSwitchInterface stageSwitchInterface,final SoundInterface soundInterface, final GameSaveInterface gameSaveInterface, GameStage gameStage, Queue<Difficulty> difficultyQueue) {
		this.stageSwitchInterface = stageSwitchInterface;
		this.difficultyQueue = difficultyQueue;
		tmxWriter = new TmxWriter();
		saveDataWriter = new SaveDataWriter();
		Skin skin = GameScreen.SKIN;
		Table rootTable = new Table(skin);
		rootTable.background(GameConstants.TABLE_BACKGROUND);
		rootTable.setFillParent(true);
		Window window = new Window(GameTexts.SAVE_STAGE_LABEL.get(), skin, GameConstants.DIALOG_WINDOW_STYLE);
		window.setMovable(false);
		
		date1 = new Label(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_1_FILEPATH)),skin);
		date2 = new Label(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_2_FILEPATH)),skin);
		date3 = new Label(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_3_FILEPATH)),skin);
		date4 = new Label(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_4_FILEPATH)),skin);
		date5 = new Label(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_5_FILEPATH)),skin);

		TextButton saveSpace1 = new TextButton(GameTexts.SAVE_STAGE_BUTTON1.get(), skin);
		saveSpace1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				floorLayer = encodeToCSV((TiledMapTileLayer) worldManager.getTiledMap().getLayers().get(TileLayers.FLOOR.get()));
				objLayer = encodeToCSV((TiledMapTileLayer) worldManager.getTiledMap().getLayers().get(TileLayers.OBJECT.get()));
				tmxWriter.writeTmxFile(floorLayer, objLayer, gameSaveInterface.getPath(GameSaves.MAP_SAVE_1_FILEPATH));
				saveDataWriter.writeMapSaveFile(spriteManager.getModelList(),spriteManager.getKnight(), gameSaveInterface.getPath(GameSaves.SAVE_DATA_1_FILEPATH),gameStage.getGold(),difficultyQueue);
				date1.setText(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_1_FILEPATH)));
			}
		});
		TextButton saveSpace2 = new TextButton(GameTexts.SAVE_STAGE_BUTTON2.get(), skin);
		saveSpace2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				floorLayer = encodeToCSV((TiledMapTileLayer) worldManager.getTiledMap().getLayers().get(TileLayers.FLOOR.get()));
				objLayer = encodeToCSV((TiledMapTileLayer) worldManager.getTiledMap().getLayers().get(TileLayers.OBJECT.get()));
				tmxWriter.writeTmxFile(floorLayer, objLayer, gameSaveInterface.getPath(GameSaves.MAP_SAVE_2_FILEPATH));	
				saveDataWriter.writeMapSaveFile(spriteManager.getModelList(),spriteManager.getKnight(), gameSaveInterface.getPath(GameSaves.SAVE_DATA_2_FILEPATH),gameStage.getGold(),difficultyQueue);
				date2.setText(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_2_FILEPATH)));
			}
		});
		TextButton saveSpace3 = new TextButton(GameTexts.SAVE_STAGE_BUTTON3.get(), skin);
		saveSpace3.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				floorLayer = encodeToCSV((TiledMapTileLayer) worldManager.getTiledMap().getLayers().get(TileLayers.FLOOR.get()));
				objLayer = encodeToCSV((TiledMapTileLayer) worldManager.getTiledMap().getLayers().get(TileLayers.OBJECT.get()));
				tmxWriter.writeTmxFile(floorLayer, objLayer, gameSaveInterface.getPath(GameSaves.MAP_SAVE_3_FILEPATH));	
				saveDataWriter.writeMapSaveFile(spriteManager.getModelList(),spriteManager.getKnight(), gameSaveInterface.getPath(GameSaves.SAVE_DATA_3_FILEPATH),gameStage.getGold(),difficultyQueue);
				date3.setText(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_3_FILEPATH)));
			}
		});
		TextButton saveSpace4 = new TextButton(GameTexts.SAVE_STAGE_BUTTON4.get(), skin);
		saveSpace4.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				floorLayer = encodeToCSV((TiledMapTileLayer) worldManager.getTiledMap().getLayers().get(TileLayers.FLOOR.get()));
				objLayer = encodeToCSV((TiledMapTileLayer) worldManager.getTiledMap().getLayers().get(TileLayers.OBJECT.get()));
				tmxWriter.writeTmxFile(floorLayer, objLayer, gameSaveInterface.getPath(GameSaves.MAP_SAVE_4_FILEPATH));	
				saveDataWriter.writeMapSaveFile(spriteManager.getModelList(),spriteManager.getKnight(), gameSaveInterface.getPath(GameSaves.SAVE_DATA_4_FILEPATH),gameStage.getGold(),difficultyQueue);
				date4.setText(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_4_FILEPATH)));
			}
		});
		TextButton saveSpace5 = new TextButton(GameTexts.SAVE_STAGE_BUTTON5.get(), skin);
		saveSpace5.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				floorLayer = encodeToCSV((TiledMapTileLayer) worldManager.getTiledMap().getLayers().get(TileLayers.FLOOR.get()));
				objLayer = encodeToCSV((TiledMapTileLayer) worldManager.getTiledMap().getLayers().get(TileLayers.OBJECT.get()));
				tmxWriter.writeTmxFile(floorLayer, objLayer, gameSaveInterface.getPath(GameSaves.MAP_SAVE_5_FILEPATH));	
				saveDataWriter.writeMapSaveFile(spriteManager.getModelList(),spriteManager.getKnight(), gameSaveInterface.getPath(GameSaves.SAVE_DATA_5_FILEPATH),gameStage.getGold(),difficultyQueue);
				date5.setText(loadSaveDataDate (gameSaveInterface.getPath(GameSaves.SAVE_DATA_5_FILEPATH)));
			}
		});
		TextButton back = new TextButton(GameTexts.SAVE_STAGE_BACK_BUTTON.get(), skin);
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				escapeEvent();
			}
		});
		window.add(saveSpace1).spaceRight(WINDOW_SPACE_RIGHT);
		window.add(date1);
		window.row();
		window.add(saveSpace2).spaceRight(WINDOW_SPACE_RIGHT);
		window.add(date2);
		window.row();
		window.add(saveSpace3).spaceRight(WINDOW_SPACE_RIGHT);
		window.add(date3);
		window.row();
		window.add(saveSpace4).spaceRight(WINDOW_SPACE_RIGHT);
		window.add(date4);
		window.row();
		window.add(saveSpace5).spaceRight(WINDOW_SPACE_RIGHT);
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
	
	private void escapeEvent() {
		visible = false;
		stageSwitchInterface.switchToGameStage(false);
	}

	@Override
	public void draw() {
		if (visible) {
			act(Gdx.graphics.getDeltaTime());
			super.draw();
		}
	}

	private String encodeToCSV(TiledMapTileLayer tmtl) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int y = 499; y >= 0; y--) {
			for (int x = 0; x < 499; x++) {
				if (tmtl.getCell(x, y) == null) {
					stringBuilder.append("-1,");
				} else {
					stringBuilder.append(tmtl.getCell(x, y).getTile().getId() + ",");
				}
			}
			stringBuilder.append("757,");
		}
		return stringBuilder.toString();
	}
	
	private String loadSaveDataDate (String filepath) {
		JSONParser parser = new JSONParser();
		String date = "";
		try {   
            Object obj = parser.parse(new FileReader(filepath));
            JSONObject jsonObject =  (JSONObject) obj;
            JSONObject metadata = (JSONObject)jsonObject.get("metadata");
            date = (String) metadata.get("date");
        } catch (IOException | ParseException e) {
        	Gdx.app.log("LoadDate", "Error loading SaveDataDate",e);
        }
		return date;
	}

	public void setGameStage(SpriteManager spriteManager,WorldManager worldManager) {
		this.spriteManager = spriteManager;
		this.worldManager = worldManager;
	}

	public Queue<Difficulty> getDifficultyQueue() {
		return difficultyQueue;
	}

	public void setDifficultyQueue(Queue<Difficulty> difficultyQueue) {
		this.difficultyQueue = difficultyQueue;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
