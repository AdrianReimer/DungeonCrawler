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

package com.mygdx.ai.slime;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.collision.Collision;
import com.mygdx.enums.CellProperties;
import com.mygdx.enums.TileLayers;
import com.mygdx.model.Knight;
import com.mygdx.model.Slime;

/**
 * Vision class of the {@link Slime} that checks if the {@link Knight} can be seen by the {@link Slime}.
 * This is implemented by checking the {@link Cell}s of the current {@link TiledMap} in a specific pattern (horizontal/vertical/diagonal).
 * @author Adrian Reimer
 *
 */
class SlimeVision {
	
	private Queue<Node<Float>> path;
	private Sprite sprite;
	private Sprite playerSprite;
	private TiledMapTileLayer tmtlFloor;
	private TiledMapTileLayer tmtlObj;
	private Timer movement;
	
	/**
	 * SlimeVision constructor.
	 * @param path | path that holds the {@link Node}s to get from the {@link Slime} to the {@link Knight}. 
	 * @param sprite | {@link Sprite} of the {@link Slime}.
	 * @param playerSprite | {@link Sprite} of the {@link Knight}.
	 * @param tm | current {@link TiledMap}.
	 * @param movement | {@link Timer} that holds an implemented {@link Task} that handles the movement of the {@link Slime}.
	 */
	SlimeVision (Queue<Node<Float>> path, Sprite sprite, Sprite playerSprite, TiledMap tm, Timer movement) {
		this.path = path;
		this.sprite = sprite;
		this.playerSprite = playerSprite;
		this.tmtlFloor = (TiledMapTileLayer) tm.getLayers().get(TileLayers.FLOOR.get());
		this.tmtlObj = (TiledMapTileLayer) tm.getLayers().get(TileLayers.OBJECT.get());
		this.movement = movement;
	}
	
	/**
	 * Checks the {@link Cell} of the {@link TiledMap}.
	 * @param x | x-position of the {@link Cell}.
	 * @param y | y-position of the {@link Cell}.
	 * @return 1 if {@link Cell} has collision, 2 if {@link Cell} is just a normal cell, 3 if {@link Cell} is where also the {@link Knight} is on.
	 */
	private int checkCell(int x, int y) {
		int cellx = (int)(sprite.getX()+Collision.TILE_SCALE)/Collision.TILE_SCALE+x;
		int celly = (int) ((sprite.getY()+Collision.TILE_SCALE)/Collision.TILE_SCALE)+y;
		Cell playerLocation = tmtlFloor.getCell((int) ((playerSprite.getX()+Collision.TILE_SCALE)/Collision.TILE_SCALE), (int) ((playerSprite.getY()+Collision.TILE_OFFSET)/Collision.TILE_SCALE));
		Cell cellFloor = tmtlFloor.getCell(cellx,celly);
		Cell cellObj = tmtlObj.getCell(cellx,celly);
		path.addLast(new Node<>((float)(cellx * Collision.TILE_SCALE)-16,(float) (celly*Collision.TILE_SCALE)-16));
		if(cellObj != null && cellObj.getTile().getProperties().containsKey(CellProperties.COLLISION.get())) {	
			path.clear();
			return 1;
		}
		if(cellFloor == null || cellFloor.getTile().getProperties().containsKey(CellProperties.COLLISION.get())) {	
			path.clear();
			return 1;
		}else if(playerLocation.equals(cellFloor)) {
			movement.start();
			return 3;
		}
		return 2;
	}
	
	/**
	 * Checks the vertical-Top {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	boolean verticalTop() {
		for(int x = -1; x < 2; x++) {
			for(int y = 0; y < 6; y++ ) {
				if(checkCell(x,y) == 1) {
					break;
				} else if(checkCell(x,y) == 3) {
					return true;
				}
			}
			path.clear();
		}
		return false;
	}
	
	/**
	 * Checks the vertical-Bottom {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	boolean verticalBottom() {
		for(int x = -1; x < 2; x++) {
			for(int y = 0; y > -6; y-- ) {
				if(checkCell(x,y) == 1) {
					break;
				} else if(checkCell(x,y) == 3) {
					return true;
				}
			}
			path.clear();
		}
		return false;
	}
	
	/**
	 * Checks the horizontal-Left {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	boolean horizontalLeft() {
		for(int y = 1; y > -2; y--) {
			for(int x = 0; x > -6; x-- ) {
				if(checkCell(x,y) == 1) {
					break;
				} else if(checkCell(x,y) == 3) {
					return true;
				}
			}
			path.clear();
		}
		return false;
	}
	
	/**
	 * Checks the horizontal-Right {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	boolean horizontalRight() {
		for(int y = 1; y > -2; y--) {
			for(int x = 0; x < 6; x++ ) {
				if(checkCell(x,y) == 1) {
					break;
				} else if(checkCell(x,y) == 3) {
					return true;
				}
			}
			path.clear();
		}
		return false;
	}
	
	/**
	 * Checks all the diagonal-Left-Bottom {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	boolean diagonalLeftBottom() {	
		return diagonalLeftBottom1() || diagonalLeftBottom2() || diagonalLeftBottom3() || diagonalLeftBottom4();
	}
	
	/**
	 * Checks the first diagonal-Left-Bottom {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalLeftBottom1() {
		if(checkCell(-1,0) == 1) {
			return false;
		} else if(checkCell(-1,0) == 3) {
			return true;
		}
		int x = -1;
		for(int y = -1; y > -6; y--) {
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
			x--;
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}
	
	/**
	 * Checks the second diagonal-Left-Bottom {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalLeftBottom2() {
		if(checkCell(-1,0) == 1) {
			return false;
		} else if(checkCell(-1,0) == 3) {
			return true;
		}
		int y = -1;
		for(int x1 = -1; x1 > -6; x1--) {
			if(checkCell(x1,y) == 1) {
				return false;
			} else if(checkCell(x1,y) == 3) {
				return true;
			}
			y--;
			if(checkCell(x1,y) == 1) {
				return false;
			} else if(checkCell(x1,y) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}
	
	/**
	 * Checks the third diagonal-Left-Bottom {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalLeftBottom3() {
		if(checkCell(0,-1) == 1) {
			return false;
		} else if(checkCell(0,-1) == 3) {
			return true;
		}
		int x1 = -1;
		for(int y1 = -1; y1 > -6; y1--) {
			if(checkCell(x1,y1) == 1) {
				return false;
			} else if(checkCell(x1,y1) == 3) {
				return true;
			}
			x1--;
			if(checkCell(x1,y1) == 1) {
				return false;
			} else if(checkCell(x1,y1) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}
	
	/**
	 * Checks the fourth diagonal-Left-Bottom {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalLeftBottom4() {
		if(checkCell(0,-1) == 1) {
			return false;
		} else if(checkCell(0,-1) == 3) {
			return true;
		}
		int y1 = -1;
		for(int x11 = -1; x11 > -6; x11--) {
			if(checkCell(x11,y1) == 1) {
				return false;
			} else if(checkCell(x11,y1) == 3) {
				return true;
			}
			y1--;
			if(checkCell(x11,y1) == 1) {
				return false;
			} else if(checkCell(x11,y1) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}
	
	/**
	 * Checks all the diagonal-Right-Top {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	boolean diagonalRightTop() {
		return diagonalRightTop1() || diagonalRightTop2() || diagonalRightTop3() || diagonalRightTop4();
	}
	
	/**
	 * Checks the first diagonal-Right-Top {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalRightTop1() {
		if(checkCell(0,1) == 1) {
			return false;
		} else if(checkCell(0,1) == 3) {
			return true;
		}
		int x = 1;
		for(int y = 1; y < 6; y++) {
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
			x++;
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}
	
	/**
	 * Checks the second diagonal-Right-Top {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalRightTop2() {
		if(checkCell(0,1) == 1) {
			return false;
		} else if(checkCell(0,1) == 3) {
			return true;
		}
		int y = 1;
		for(int x = 1; x < 6; x++) {
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
			y++;
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}
	
	/**
	 * Checks the third diagonal-Right-Top {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalRightTop3() {
		if(checkCell(1,0) == 1) {
			return false;
		} else if(checkCell(1,0) == 3) {
			return true;
		}
		int x = 1;
		for(int y = 1; y < 6; y++) {
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
			x++;
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
		}
		path.clear();
		return false;	
	}
	
	/**
	 * Checks the fourth diagonal-Right-Top {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalRightTop4() {
		if(checkCell(1,0) == 1) {
			return false;
		} else if(checkCell(1,0) == 3) {
			return true;
		}
		int y = 1;
		for(int x = 1; x < 6; x++) {
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
			y++;
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}
	
	/**
	 * Checks all the diagonal-Left-Top {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	boolean diagonalLeftTop() {
		return diagonalLeftTop1() || diagonalLeftTop2() || diagonalLeftTop3() || diagonalLeftTop4();
	}
	
	/**
	 * Checks the first diagonal-Left-Top {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalLeftTop1() {
		if(checkCell(-1,0) == 1) {
			return false;
		} else if(checkCell(-1,0) == 3) {
			return true;
		}
		int x = -1;
		for(int y = 1; y < 6; y++) {
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
			x--;
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}
	
	/**
	 * Checks the second diagonal-Left-Top {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalLeftTop2() {
		if(checkCell(-1,0) == 1) {
			return false;
		} else if(checkCell(-1,0) == 3) {
			return true;
		}
		int y = 1;
		for(int x = -1; x > -6; x--) {
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
			y++;
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}
	
	/**
	 * Checks the third diagonal-Left-Top {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalLeftTop3() {
		if(checkCell(0,1) == 1) {
			return false;
		} else if(checkCell(0,1) == 3) {
			return true;
		}
		int x = -1;
		for(int y = 1; y < 6; y++) {
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
			x--;
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}
	
	/**
	 * Checks the fourth diagonal-Left-Top {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalLeftTop4() {
		if(checkCell(0,1) == 1) {
			return false;
		} else if(checkCell(0,1) == 3) {
			return true;
		}
		int y = 1;
		for(int x = -1; x > -6; x--) {
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
			y++;
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}
	
	/**
	 * Checks all the diagonal-Right-Bottom {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	boolean diagonalRightBottom() {
		return diagonalRightBottom1() || diagonalRightBottom2() || diagonalRightBottom3() || diagonalRightBottom4();
	}

	/**
	 * Checks the first diagonal-Right-Bottom {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalRightBottom1() {
		if(checkCell(1,0) == 1) {
			return false;
		} else if(checkCell(1,0) == 3) {
			return true;
		}
		int x = 1;
		for(int y = -1; y > -6; y--) {
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
			x++;
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}
	
	/**
	 * Checks the second diagonal-Right-Bottom {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalRightBottom2() {
		if(checkCell(1,0) == 1) {
			return false;
		} else if(checkCell(1,0) == 3) {
			return true;
		}
		int y = -1;
		for(int x = 1; x < 6; x++) {
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
			y--;
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}
	
	/**
	 * Checks the third diagonal-Right-Bottom {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalRightBottom3() {
		if(checkCell(0,-1) == 1) {
			return false;
		} else if(checkCell(0,-1) == 3) {
			return true;
		}
		int x = 1;
		for(int y = -1; y > -6; y--) {
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
			x++;
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}
	
	/**
	 * Checks the fourth diagonal-Right-Bottom {@link Cell}s relative to the {@link Slime}.
	 * @return true if there was a viable path to the {@link Knight}, else false.
	 */
	private boolean diagonalRightBottom4() {
		if(checkCell(0,-1) == 1) {
			return false;
		} else if(checkCell(0,-1) == 3) {
			return true;
		}
		int y = -1;
		for(int x = 1; x < 6; x++) {
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
			y--;
			if(checkCell(x,y) == 1) {
				return false;
			} else if(checkCell(x,y) == 3) {
				return true;
			}
		}
		path.clear();
		return false;
	}

}
