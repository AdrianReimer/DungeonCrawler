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

package com.mygdx.Manager;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.camera.Camera;
import com.mygdx.model.DefaultModel;
import com.mygdx.model.Knight;

/**
 * Sprite Manager that handles all {@link Sprite}s.
 * @author Adrian Reimer
 *
 */
public class SpriteManager implements Disposable {

	private SpriteBatch batch; // memory intensive

	private List<DefaultModel> modelList; // models in the current Level
	private Camera camera; // Camera focusing on the Player
	private Knight knight; // Player

	/**
	 * SpriteManager constructor.
	 * @param camera | {@link Camera}.
	 */
	public SpriteManager(Camera camera) {
		modelList = new ArrayList<>();
		batch = new SpriteBatch();
		this.camera = camera;
	}

	/**
	 * Updates all {@link Sprite}s by looping over them.
	 */
	public void updateSpriteList () {
		for(DefaultModel model: modelList) {
			if(model.getSprite() == null) {
				continue;
			}
			updateSprite(model.getSprite());
		}
	}
	
	/**
	 * draws the {@link Sprite} on the Screen.
	 * @param sprite | {@link Sprite}.
	 */
	private void updateSprite(Sprite sprite) {
		if(camera.getCamera() != null){
			batch.setProjectionMatrix(camera.getCamera().combined);
			batch.begin();
			sprite.draw(batch);
			batch.end();
		}
	}
	
	@Override
	public void dispose() {
		try {
			knight.dispose();
			for (DefaultModel model: modelList) {
				if(model == null) {
					continue;
				}
				model.dispose();
			}
		}catch (ConcurrentModificationException | NullPointerException e) {
			Gdx.app.log("DisposeSpriteList", "Error disposing Sprite from modelList",e);
		}
		modelList.clear();
		batch.dispose();
	}
	
	/**
	 * Adds a Model to the List.
	 * @param model | {@link DefaultModel}.
	 */
	public void addModel(DefaultModel model) {
		modelList.add(model);
	}

	public List<DefaultModel> getModelList() {
		return modelList;
	}
	
	public Knight getKnight() {
		return knight;
	}

	public void setKnight(Knight knight) {
		this.knight = knight;		
	}

	public void setModelList(List<DefaultModel> modelList) {
		this.modelList = modelList;
	}
	
}