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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.mygdx.ai.slime.Node;
import com.mygdx.collision.Collision;
import com.mygdx.enums.CellProperties;
import com.mygdx.model.Knight;

/**
 * Room Creator that is responsible for moving and connecting Rooms together.
 * @author Adrian Reimer
 *
 */
public class RoomCreator{
	
	public static final int MAX_ROOM_SIZE_X = 20;	// x=0 to x=20, y=499 to y=479   (20x20 square Room)
	public static final int MAX_ROOM_SIZE_Y = 479; 	// 
	public static final int DUNGEON_HALF = (DungeonCreator.DUNGEON_SCALE/2)+1; // center of Dungeon TileMap 
	
	private DungeonCreator dungeonCreator;
	private Cell brick; // cell with brick texture
	private Cell lava;	// cell with lava texture
	private Cell end; // cell with end texture
	private int connectionXPosOldRoom; // X connection Position of Room freshly loaded (left top) 
	private int connectionYPosOldRoom; // Y connection Position of Room freshly loaded (left top) 
	private int connectionXPosNewRoom; // X connection Position of Room perfectly aligned
	private int connectionYPosNewRoom; // Y connection Position of Room perfectly aligned
	private int shiftFactorX; // Factors needed to move the Room
	private int shiftFactorY; // 
	private Knight knight;
	
	/**
	 * RoomCreator constructor.
	 * @param dungeonCreator | {@link DungeonCreator}.
	 * @param knight | {@link Knight}.
	 */
	public RoomCreator(DungeonCreator dungeonCreator,Knight knight) {
		this.dungeonCreator = dungeonCreator;
		this.knight = knight;
	}
	
	/**
	 * Creates a new Spawn Room and moves it to the middle of the {@link TiledMap}.
	 * @param tmtl1 | Floor {@link TiledMapTileLayer}.
	 * @param tmtl2 | Object {@link TiledMapTileLayer}.
	 */
	public void createSpawnRoom (TiledMapTileLayer tmtl1,TiledMapTileLayer tmtl2) {
		// cells baked into each spawnRoom
		try {
			brick = tmtl1.getCell(0, 0);
			lava = tmtl1.getCell(1, 0);
			end = tmtl1.getCell(2, 0);
		} catch (NullPointerException e) {
			Gdx.app.log("CreateSpawnRoom", "Error getting baked Cells",e);
		}
		// moves spawn room to the middle of the map
		for(int x = 0; x < MAX_ROOM_SIZE_X ; x++) {
			for(int y = DungeonCreator.DUNGEON_SCALE; y > MAX_ROOM_SIZE_Y ; y--) {
				tmtl1.setCell(x+DUNGEON_HALF, y-DUNGEON_HALF, tmtl1.getCell(x,y));
				tmtl2.setCell(x+DUNGEON_HALF, y-DUNGEON_HALF, tmtl2.getCell(x,y));
				if(tmtl1.getCell(x+DUNGEON_HALF, y-DUNGEON_HALF).getTile().getProperties().containsKey(CellProperties.SPAWN.get())) {
					// defines spawn Position
					knight.setSpawnX((x+DUNGEON_HALF) * Collision.TILE_SCALE - Collision.TILE_SCALE/2);
					knight.setSpawnY((y-DUNGEON_HALF) * Collision.TILE_SCALE - Collision.TILE_SCALE/2);
				}
				if(tmtl1.getCell(x+DUNGEON_HALF, y-DUNGEON_HALF).getTile().getProperties().containsKey(CellProperties.CONNECTION.get())) {
					// adds further Room connections to Queue
					dungeonCreator.getConnections().addLast(new RoomConnection(tmtl1.getCell(x + DUNGEON_HALF, y - DUNGEON_HALF),x + DUNGEON_HALF,y - DUNGEON_HALF));
					dungeonCreator.getEnemySpawns().addLast(new Node<Integer>(((x + DUNGEON_HALF)*Collision.TILE_SCALE)-(Collision.TILE_SCALE/2),((y- DUNGEON_HALF)*Collision.TILE_SCALE)-(Collision.TILE_SCALE/2)));
				}
			}
		}
	}
	
	/**
	 * Tries to connect the new {@link TiledMap} with the root {@link TiledMap}.
	 * @param tmtl1Floor | root Floor {@link TiledMapTileLayer}.
	 * @param tmtl1Obj | root Object {@link TiledMapTileLayer}.
	 * @param tmtl2Floor | new Floor {@link TiledMapTileLayer}.
	 * @param tmtl2Obj | new Object {@link TiledMapTileLayer}.
	 * @param roomType | {@link CellProperties} roomType (north/east/south/west).
	 */
	public void createRoom (TiledMapTileLayer tmtl1Floor,TiledMapTileLayer tmtl1Obj,TiledMapTileLayer tmtl2Floor,TiledMapTileLayer tmtl2Obj,CellProperties roomType) {
		setConnectionPositionOfNewRoom (roomType);							
		setConnectionPositionOfOldRoom (tmtl2Floor, roomType);
		
		shiftFactorX = connectionXPosNewRoom-connectionXPosOldRoom;
		shiftFactorY = DungeonCreator.DUNGEON_SCALE+(connectionYPosNewRoom-connectionYPosOldRoom);
		
		// moves Room to target Destination 
		for(int x = 0; x < MAX_ROOM_SIZE_X ; x++) {
			for(int y = 499; y > MAX_ROOM_SIZE_Y ; y--) {
				if(tmtl2Floor.getCell(x, y) != null) {
					tmtl2Floor.setCell(x+(connectionXPosNewRoom - connectionXPosOldRoom), y+(connectionYPosNewRoom-connectionYPosOldRoom), tmtl2Floor.getCell(x, y));
				} 
				if(tmtl2Obj.getCell(x, y) != null) {
					tmtl2Obj.setCell(x+(connectionXPosNewRoom - connectionXPosOldRoom), y+(connectionYPosNewRoom-connectionYPosOldRoom), tmtl2Obj.getCell(x, y));
				} 
			}
		}
		// checks if Rooms are overlapping
		if(!isOverlapping(tmtl1Floor,tmtl2Floor)) {
			connect(tmtl1Floor,tmtl2Floor,tmtl1Obj,tmtl2Obj);
		}
	}
	
	/**
	 * sets the connection position of the new Room 
	 * by overlapping the connection {@link Cell}s of both {@link TiledMap}s.
	 * @param roomType | {@link CellProperties} roomType (north/east/south/west).
	 */
	private void setConnectionPositionOfNewRoom (CellProperties roomType) {
		connectionXPosNewRoom = dungeonCreator.getConnections().first().getxPos();
		connectionYPosNewRoom = dungeonCreator.getConnections().first().getyPos();
		
		if(roomType == CellProperties.NORTH) {
			connectionYPosNewRoom--;
		} else if(roomType == CellProperties.EAST) {
			connectionXPosNewRoom--;
		} else if(roomType == CellProperties.SOUTH) {
			connectionYPosNewRoom++;
		} else if(roomType == CellProperties.WEST) {
			connectionXPosNewRoom++;
		}
	}
	
	/**
	 * sets the connection position of the old Room.
	 * @param tmtl2Floor | Floor {@link TiledMapTileLayer}.
	 * @param roomType | {@link CellProperties} roomType (north/east/south/west).
	 */
	private void setConnectionPositionOfOldRoom (TiledMapTileLayer tmtl2Floor, CellProperties roomType) {
		// defines connection Position of freshly loaded Room (left top)
		for(int x = 0; x < MAX_ROOM_SIZE_X ; x++) {
			for(int y = DungeonCreator.DUNGEON_SCALE; y > MAX_ROOM_SIZE_Y ; y--) {
				if(tmtl2Floor.getCell(x, y) != null && tmtl2Floor.getCell(x, y).getTile().getProperties().containsKey(roomType.get())) {
					connectionXPosOldRoom = x;
					connectionYPosOldRoom = y;
					// replaces connection Cell texture 
					tmtl2Floor.setCell(x, y, brick);
					return;
				}
			}
		}
	}
	
	/**
	 * Checks if both Floor {@link TiledMapTileLayer}s are Overlapping (is there room for the new loaded TiledMap Room).
	 * @param tmtl1 | root Floor {@link TiledMapTileLayer}.
	 * @param tmtl2 | new Floor {@link TiledMapTileLayer}.
	 * @return true if {@link TiledMap}s are Overlapping, else false.
	 */
	private boolean isOverlapping(TiledMapTileLayer tmtl1,TiledMapTileLayer tmtl2) {
		for(int x = shiftFactorX; x < shiftFactorX+20 ; x++) {
			for(int y = shiftFactorY; y > shiftFactorY-20 ; y--) {
				if(tmtl2.getCell(x, y) != null && tmtl1.getCell(x, y) != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Connects the {@link TiledMap}s.
	 * @param tmtl1 | root Floor {@link TiledMapTileLayer}.
	 * @param tmtl2 | new Floor {@link TiledMapTileLayer}.
	 * @param tmtl3 | root Object {@link TiledMapTileLayer}.
	 * @param tmtl4 | new Object {@link TiledMapTileLayer}. 
	 */
	private void connect(TiledMapTileLayer tmtl1,TiledMapTileLayer tmtl2,TiledMapTileLayer tmtl3,TiledMapTileLayer tmtl4) {
		for(int x = shiftFactorX; x < shiftFactorX+20 ; x++) {
			for(int y = shiftFactorY; y > shiftFactorY-20 ; y--) {
				if(tmtl2.getCell(x, y) != null) {
					tmtl1.setCell(x, y, tmtl2.getCell(x, y));
					tmtl3.setCell(x, y, tmtl4.getCell(x, y));
					if(tmtl2.getCell(x, y).getTile().getProperties().containsKey(CellProperties.CONNECTION.get())) {
						dungeonCreator.getConnections().addLast(new RoomConnection (tmtl1.getCell(x, y),x,y));
						dungeonCreator.getEnemySpawns().addLast(new Node<>((x*Collision.TILE_SCALE)-(Collision.TILE_SCALE/2),(y*Collision.TILE_SCALE)-(Collision.TILE_SCALE/2)));
					}
				}
			}
		}
	}
	
	public Cell getLavaCell () {
		return lava;
	}

	public Cell getBrickCell () {
		return brick;
	}

	public Cell getEnd() {
		return end;
	}

}
