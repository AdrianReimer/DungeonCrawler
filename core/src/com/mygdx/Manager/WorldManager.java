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

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.camera.Camera;
import com.mygdx.model.Knight;

/**
 * World Manager that handles the {@link TiledMap}.
 * @author Adrian Reimer
 *
 */
public class WorldManager implements Disposable {
	
	private boolean isUpdatingLevel;
	private TiledMap tm;
	private OrthogonalTiledMapRenderer tmr;
	private Camera camera;
	
	/**
	 * WorldManager constructor.
	 */
	public WorldManager() {
		camera = new Camera();
		tm = new TiledMap();
	}
	
	/**
	 * Updates the current {@link TiledMap} (Dungeon Level).
	 * @param knightSprite | {@link Sprite} of the {@link Knight}.
	 */
	public void updateWorld(Sprite knightSprite) {
		camera.updateCamera(knightSprite);
		tmr.setView(camera.getCamera());
		tmr.render();
	}
	
	@Override
	public void dispose() {
		if(tm == null || tmr == null) return;
		tm.dispose();
		tmr.dispose();
		isUpdatingLevel = false;
	}

	public TiledMap getTiledMap() {
		return tm;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setTiledMap(TiledMap tm) {
		if(tm != null) 
			isUpdatingLevel = true;
		this.tm = tm;
	}

	public void setTiledMapRenderer(OrthogonalTiledMapRenderer tmr) {
		this.tmr = tmr;
	}

	public boolean isUpdatingLevel() {
		return isUpdatingLevel;
	}

	public void setUpdatingLevel(boolean isUpdatingLevel) {
		this.isUpdatingLevel = isUpdatingLevel;
	}
	
}