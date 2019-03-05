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

package com.mygdx.enums;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.screen.GameScreen;

public enum GameSaves {
	// saves
	MAP_SAVE_1_FILEPATH(Gdx.files.external(System.getProperty(GameScreen.USER_HOME)+"/Documents/DungeonGame/saves/save1.tmx")),
	MAP_SAVE_2_FILEPATH(Gdx.files.external(System.getProperty(GameScreen.USER_HOME)+"/Documents/DungeonGame/saves/save2.tmx")),
	MAP_SAVE_3_FILEPATH(Gdx.files.external(System.getProperty(GameScreen.USER_HOME)+"/Documents/DungeonGame/saves/save3.tmx")),
	MAP_SAVE_4_FILEPATH(Gdx.files.external(System.getProperty(GameScreen.USER_HOME)+"/Documents/DungeonGame/saves/save4.tmx")),
	MAP_SAVE_5_FILEPATH(Gdx.files.external(System.getProperty(GameScreen.USER_HOME)+"/Documents/DungeonGame/saves/save5.tmx")),
	SAVE_DATA_1_FILEPATH(Gdx.files.external(System.getProperty(GameScreen.USER_HOME)+"/Documents/DungeonGame/saves/save_data1.txt")),
	SAVE_DATA_2_FILEPATH(Gdx.files.external(System.getProperty(GameScreen.USER_HOME)+"/Documents/DungeonGame/saves/save_data2.txt")),
	SAVE_DATA_3_FILEPATH(Gdx.files.external(System.getProperty(GameScreen.USER_HOME)+"/Documents/DungeonGame/saves/save_data3.txt")),
	SAVE_DATA_4_FILEPATH(Gdx.files.external(System.getProperty(GameScreen.USER_HOME)+"/Documents/DungeonGame/saves/save_data4.txt")),
	SAVE_DATA_5_FILEPATH(Gdx.files.external(System.getProperty(GameScreen.USER_HOME)+"/Documents/DungeonGame/saves/save_data5.txt"));
    
    private FileHandle filePath = null;

    GameSaves(FileHandle filePath) {
        this.filePath = filePath;
    }

    public String get() {
        if (filePath != null) {
            return filePath.path();
        } else {
            throw new IllegalArgumentException("No filepath for this save!");
        }
    }
    
    public static ObjectMap<GameSaves, String> loadGameSaves() {
        ObjectMap<GameSaves, String> saveMap = new ObjectMap<>();
        for (GameSaves t : GameSaves.values()) {
        	saveMap.put(t, t.get());
        }
        return saveMap;
    }
}
