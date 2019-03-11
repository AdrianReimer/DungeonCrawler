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

package com.mygdx.item;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.Manager.SoundManager;
import com.mygdx.constant.GameConstants;
import com.mygdx.enums.CellProperties;
import com.mygdx.model.Knight;
import com.mygdx.screen.GameScreen;
import com.mygdx.stage.GameStage;
import com.mygdx.stage.ItemInterface;

/**
 * Collectible Potion Item.
 * @author Adrian Reimer
 *
 */
public class Potion extends DefaultItem {
	
	private static final int MAX_HEALTH_POTION_VALUE = 10;
	private static final int MAX_AGILITY_POTION_VALUE = 60;
	
	private SoundManager soundManager;
	private ItemInterface itemInterface;
	private Skin skin;
	private Color healthColor;
	private Color agilityColor;
	
	/**
	 * Potion constructor.
	 * @param soundManager | {@link SoundManager} that handles sounds.
	 * @param knightSprite | {@link Sprite} of the {@link Knight}.
	 * @param itemInterface | Interface between {@link GameStage} and other Items. 
	 */
	public Potion(SoundManager soundManager,Sprite knightSprite, final ItemInterface itemInterface){
		this.soundManager = soundManager;
		this.knightSprite = knightSprite;
		this.itemInterface = itemInterface;
		skin = GameScreen.SKIN;
		healthColor = Color.RED;
		agilityColor = Color.GREEN;
	}
	
	/**
	 * Checks if the {@link Knight} is on a {@link Cell} with Potion {@link CellProperties}.
	 * @param tm | current {@link TiledMap}.
	 */
	public void check(TiledMap tm) {
		checkCell(tm);
		if (cell == null) {
			return;
		}
		if (cell.getTile().getProperties().containsKey(CellProperties.HEALTH_POTION.get())) {
			healthPotion();
		} else if (cell.getTile().getProperties().containsKey(CellProperties.AGILITY_POTION.get())) {
			agilityPotion();
		}
	}

	/**
	 * handles the events when a health Potion was collected by the {@link Knight}.
	 */
	private void healthPotion() {
		soundManager.getSoundEffect().playBottle();
		int health = GameConstants.RANDOM.nextInt(MAX_HEALTH_POTION_VALUE)+1;
		Label label = new Label("+" + health,skin);
		label.setFontScale(FONT_SCALE);
		label.setColor(healthColor);
		itemInterface.addLabel(label);
		itemInterface.addHealth(health);
		destroyItem();
	}

	/**
	 * handles the events when a agility Potion was collected by the {@link Knight}.
	 */
	private void agilityPotion() {
		soundManager.getSoundEffect().playBottle();
		int agility = GameConstants.RANDOM.nextInt(MAX_AGILITY_POTION_VALUE)+1;
		Label label = new Label("+" + agility,skin);
		label.setFontScale(FONT_SCALE);
		label.setColor(agilityColor);
		itemInterface.addLabel(label);
		itemInterface.addHealth(agility);
		destroyItem();
	}

}
