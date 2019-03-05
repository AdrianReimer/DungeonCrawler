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

package com.mygdx.collision;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.mygdx.enums.CellProperties;
import com.mygdx.enums.TileLayers;
import com.mygdx.model.DefaultModel;

/**
 * Collision Detection each {@link DefaultModel} has.
 * @author Adrian Reimer
 *
 */
public class Collision {
	
	public static final int TILE_SCALE = 32;
	public static final int TILE_OFFSET = 22;
	
	private int boundingBox;
	private DefaultModel defaultModel;
	private TiledMap tm;
	
	/**
	 * Collision constructor.
	 * @param defaultModel | Model that will use this Collision Detection.
	 * @param tm | current {@link TiledMap}.
	 */
	public Collision(DefaultModel defaultModel, TiledMap tm){
		boundingBox = 4;
		this.defaultModel = defaultModel;
		this.tm = tm;
	}

	/**
	 * updates by trying to move the models {@link Sprite} and checking
	 * if it is on a {@link Cell} with collision properties.
	 */
	public void update () {
		TiledMapTileLayer collisionLayerFloor = (TiledMapTileLayer)tm.getLayers().get(TileLayers.FLOOR.get());
		TiledMapTileLayer collisionLayerObj = (TiledMapTileLayer)tm.getLayers().get(TileLayers.OBJECT.get());
		float oldXPos = defaultModel.getSprite().getX(); // old x Position of Sprite
		float oldYPos = defaultModel.getSprite().getY(); // old y Position of Sprite
		defaultModel.translateSprite(); // moves Model

		boolean isColliding = (collides(collisionLayerFloor,boundingBox,defaultModel.getSprite()) 
							|| collides(collisionLayerObj,boundingBox,defaultModel.getSprite())) 
							|| (collides(collisionLayerFloor,-boundingBox,defaultModel.getSprite()) 
							|| collides(collisionLayerObj,-boundingBox,defaultModel.getSprite()));
		if(isColliding) {
			defaultModel.getSprite().setX(oldXPos);
			defaultModel.getSprite().setY(oldYPos);	
		}
	}
	
	/**
	 * Checks if {@link Sprite} is colliding with a given Layer.
	 * @param tmtl | floor/object Layer of the current {@link TiledMap}.
	 * @param boundingBox | box scale around the Model to give it a believable feel.
	 * @param sprite | {@link Sprite} of the Model.
	 * @return true if {@link Sprite} is colliding with a cell, else false.
	 */
	private boolean collides (TiledMapTileLayer tmtl, int boundingBox,Sprite sprite) {
		Cell cell = tmtl.getCell((int) ((sprite.getX()+TILE_SCALE+boundingBox)/TILE_SCALE), (int) ((sprite.getY()+TILE_OFFSET)/TILE_SCALE));
		if(cell == null) {
			return false;
		}
		return cell.getTile().getProperties().containsKey(CellProperties.COLLISION.get());
	}
}