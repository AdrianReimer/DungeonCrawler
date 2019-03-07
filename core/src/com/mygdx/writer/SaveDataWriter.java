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

package com.mygdx.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.enums.Difficulty;
import com.mygdx.model.DefaultModel;
import com.mygdx.model.Knight;
import com.mygdx.model.Slime;

/**
 * Save Data Writer.
 * @author Adrian Reimer
 *
 */
public class SaveDataWriter {
	
	/**
	 * writes save data files for the related map.
	 * @param list | the {@link List} of {@link DefaultModel} of the DungeonLevel.
	 * @param knight | {@link Knight}.
	 * @param filepath | path where the save data file should be stored.
	 * @param gold | amount of gold collected.
	 * @param difficultyQueue | {@link Difficulty}.
	 */
	@SuppressWarnings("unchecked")
	public void writeMapSaveFile(List<DefaultModel> list,Knight knight, String filepath, int gold, Queue<Difficulty> difficultyQueue) {
		int i = 0;
		JSONObject obj = new JSONObject();
		// save date
		JSONObject startobj = new JSONObject();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		startobj.put("date",dateFormat.format(date));
		obj.put("metadata", startobj);
		// save level data
		JSONObject levelobj = new JSONObject();
		levelobj.put("gold", gold);
		levelobj.put("difficultyQueue", difficultyQueue.toString());
		obj.put("leveldata", levelobj);
		// save knight data
		JSONObject knightobj = new JSONObject();
		knightobj.put("health",knight.getHealth());
		knightobj.put("maxHealth",knight.getMaxHealth());
		knightobj.put("stamina",knight.getStamina());
		knightobj.put("maxStamina",knight.getMaxStamina());
		knightobj.put("x",knight.getSprite().getX());
		knightobj.put("y",knight.getSprite().getY());
		obj.put("knight", knightobj);
		for(DefaultModel model: list) {
			if(model.getSprite() == null) {
				continue;
			}
			else if(model.getName().equals(Slime.class.toString())) {
				// save all slimes
				JSONObject slimeObj = new JSONObject();
				slimeObj.put("health",model.getHealth());
				slimeObj.put("attackDamage",model.getAttackDamage());
				slimeObj.put("x",model.getSprite().getX());
				slimeObj.put("y",model.getSprite().getY());
				obj.put("slime"+i, slimeObj);
			}
			i++;
		}
		try(FileWriter fw = new FileWriter(filepath)) {
			fw.write(obj.toJSONString());
			Gdx.app.log("SaveData", "Successfully Copied JSON Object to File");
		} catch (IOException e1) {
			Gdx.app.log("SaveData", "Error copying JSON Object to File",e1);
		}
	}
	
	/**
	 * writes a highscore data file for the Game.
	 * @param name | name of the Player.
	 * @param gold | amount of gold collected.
	 * @param highscoreList | currently saved highscores.
	 * @param filepath | save destination path.
	 */
	@SuppressWarnings("unchecked")
	public void writeHighscoreSaveFile(String name, int gold, List<Highscore> highscoreList, String filepath) {
		boolean highscoreIsAdded = false;
		int highscoreNumber = 1;
		JSONObject obj = new JSONObject();
		// add older highscores
		for(Highscore highscore : highscoreList) {
			JSONObject highscores = new JSONObject();
			if(name.equals(highscore.getName())) {
				// name already exists in List
				highscoreIsAdded = true;
				if(gold > highscore.getGold())
					highscore.setGold(gold);
			}
			highscores.put("name", highscore.getName());
			highscores.put("gold", highscore.getGold());
			obj.put("Player"+highscoreNumber, highscores);
			highscoreNumber++;
		}
		// save current Player
		if(!highscoreIsAdded) {
			JSONObject highscores = new JSONObject();
			highscores.put("name", name);
			highscores.put("gold", gold);
			obj.put("Player"+highscoreNumber, highscores);
			highscoreNumber++;
		}
		// write file
		try(FileWriter fw = new FileWriter(filepath)) {
			fw.write(obj.toJSONString());
			Gdx.app.log("SaveHighscores", "Successfully Copied JSON Object to File");
		} catch (IOException e1) {
			Gdx.app.log("SaveHighscores", "Error copying JSON Object to File",e1);
		}
	}
	
}
