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

package com.mygdx.dungeon;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

/**
 * RoomConnection helper class that holds a {@link Cell} with its associated x and y Position.
 * @author Adrian Reimer
 *
 */
public final class RoomConnection {
	
	private Cell cell;
	private int xPos;
	private int yPos;
	
	/**
	 * RoomConnection constructor.
	 * @param cell | {@link Cell}.
	 * @param xPos | x Position of the {@link Cell}.
	 * @param yPos | y Position of the {@link Cell}.
	 */
	public RoomConnection (Cell cell, int xPos, int yPos) {
		this.cell = cell;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public Cell getCell() {
		return cell;
	}
	
	public int getxPos() {
		return xPos;
	}
	
	public int getyPos() {
		return yPos;
	}
	
}
