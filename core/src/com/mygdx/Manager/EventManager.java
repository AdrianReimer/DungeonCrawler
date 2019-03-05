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

package com.mygdx.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.ai.slime.Node;
import com.mygdx.collision.Collision;
import com.mygdx.constant.GameConstants;
import com.mygdx.dungeon.DungeonCreator;
import com.mygdx.enums.CellProperties;
import com.mygdx.enums.TileLayers;
import com.mygdx.item.Chest;
import com.mygdx.item.Potion;
import com.mygdx.model.DefaultModel;
import com.mygdx.model.Knight;
import com.mygdx.model.ModelInterface;
import com.mygdx.model.Slime;
import com.mygdx.stage.GameStage;
import com.mygdx.stage.ItemInterface;

/**
 * Event Manager that handles all events in the Level.
 * Events like playerCollision,Interactable Items,spawning monsters ...
 * @author Adrian Reimer
 *
 */
public class EventManager implements Disposable {
	
	private boolean isOnEndTile; // if player is able to go to the next dungeon
	private Potion potion;	// Potion data
	private Chest chest;	// Chest data
	private Collision collision;	// Collision requests
	private WorldManager worldManager; // manages the Tilemap
	private SpriteManager spriteManager;
	private Queue<DefaultModel> monsters;
	private ModelInterface modelInterface;
	
	/**
	 * EventManager constructor.
	 * @param soundManager | {@link SoundManager} that handles sounds.
	 * @param worldManager | {@link WorldManager} that renders the {@link TiledMap}.
	 * @param spriteManager | {@link SpriteManager} that draws the {@link Sprite}s.
	 * @param itemInterface | Interface between {@link GameStage} and other Items. 
	 * @param deathTable | {@link Table} that gets visible when the {@link Knight} dies.
	 */
	public EventManager(SoundManager soundManager,WorldManager worldManager,SpriteManager spriteManager, final ItemInterface itemInterface,final ModelInterface modelInterface, Table deathTable) {
		this.worldManager = worldManager;
		this.spriteManager = spriteManager;
		this.modelInterface = modelInterface;
		// Player movement Controller init + Player Creation
		spriteManager.setKnight(new Knight(soundManager,spriteManager.getModelList(),deathTable,modelInterface));
		// interactable Game Objects
		potion = new Potion(soundManager,spriteManager.getKnight().getSprite(),itemInterface);
		chest = new Chest(soundManager,spriteManager.getKnight().getSprite(),itemInterface);
		monsters = new Queue<>();
		isOnEndTile = true;
	}
	
	/**
	 * Updates all Events.
	 */
	public void checkEvents () {
		collision.update();
		isOnEndTile = checkIfOnEndCell();
		potion.check(worldManager.getTiledMap());
		chest.check(worldManager.getTiledMap());
	}
	
	/**
	 * Creates a new Dungeon Level.
	 * @param monsterSpawnChance | chance a Monster spawns in this Level.
	 * @param roomSpawnChance | max amount of Rooms that can spawn in this Level.
	 */
	public void createNewDungeonLevel (int monsterSpawnChance, int roomSpawnChance) {
		// dispose old level
		worldManager.dispose();
		// create new Dungeon
		DungeonCreator dungeon = new DungeonCreator(roomSpawnChance,spriteManager.getKnight());
		worldManager.setTiledMap(dungeon.createDungeon());
		worldManager.setTiledMapRenderer(new OrthogonalTiledMapRenderer(worldManager.getTiledMap()));
		collision = new Collision(spriteManager.getKnight(),worldManager.getTiledMap());
		// spawn Monsters dependent on the Difficulty
		spawnMonsters(worldManager.getTiledMap(),dungeon.getEnemySpawns(),monsterSpawnChance);
		// Teleport Player Character
		spriteManager.getKnight().teleportToSpawn();
		spriteManager.getKnight().setModelList(spriteManager.getModelList());
		isOnEndTile = false;
	}
	
	/**
	 * Checks if the {@link Knight} is on the {@link Cell} with End {@link CellProperties}.
	 * @return true if {@link Knight} is on a {@link Cell} with End {@link CellProperties}, else false.
	 */
	private boolean checkIfOnEndCell () {
		TiledMapTileLayer tmtlFloor = (TiledMapTileLayer) worldManager.getTiledMap().getLayers().get(TileLayers.FLOOR.get());
		Cell cell = tmtlFloor
				.getCell(
						(int) ((spriteManager.getKnight().getSprite().getX() + Collision.TILE_SCALE) / Collision.TILE_SCALE),
						(int) ((spriteManager.getKnight().getSprite().getY() + Collision.TILE_OFFSET) / Collision.TILE_SCALE));
		if (cell == null) {
			Gdx.app.log("checkIfOnEndCell", "Cell should not be Null!",new NullPointerException());
			return false;
		}
		return cell.getTile().getProperties().containsKey(CellProperties.END.get());
	}
	
	/**
	 * Spawns Monsters in the Level.
	 * @param tm | current {@link TiledMap}.
	 * @param enemySpawns | spawn locations for the monsters.
	 * @param monsterSpawnChance | spawn chance of the monsters.
	 */
	private void spawnMonsters(TiledMap tm, Queue<Node<Integer>> enemySpawns, int monsterSpawnChance) {
		for(Node<Integer> node : enemySpawns) {
			if(monsterSpawnChance == GameConstants.RANDOM.nextInt(monsterSpawnChance)+1) {
				monsters.addLast((new Slime (spriteManager,tm,node.getX(),node.getY(),modelInterface)));
			}
		}
	}
	
	/**
	 * Kills all Monsters in the Level.
	 */
	public void killMonsters() {
		if(monsters == null) {
			Gdx.app.log("killMonsters", "monsterStack should not be null!",new NullPointerException());
			return;
		}
		for(DefaultModel model : monsters) {
			model.dispose();
		}
		monsters.clear();
	}
	
	public void setCollision(Collision collision) {
		this.collision = collision;
	}

	public void setOnFinish(boolean isOnFinish) {
		this.isOnEndTile = isOnFinish;
	}
	
	public Queue<DefaultModel> getMonsters() {
		return monsters;
	}

	public boolean isOnEndTile() {
		return isOnEndTile;
	}

	@Override
	public void dispose() {
		monsters.clear();
	}
}
