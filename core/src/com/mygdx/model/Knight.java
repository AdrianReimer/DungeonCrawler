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

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.Manager.SoundManager;
import com.mygdx.collision.Collision;
import com.mygdx.enums.Models;

/**
 * Knight Model.
 * @author Adrian Reimer
 *
 */
public class Knight extends DefaultModel {
	
	private static final int MAX_MOVEMENT_FRAME = 5; // Defined by Knight SpriteSheet
	private static final int MAX_ATTACK_FRAME = 2; // --
	private static final int DOWN_MOVEMENT_ANIMATION_ROW = 0; // --
	private static final int TOP_MOVEMENT_ANIMATION_ROW = 1; // --
	private static final int LEFT_MOVEMENT_ANIMATION_ROW = 2; // --
	private static final int RIGHT_MOVEMENT_ANIMATION_ROW = 3; // --
	private static final int ATTACK_DOWN_ANIMATION_ROW = 4; // --
	private static final int ATTACK_TOP_ANIMATION_ROW = 5; // --
	private static final int ATTACK_LEFT_ANIMATION_ROW = 6; // --
	private static final int ATTACK_RIGHT_ANIMATION_ROW = 7; // --
	private static final int DEATH_ANIMATION_ROW = 8; // -- 
	private static final float ATTACK_DELAY = 0.02f;
	private static final float MOVEMENT_ANIMATION_DELAY = 0.1f;
	private static final float ATTACK_ANIMATION_DELAY = 0.15f;
	private static final float DEATH_ANIMATION_DELAY = 0.2f;
	private static final float STAMINA_REGENERATION_DELAY = 1.5f;
	
	private boolean isAttacking;
	private boolean isDead;
	private int attackStaminaCost = 20; // stamina cost for attacks
	private int staminaRegeneration = 1; // stamina regeneration
	private int stamina = 100; // starting stamina
	private int maxHealth = 100; // starting maxHealth
	private int maxStamina = 100; // starting maxStamina
	private SoundManager soundManager;
	private List<DefaultModel> modelList;
	private Timer attackAnimationTimer;
	private Timer deathAnimationTimer;
	private Timer staminaRegenerationTimer;
	private int spawnX;
	private int spawnY;
	private Queue<DefaultModel> targets;
	private Table deathTable; // visible when knight is dead

	/**
	 * Knight constructor.
	 * @param soundManager | {@link SoundManager} that handles sounds.
	 * @param modelList | {@link List} of {@link DefaultModel} in the {@link TiledMap}.
	 * @param deathTable | {@link Table} that gets visible when the {@link Knight} dies.
	 */
	public Knight(SoundManager soundManager,List<DefaultModel> modelList,Table deathTable,final ModelInterface modelInterface) {
		this.soundManager = soundManager;
		this.modelList = modelList;
		this.deathTable = deathTable;
		// name this model the same as the Class
		name = this.getClass().toString();
		attackAnimationTimer = new Timer();
		deathAnimationTimer = new Timer();
		staminaRegenerationTimer = new Timer();
		targets = new Queue<>();
		anims = modelInterface.getModelTexture(Models.KNIGHT);
		regions = TextureRegion.split(anims, 64, 64);
		sprite = new Sprite(regions[row][frame]);
		movementAnimationTimer = new Timer();
		// start Timer Methods
		movementAnimation();
		attack();
		death();
		staminaRegeneration();
		// add model to List
		modelList.add(this);
	}

	@Override
	public void movementAnimation() {
		movementAnimationTimer.scheduleTask(new Task() {
			@Override
			public void run() {
				movementAnimationToSpritesheetRow();
				if (frame > MAX_MOVEMENT_FRAME) {
					frame = 0;
				}
				sprite.setRegion(regions[row][frame]);
				frame++;
			}
		}, 0,MOVEMENT_ANIMATION_DELAY);
	}
	
	/**
	 * sets the animation of the {@link Sprite} depending on the movement.
	 */
	private void movementAnimationToSpritesheetRow () {
		if (movementX < 0)
			row = LEFT_MOVEMENT_ANIMATION_ROW;
		else if (movementX > 0)
			row = RIGHT_MOVEMENT_ANIMATION_ROW;
		else if (movementY < 0)
			row = DOWN_MOVEMENT_ANIMATION_ROW;
		else if (movementY > 0)
			row = TOP_MOVEMENT_ANIMATION_ROW;
		else {
			frame = 1;
			sprite.setRegion(regions[row][frame]);
		}
	}

	/**
	 * handles the attack Animation.
	 */
	public void attackAnimation() {
		// can´t attack again while attacking
		isAttacking = true;
		// stamina cost for attack
		setStamina(stamina - attackStaminaCost);
		// stop movement Animation
		movementAnimationTimer.stop();
		soundManager.getSoundEffect().playSwordSwing();
		frame = 0;
		isHittingEnemy();
		attackAnimationTimer.scheduleTask(new Task() {
			@Override
			public void run() {
				movementAnimationWhileAttackAnimation();
				if (frame > MAX_ATTACK_FRAME) {
					frame = 0;
					row -= ATTACK_DOWN_ANIMATION_ROW;
					movementAnimationTimer.start();
					attackAnimationTimer.clear();
					isAttacking = false;
				}
				sprite.setRegion(regions[row][frame]);
				frame++;
			}
		}, 0,ATTACK_ANIMATION_DELAY);
	}
	
	/**
	 * if model is moving while attacking then set it back to movement animation.
	 */
	private void movementAnimationWhileAttackAnimation () {
		if (movementX < 0)
			row = LEFT_MOVEMENT_ANIMATION_ROW;
		else if (movementX > 0)
			row = RIGHT_MOVEMENT_ANIMATION_ROW;
		else if (movementY < 0)
			row = DOWN_MOVEMENT_ANIMATION_ROW;
		else if (movementY > 0)
			row = TOP_MOVEMENT_ANIMATION_ROW;
		if (row < ATTACK_DOWN_ANIMATION_ROW)
			row += ATTACK_DOWN_ANIMATION_ROW;
	}
	
	/**
	 * handles the death event of the model.
	 */
	private void death() {
		deathAnimationTimer.scheduleTask(new Task() {
			@Override
			public void run() {
				if(health <= 0) {
					if (frame >= MAX_MOVEMENT_FRAME) {
						row = DOWN_MOVEMENT_ANIMATION_ROW;
						// enable death "window"
						deathTable.setVisible(true);
						// enable Animation timers for next NewGame
						movementAnimationTimer.start();
						attackAnimationTimer.start();
						// stop death Animation
						deathAnimationTimer.stop();
					}else {
						sprite.setRegion(regions[row][frame]);
						frame++;
					}
				}
			}
		},0,DEATH_ANIMATION_DELAY);
	}
	
	@Override
	public void attack() {
		attackTurn.scheduleTask(new Task() {
			@Override
			public void run() {
				if(targets != null) {
					if(targets.size > 0) {
						targets.removeFirst().dispose();
					} else {
						sprite.setColor(Color.WHITE);
						attackTurn.stop();
					}
				}
			}
		},0,ATTACK_DELAY);
	}

	/**
	 * checks if the {@link Knight} is hitting any enemy with its attack.
	 */
	private void isHittingEnemy() {
		if(modelList == null) Gdx.app.log("isHittingDemon", "modelList should not be null",new NullPointerException());
		else if(modelList.isEmpty()) Gdx.app.log("isHittingDemon", "no models in this Level",new IndexOutOfBoundsException());
		// check enemies
		for (DefaultModel model : modelList) {
			if(model == null || model.getClass().equals(Knight.class)) {
				continue;
			}
			switch (row) {
			case DOWN_MOVEMENT_ANIMATION_ROW:
			case ATTACK_DOWN_ANIMATION_ROW:
				isHittingBottom(model);
				break;
			case TOP_MOVEMENT_ANIMATION_ROW:
			case ATTACK_TOP_ANIMATION_ROW:
				isHittingTop(model);
				break;
			case LEFT_MOVEMENT_ANIMATION_ROW:
			case ATTACK_LEFT_ANIMATION_ROW:
				isHittingLeft(model);
				break;
			case RIGHT_MOVEMENT_ANIMATION_ROW:
			case ATTACK_RIGHT_ANIMATION_ROW:
				isHittingRight(model);
				break;
			default: break;
			}
		}
		attackTurn.start();
	}
	
	/**
	 * Checks if the {@link Knight} is hitting the enemy right of him.
	 * @param model | enemy model
	 * @return true if {@link Knight} is hitting the enemy, else false.
	 */
	private boolean isHittingRight(DefaultModel model) {
		if(sprite.getX() - model.getSprite().getX() < 0  && sprite.getX() - model.getSprite().getX() > -2*Collision.TILE_SCALE
		&& sprite.getY() - model.getSprite().getY() < Collision.TILE_SCALE && sprite.getY() - model.getSprite().getY() > -Collision.TILE_SCALE) {
			targets.addLast(model);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the {@link Knight} is hitting the enemy left of him.
	 * @param model | enemy model
	 * @return true if {@link Knight} is hitting the enemy, else false.
	 */
	private boolean isHittingLeft(DefaultModel model) {
		if(sprite.getX() - model.getSprite().getX() > 0  && sprite.getX() - model.getSprite().getX() < 2*Collision.TILE_SCALE
		&& sprite.getY() - model.getSprite().getY() < Collision.TILE_SCALE && sprite.getY() - model.getSprite().getY() > -Collision.TILE_SCALE) {
			targets.addLast(model);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the {@link Knight} is hitting the enemy above of him.
	 * @param model | enemy model
	 * @return true if {@link Knight} is hitting the enemy, else false.
	 */
	private boolean isHittingTop(DefaultModel model) {
		if(sprite.getY() - model.getSprite().getY() < 0  && sprite.getY() - model.getSprite().getY() > -2*Collision.TILE_SCALE
		&& sprite.getX() - model.getSprite().getX() < Collision.TILE_SCALE && sprite.getX() - model.getSprite().getX() > -Collision.TILE_SCALE) {
			targets.addLast(model);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the {@link Knight} is hitting the enemy below of him.
	 * @param model | enemy model
	 * @return true if {@link Knight} is hitting the enemy, else false.
	 */
	private boolean isHittingBottom(DefaultModel model) {
		if(sprite.getY() - model.getSprite().getY() > 0  && sprite.getY() - model.getSprite().getY() < 2*Collision.TILE_SCALE
			&& sprite.getX() - model.getSprite().getX() < Collision.TILE_SCALE && sprite.getX() - model.getSprite().getX() > -Collision.TILE_SCALE) {
			targets.addLast(model);
			return true;
		}
		return false;
	}
	
	/**
	 * handles the Stamina Regeneration.
	 */
	private void staminaRegeneration () {
		staminaRegenerationTimer.scheduleTask(new Task() {
			@Override
			public void run() {
				setStamina(stamina+staminaRegeneration);
			}
		}, 0,STAMINA_REGENERATION_DELAY);
	}

	@Override
	public void dispose() {
		super.dispose();
		attackAnimationTimer.clear();
		deathAnimationTimer.clear();
		targets.clear();
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	@Override
	public void setHealth(int health) {
		if(health > maxHealth)
			this.health = maxHealth;
		else if (health <= 0) {
			this.health = 0;
			if(!isDead) {
				// stop animations
				movementAnimationTimer.stop();
				attackAnimationTimer.stop();
				// disable movement
				movementX = 0;
				movementY = 0;
				movementSpeed = 0;
				// switch to deathAnimation SpriteSheet row
				row = DEATH_ANIMATION_ROW;
				frame = 0;
				deathAnimationTimer.start();
				isDead = true;
			}
		}
		else 
			this.health = health;
	}

	public void setStamina(int stamina) {
		if(stamina > maxStamina)
			this.stamina = maxStamina;
		else if (stamina < 0)
			this.stamina = 0;
		else
			this.stamina = stamina;
	}

	public void setMaxStamina(int maxStamina) {
		this.maxStamina = maxStamina;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getStamina() {
		return stamina;
	}

	public int getMaxStamina() {
		return maxStamina;
	}

	public void setSpawnX(int spawnX) {
		this.spawnX = spawnX;
	}

	public void setSpawnY(int spawnY) {
		this.spawnY = spawnY;
	}
	
	public void teleportToSpawn() {
		sprite.setPosition(spawnX, spawnY);
	}

	public void setModelList(List<DefaultModel> modelList) {
		this.modelList = modelList;
	}

	public boolean isAttacking() {
		return isAttacking;
	}

	public int getAttackStaminaCost() {
		return attackStaminaCost;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

}
