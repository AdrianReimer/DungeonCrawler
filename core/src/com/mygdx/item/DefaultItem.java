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

package com.mygdx.item;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.mygdx.collision.Collision;
import com.mygdx.model.Knight;

/**
 * An abstract Default Item that defines what every Item needs to have.
 * Every Item needs to inherit this Class.
 * @author Adrian Reimer
 *
 */
abstract class DefaultItem {


	TiledMapTileLayer collisionLayerObj; // possible interactive entities
	Cell cell; // holds important information
	Sprite knightSprite;
	
	/**
	 * Checks the {@link Cell} the {@link Knight} is currently on.
	 * @param tm | current {@link TiledMap}.
	 */
	void checkCell (TiledMap tm) {
		collisionLayerObj = (TiledMapTileLayer) tm.getLayers().get("obj");
		cell = collisionLayerObj
				.getCell(
						(int) ((knightSprite.getX() + Collision.TILE_SCALE) / Collision.TILE_SCALE),
						(int) ((knightSprite.getY() + Collision.TILE_OFFSET) / Collision.TILE_SCALE));
	}
	
	/**
	 * destroys the Item by setting its {@link Cell} to null.
	 */
	void destroyItem() {
		collisionLayerObj
				.setCell(
						(int) ((knightSprite.getX() + Collision.TILE_SCALE) / Collision.TILE_SCALE),
						(int) ((knightSprite.getY() + Collision.TILE_OFFSET) / Collision.TILE_SCALE),
						null);
	}
}