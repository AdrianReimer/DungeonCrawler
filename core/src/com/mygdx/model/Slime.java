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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.Manager.SpriteManager;
import com.mygdx.ai.slime.SlimeAi;
import com.mygdx.ai.slime.stateMachine.StateMachine;
import com.mygdx.collision.Collision;
import com.mygdx.enums.Models;

/**
 * Slime Model.
 * @author Adrian Reimer
 *
 */
public class Slime extends DefaultModel {
	
	private static final int MAX_MOVEMENT_FRAME = 4;
	private static final float AI_CHECK_DELAY = 0.1f;
	private static final float MOVE_SLIME_DELAY = 0.02f;
	private static final float MOVEMENT_ANIMATION_DELAY = 0.1f;
	
	private int counter;
	private List<DefaultModel> modelList;
	private Sprite playerSprite;
	private SlimeAi slimeAi;
	private Timer aiUpdateTimer;
	private Knight knight;

	/**
	 * Slime constructor.
	 * @param spriteManager | {@link SpriteManager}.
	 * @param tm | current {@link TiledMap}.
	 * @param xPos | x-position of the {@link Slime}.
	 * @param yPos | y-position of the {@link Slime}.
	 */
	public Slime(SpriteManager spriteManager, TiledMap tm, float xPos, float yPos, final ModelInterface modelInterface) {
		// name this model the same as the Class
		name = this.getClass().toString();
		aiUpdateTimer = new Timer();
		this.knight = spriteManager.getKnight();
		// create collision object for this model
		collision = new Collision(this,tm);
		modelList = spriteManager.getModelList();
		playerSprite = spriteManager.getKnight().getSprite();
		anims = modelInterface.getModelTexture(Models.SLIME);
		regions = TextureRegion.split(anims, 64, 64);
		sprite = new Sprite(regions[row][frame]);
		sprite.setScale(0.7f, 0.7f);
		sprite.setPosition(xPos,yPos);
		// create AI
		slimeAi = new SlimeAi(this,spriteManager.getKnight(),tm,attackTurn);
		// add model to List
		spriteManager.addModel(this);
		// start Timer Methods
		movementAnimation();
		attack();
		attackTurn.stop();
		updateAi();
	}

	@Override
	public void movementAnimation() {
		movementAnimationTimer.scheduleTask(new Task() {
			@Override
			public void run() {
				frame++;
				if (frame > MAX_MOVEMENT_FRAME)
					frame = 0;
				sprite.setRegion(regions[row][frame]);
			}
		}, 0, MOVEMENT_ANIMATION_DELAY);
	}
	
	/**
	 * Updates the {@link StateMachine} of the {@link Slime} every {@value #AI_CHECK_DELAY}sec.
	 */
	public void updateAi () {
		aiUpdateTimer.scheduleTask(new Task() {
			@Override
			public void run() {
				slimeAi.checkAi();
			}
		}, 0,AI_CHECK_DELAY);
	}
	
	@Override
	public void attack() {
		attackTurn.scheduleTask(new Task() {
			@Override
			public void run() {
				// 2x moves in one Tick
				collision.update();
				collision.update();
				switch(counter) {
				case 1:
					// slime bounces away from knight
					movementX = -movementX;
					movementY = -movementY;
					// set color of knight to red --> hurt
					playerSprite.setColor(Color.RED);
					// set health of knight
					knight.setHealth(knight.getHealth()-attackDamage);
					break;
				case 10:
					// set color of knight to normal
					playerSprite.setColor(Color.WHITE);
					break;
				case 32:
					// stop bouncing away
					movementX = 0;
					movementY = 0;
					counter = 0;
					attackTurn.stop();
					slimeAi.getPath().clear();
					slimeAi.getMovement().start();
					break;
				default: break;
				}
				counter++;
			}
		},0,MOVE_SLIME_DELAY);
	}

	@Override
	public void dispose() {
		super.dispose();
		// turn model invisible
		sprite.setAlpha(0);
		aiUpdateTimer.stop();
		slimeAi.dispose();
		try {
			// set color of knight to normal
			playerSprite.setColor(Color.WHITE);
			modelList.remove(this);
		}catch (NullPointerException e) {
			Gdx.app.log("RemoveSlime", "Error removing Slime from modelList",e);
		}
	}
	
}
