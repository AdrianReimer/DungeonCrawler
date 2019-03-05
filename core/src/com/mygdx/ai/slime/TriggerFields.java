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
import com.mygdx.collision.Collision;
import com.mygdx.model.Knight;
import com.mygdx.model.Slime;

/**
 * TriggerFields that checks if the {@link Knight} is in a certain Field relative to the {@link Slime}.
 * @author Adrian Reimer
 *
 */
class TriggerFields {

	private Sprite playerSprite;
	private Sprite sprite;

	/**
	 * TriggerFields constructor.
	 * @param playerSprite | {@link Sprite} of the {@link Knight}.
	 * @param sprite | {@link Sprite} of the {@link Slime}.
	 */
	TriggerFields(Sprite playerSprite, Sprite sprite) {
		this.playerSprite = playerSprite;
		this.sprite = sprite;
	}

	/**
	 * Checks the Top Field relative to the {@link Slime}.
	 * @return true if the {@link Knight} is in that field, else false.
	 */
	boolean checkTop() {
		return (sprite.getX() + 2 * Collision.TILE_SCALE) >= playerSprite.getX()
				&& (sprite.getX() - Collision.TILE_SCALE) <= playerSprite.getX()
				&& (sprite.getY() + 6 * Collision.TILE_SCALE) >= playerSprite.getY()
				&& sprite.getY() <= playerSprite.getY();
	}
	
	/**
	 * Checks the Bottom Field relative to the {@link Slime}.
	 * @return true if the {@link Knight} is in that field, else false.
	 */
	boolean checkBottom() {
		return (sprite.getX() + 2 * Collision.TILE_SCALE) >= playerSprite.getX()
				&& (sprite.getX() - Collision.TILE_SCALE) <= playerSprite.getX()
				&& (sprite.getY() - 5 * Collision.TILE_SCALE) <= playerSprite.getY()
				&& sprite.getY() >= playerSprite.getY();
	}
	
	/**
	 * Checks the Right Field relative to the {@link Slime}.
	 * @return true if the {@link Knight} is in that field, else false.
	 */
	boolean checkRight() {
		return (sprite.getY() + 2 * Collision.TILE_SCALE) >= playerSprite.getY()
				&& (sprite.getY() - Collision.TILE_SCALE) <= playerSprite.getY()
				&& (sprite.getX() + 6 * Collision.TILE_SCALE) >= playerSprite.getX() 
				&& sprite.getX() < playerSprite.getX();
	}
	
	/**
	 * Checks the Left Field relative to the {@link Slime}.
	 * @return true if the {@link Knight} is in that field, else false.
	 */
	boolean checkLeft() {
		return (sprite.getY() + 2 * Collision.TILE_SCALE) >= playerSprite.getY()
				&& (sprite.getY() - Collision.TILE_SCALE) <= playerSprite.getY()
				&& (sprite.getX() - 5 * Collision.TILE_SCALE) <= playerSprite.getX()
				&& sprite.getX() > playerSprite.getX();
	}
	
	/**
	 * Checks the Right-Top Field relative to the {@link Slime}.
	 * @return true if the {@link Knight} is in that field, else false.
	 */
	boolean checkRightTop() {
		return (sprite.getX() + 6 * Collision.TILE_SCALE) > playerSprite.getX() 
				&& (sprite.getX() + 2 * Collision.TILE_SCALE) < playerSprite.getX()
				&& (sprite.getY() + 6 * Collision.TILE_SCALE) > playerSprite.getY() 
				&& (sprite.getY() + 2 * Collision.TILE_SCALE) < playerSprite.getY();
	}
	
	/**
	 * Checks the Right-Bottom Field relative to the {@link Slime}.
	 * @return true if the {@link Knight} is in that field, else false.
	 */
	boolean checkRightBottom() {
		return (sprite.getX() + 6 * Collision.TILE_SCALE) > playerSprite.getX() 
				&& (sprite.getX() + 2 * Collision.TILE_SCALE) < playerSprite.getX()
				&& (sprite.getY() - 5 * Collision.TILE_SCALE) < playerSprite.getY() 
				&& (sprite.getY() - Collision.TILE_SCALE) > playerSprite.getY();
	}
	
	/**
	 * Checks the Left-Top Field relative to the {@link Slime}.
	 * @return true if the {@link Knight} is in that field, else false.
	 */
	boolean checkLeftTop() {
		return (sprite.getX() - 5 * Collision.TILE_SCALE) < playerSprite.getX() 
				&& (sprite.getX() - Collision.TILE_SCALE) > playerSprite.getX()
				&& (sprite.getY() + 6 * Collision.TILE_SCALE) > playerSprite.getY() 
				&& (sprite.getY() + 2 * Collision.TILE_SCALE) < playerSprite.getY();
	}
	
	/**
	 * Checks the Left-Bottom Field relative to the {@link Slime}.
	 * @return true if the {@link Knight} is in that field, else false.
	 */
	boolean checkLeftBottom() {
		return (sprite.getX() - 5 * Collision.TILE_SCALE) < playerSprite.getX() 
				&& (sprite.getX() - Collision.TILE_SCALE) > playerSprite.getX()
				&& (sprite.getY() - 5 * Collision.TILE_SCALE) < playerSprite.getY() 
				&& (sprite.getY() - Collision.TILE_SCALE) > playerSprite.getY();
	}
}
