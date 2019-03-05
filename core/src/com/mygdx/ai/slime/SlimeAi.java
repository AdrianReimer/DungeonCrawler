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
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.ai.slime.stateMachine.Action;
import com.mygdx.ai.slime.stateMachine.Request;
import com.mygdx.ai.slime.stateMachine.State;
import com.mygdx.ai.slime.stateMachine.StateMachine;
import com.mygdx.ai.slime.stateMachine.TransitionConfiguration;
import com.mygdx.model.Knight;
import com.mygdx.model.Slime;

/**
 * Simple AI with a related {@link StateMachine} that tries to follow the current {@link Knight} when in range.
 * @author Adrian Reimer
 */
public class SlimeAi implements Disposable{
	
	private static final int AREA_RANGE = 250;
	private static final int DAMAGE_RANGE = 25;
	private static final float MOVE_SLIME_DELAY = 0.02f;
	
	private int stateCounter = 1;
	private StateMachine stateMachine;
	private Sprite playerSprite;
	private Sprite sprite;
	private TriggerFields triggerFields;
	private Queue<Node<Float>> path;
	private Timer movement;
	private Timer attack;
	private Slime slime;
	private SlimeVision slimeVision;
	
	/**
	 * SlimeAi constructor.
	 * @param slime | Related {@link Slime} that uses this {@link SlimeAi}.   
	 * @param knight | Current {@link Knight} that contains the mainly needed player{@link Sprite}.
	 * @param tm | Current {@link TiledMap}.
	 * @param attack | {@link Timer} that holds an implemented {@link Task} that handles the attack of the {@link Slime}.
	 */
	public SlimeAi (Slime slime,Knight knight, TiledMap tm, Timer attack) {
		stateMachine = new StateMachine(TransitionConfiguration.DEFAULT, new Request("slime",State.IDLE));
		path = new Queue<>();
		this.movement = new Timer();
		this.slime = slime;
		this.sprite = slime.getSprite();
		this.playerSprite = knight.getSprite();
		this.slimeVision = new SlimeVision(path,sprite,playerSprite,tm, movement);
		triggerFields = new TriggerFields (playerSprite,sprite);
		this.attack = attack;
		moveSlime();
	}
	
	/**
	 * Updates the {@link StateMachine}. 
	 */
	public void checkAi () {
		switch (stateMachine.getRequest().getState()) {
		case IDLE:
			if(isInAreaRange()) {
				stateMachine.apply(Action.IN_AREA_REQUEST);
			}
			break;
		case LOOKING:
			if(isInAggroRange()) {
				stateMachine.apply(Action.IN_AGGRORANGE_REQUEST);
				stateCounter = 1;
			}else if(stateCounter == 0b10000000000000000000000000000000) {
				stateMachine.apply(Action.NOT_IN_AREA_REQUEST);
				stateCounter = 1;
			}
			stateCounter = stateCounter << 1;
			break;
		case HUNTING:
			if(!isHunting()) {
				stateMachine.apply(Action.NOT_IN_AGGRORANGE_REQUEST);
				movement.stop();
			}
			break;
		}
	}
	
	/**
	 * Movement{@link Task} that handles the Movement of the {@link Slime} by iterating over {@link Node}s. 
	 *  
	 */
	private void moveSlime() {
		movement.scheduleTask(new Task() {
			@Override
			public void run() {
				if(path.size > 0) {
					if(sprite.getX() != path.first().getX()) {
						moveX();
					}else if(sprite.getY() != path.first().getY()) {
						moveY();
					}
					if(isInDamageRange()) {
						attack.start();
						movement.stop();
					}else if(sprite.getX() == path.first().getX() && sprite.getY() == path.first().getY()) {
						slime.setMovementX(0);
						slime.setMovementY(0);
						path.removeFirst();
					}
					slime.getCollision().update();	
				}	
			}
		},0,MOVE_SLIME_DELAY);
	}
	
	/**
	 * Sets the X-Speed of the {@link Slime}.
	 */
	private void moveX () {
		slime.setMovementY(0);
		if(sprite.getX() < path.first().getX()) {
			slime.setMovementX(slime.getMovementSpeed());
		} else {
			slime.setMovementX(-slime.getMovementSpeed());
		}
	}
	
	/**
	 * Sets the Y-Speed of the {@link Slime}.
	 */
	private void moveY () {
		slime.setMovementX(0);
		if(sprite.getY() < path.first().getY()) {
			slime.setMovementY(slime.getMovementSpeed());
		} else {
			slime.setMovementY(-slime.getMovementSpeed());
		}
	}
	
	/**
	 * Calculates if {@link Knight} is in Area Range of {@link Slime}.
	 * The Area Range is a radius of {@value #AREA_RANGE} pixels around the {@link Slime}.
	 * @return true if {@link Knight} is in Area Range of {@link Slime}, else false.
	 */
	private boolean isInAreaRange() {
		return Math.abs((int) (playerSprite.getX() - sprite.getX())) < AREA_RANGE
			&& Math.abs((int) (playerSprite.getY() - sprite.getY())) < AREA_RANGE;
	}
	
	/**
	 * Calculates if {@link Knight} is in Damage Range of {@link Slime}.
	 * The Damage Range is a radius of {@value #DAMAGE_RANGE} pixels around the {@link Slime}.
	 * @return true if {@link Knight} is in Damage Range of {@link Slime}, else false.
	 */
	private boolean isInDamageRange() {
		return Math.abs((int) (playerSprite.getX() - sprite.getX())) < DAMAGE_RANGE
			&& Math.abs((int) (playerSprite.getY() - sprite.getY())) < DAMAGE_RANGE;
	}
	
	/**
	 * Calculates if {@link Knight} can be seen by {@link Slime}.
	 * @return true if {@link Knight} can be seen by {@link Slime}, else false.
	 */
	private boolean isInAggroRange() {
		boolean isInLineOfSight = false;
		// vertical
		if(triggerFields.checkTop()) {
			isInLineOfSight = slimeVision.verticalTop();
		} else if (triggerFields.checkBottom()) {
			isInLineOfSight = slimeVision.verticalBottom();
		}
		// horizontal
		if (triggerFields.checkRight()) {
			isInLineOfSight = slimeVision.horizontalRight();
		} else if (triggerFields.checkLeft()) {
			isInLineOfSight = slimeVision.horizontalLeft();
		}
		// diagonal
		if (triggerFields.checkRightTop()) {
			isInLineOfSight = slimeVision.diagonalRightTop();
		} else if (triggerFields.checkRightBottom()) {
			isInLineOfSight = slimeVision.diagonalRightBottom();
		} else if (triggerFields.checkLeftTop()) {
			isInLineOfSight = slimeVision.diagonalLeftTop();
		} else if (triggerFields.checkLeftBottom()) {
			isInLineOfSight = slimeVision.diagonalLeftBottom();
		}
		return isInLineOfSight;
	}
	
	/**
	 * Checks if {@link Slime} is still hunting the {@link Knight}.
	 * @return true if {@link Slime} is still hunting, else false.
	 */
	private boolean isHunting () {
		if(path.size == 0) {
			return isInAggroRange();
		}
		return true;
	}

	/**
	 * Movement {@link Timer} getter.
	 * @return movement {@link Timer}.
	 */
	public Timer getMovement() {
		return movement;
	}

	/**
	 * path getter.
	 * @return path that holds the {@link Node}s to get to the {@link Knight}. 
	 */
	public Queue<Node<Float>> getPath() {
		return path;
	}

	@Override
	public void dispose() {
		movement.stop();
	}
	
}
