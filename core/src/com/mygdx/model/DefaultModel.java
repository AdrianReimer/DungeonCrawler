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

package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.collision.Collision;

/**
 * Abstract Default Model class that every Model needs to inherit.
 * Holds {@link Sprite} information, health, speed, damage etc. 
 * of a Standard Model.
 * @author Adrian Reimer
 *
 */
public abstract class DefaultModel implements Disposable,SpriteMovementInterface{
	
	public static final int DEFAULT_ATTACK_DAMAGE = 35; // Standard attack damage 
	public static final int DEFAULT_HEALTH_VALUE = 100; // Standard health value
	public static final int DEFAULT_STAMINA_VALUE = 100; // Standard stamina value
	public static final float DEFAULT_MOVEMENT_SPEED = 2; // Standard movement Speed
	
	String name; // name of the Model
	Texture anims ; // animations
	int frame; // frame of an animation type
	int row; // type of animation
	Sprite sprite; // Visual data of the model
	TextureRegion[][] regions; // texture regions of the sprite
	Timer movementAnimationTimer = new Timer(); // handles movement of the model
	Timer attackTurn = new Timer(); // handles attack of the model
	int health = DEFAULT_HEALTH_VALUE; // default health of a model
	int attackDamage = DEFAULT_ATTACK_DAMAGE; // default damage of a model
	float movementSpeed = DEFAULT_MOVEMENT_SPEED; // default movement speed of a model
	float movementX; // default movementX of a model
	float movementY; // default movementY of a model
	Collision collision; // handles collision of the model
	
	/**
	 * handles the movement animation of the Model
	 */
	abstract void movementAnimation(); // default animation
	
	/**
	 * handles the attack of the Model
	 */
	abstract void attack(); // attack animation
	
	public String getName() {
		return name;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void setSprite (Sprite sprite) {
		this.sprite = sprite;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getAttackDamage() {
		return attackDamage;
	}
	
	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}
	
	public float getMovementX() {
		return movementX;
	}
	
	public float getMovementY() {
		return movementY;
	}
	
	public Collision getCollision() {
		return collision;
	}
	
	public void translateSprite() {
		sprite.translateY(movementY);
		sprite.translateX(movementX);
	}
	
	public void setMovementX(float movementX) {
		this.movementX = movementX;
	}

	public void setMovementY(float movementY) {
		this.movementY = movementY;
	}
	
	public void setMovementSpeed(float movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	public float getMovementSpeed() {
		return movementSpeed;
	}
	
	public void dispose() {
		movementAnimationTimer.clear();
		attackTurn.clear();
	}
	
}
