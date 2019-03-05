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

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.ai.slime.Node;
import com.mygdx.constant.GameConstants;
import com.mygdx.enums.CellProperties;
import com.mygdx.enums.TileLayers;
import com.mygdx.model.Knight;

/**
 * Dungeon Creator that creates new Dungeon Levels by Loading Different {@link TiledMap}s
 * and connecting them afterwards. Rooms connect by specific {@link Cell}s with connection {@link CellProperties}.
 * @author Adrian Reimer
 *
 */
public class DungeonCreator {
	
	public static final int DUNGEON_SCALE = 499;	// x=0 to x=499, y=0 to y=499 (square)
	
	private static final String DUNGEON_ROOM = "data/maps/Dungeon_room_"; // Dungeon room Folder
	private static final String DUNGEON_ROOM_FORMAT = ".tmx"; // Tiled Map Format
	private static final int MAX_AMOUNT_OF_ROOMS = 80;
	private static final int NUMBER_OF_SPAWNABLE_ROOMS = 50; // 50 possible rooms for each direction
	private static final int ROOM_OFFSET_NORTH = 1;		// North-Rooms 1-50	
	private static final int ROOM_OFFSET_EAST = 101;		// East-Rooms 101-150
	private static final int ROOM_OFFSET_SOUTH = 201;	// South-Rooms 201-250	
	private static final int ROOM_OFFSET_WEST = 301;		// West-Rooms 301-350
	private static final int ROOM_OFFSET_SPAWN = 401;	// Spawn-Rooms 401-450
	
	private TiledMapTileLayer tmtlMainFloor;	// Floor layer of TiledMap
	private TiledMapTileLayer tmtlMainObj;		// Object layer of TiledMap
	private RoomCreator roomCreator;	// to create new Rooms for the Dungeon
	private TmxMapLoader loader;	// TiledMap Loader
	private Queue <RoomConnection> connections;	// Queue of Room connections (north,east,south,west)	 
	private Queue <Node<Integer>> enemySpawns;	// Queue of Nodes that holds the possible enemy spawn locations
	private int numberOfRooms; // amount of Rooms to be loaded
	
	/**
	 * DungeonCreator constructor.
	 * @param numberOfRooms | maximum Number of Rooms for the Level. 
	 * @param knight | {@link Knight} Model.
	 */
	public DungeonCreator(int numberOfRooms,Knight knight) {
		this.numberOfRooms = (numberOfRooms < MAX_AMOUNT_OF_ROOMS) ? numberOfRooms : MAX_AMOUNT_OF_ROOMS;
		connections = new Queue <>();
		enemySpawns = new Queue <>();
		loader = new TmxMapLoader();
		roomCreator = new RoomCreator(this,knight);
	}
	
	/**
	 * Creates a new Dungeon Level by connecting Different Room types.
	 * @return created {@link TiledMap}.
	 */
	public TiledMap createDungeon() {
		int randomRoomNumber = GameConstants.RANDOM.nextInt(NUMBER_OF_SPAWNABLE_ROOMS)+ROOM_OFFSET_SPAWN;
		TiledMap tmMain = loader.load(DUNGEON_ROOM+randomRoomNumber+DUNGEON_ROOM_FORMAT);
		tmtlMainFloor = (TiledMapTileLayer) tmMain.getLayers().get(TileLayers.FLOOR.get());
		tmtlMainObj = (TiledMapTileLayer) tmMain.getLayers().get(TileLayers.OBJECT.get());
		roomCreator.createSpawnRoom(tmtlMainFloor,tmtlMainObj);
		// add further rooms
		while(numberOfRooms > 0) {
			if(connections.size == 1) {
				// for End Cell
				break;
			}
			MapProperties cellProp = connections.first().getCell().getTile().getProperties();
			
			if(cellProp.containsKey(CellProperties.NORTH.get())) {
				// Room with south connection
				randomRoomNumber = GameConstants.RANDOM.nextInt(NUMBER_OF_SPAWNABLE_ROOMS)+ROOM_OFFSET_SOUTH; 
				loadRoom(randomRoomNumber,CellProperties.SOUTH);
			}
			else if(cellProp.containsKey(CellProperties.EAST.get())) {
				// Room with west connection
				randomRoomNumber = GameConstants.RANDOM.nextInt(NUMBER_OF_SPAWNABLE_ROOMS)+ROOM_OFFSET_WEST; 
				loadRoom(randomRoomNumber,CellProperties.WEST);
			}
			else if(cellProp.containsKey(CellProperties.SOUTH.get())) {
				// Room with north connection
				randomRoomNumber = GameConstants.RANDOM.nextInt(NUMBER_OF_SPAWNABLE_ROOMS)+ROOM_OFFSET_NORTH;
				loadRoom(randomRoomNumber,CellProperties.NORTH);
			}
			else if(cellProp.containsKey(CellProperties.WEST.get())) {
				// Room with east connection
				randomRoomNumber = GameConstants.RANDOM.nextInt(NUMBER_OF_SPAWNABLE_ROOMS)+ROOM_OFFSET_EAST;
				loadRoom(randomRoomNumber,CellProperties.EAST);
			}
			numberOfRooms--;
		}
		finishDungeon();
		return tmMain;
	}
	
	/**
	 * Finishes the Dungeon Level by setting the end Tile (Cell that teleport you to the next Level)
	 * and filling empty {@link Cell}s with Lava.
	 */
	private void finishDungeon() {
		tmtlMainFloor.setCell(connections.last().getxPos(), connections.last().getyPos(), roomCreator.getEnd());
		// fill empty Cells with Lava
		for(int x = 0; x < DUNGEON_SCALE; x++) {
			for(int y = 0; y < DUNGEON_SCALE; y++) {
				if(tmtlMainFloor.getCell(x, y) == null) {
					tmtlMainFloor.setCell(x, y, roomCreator.getLavaCell());
				} else if (tmtlMainFloor.getCell(x, y).getTile().getProperties().containsKey(CellProperties.CONNECTION.get())) {
					tmtlMainFloor.setCell(x, y, roomCreator.getBrickCell());
				}
			}
		}
	}
	
	/**
	 * Loads a new {@link TiledMap} and tries to connect it with the root {@link TiledMap} (root beginning is always a Spawn Room).
	 * @param roomNumber | number of the {@link TiledMap} file in {@value #DUNGEON_ROOM}.
	 * @param connection | type of connection.
	 */
	private void loadRoom (int roomNumber,CellProperties connection) {
		TiledMap tmTemp = loader.load(DUNGEON_ROOM+roomNumber+DUNGEON_ROOM_FORMAT);
		TiledMapTileLayer tmtlTempFloor = (TiledMapTileLayer) tmTemp.getLayers().get(TileLayers.FLOOR.get());
		TiledMapTileLayer tmtlTempObj = (TiledMapTileLayer) tmTemp.getLayers().get(TileLayers.OBJECT.get());
		roomCreator.createRoom(tmtlMainFloor,tmtlMainObj,tmtlTempFloor,tmtlTempObj,connection);
		connections.removeFirst();
	}
	
	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public Queue<RoomConnection> getConnections () {
		return connections;
	}

	public Queue<Node<Integer>> getEnemySpawns() {
		return enemySpawns;
	}
	
}
