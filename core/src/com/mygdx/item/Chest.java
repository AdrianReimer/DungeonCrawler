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
 * Collectible Chest Item.
 * @author Adrian Reimer
 *
 */
public class Chest extends DefaultItem {

	private static final int MAX_GOLD_CHEST1 = 10;
	private static final int MAX_GOLD_CHEST2 = 20;
	private static final int MAX_GOLD_CHEST3 = 40;
	private static final String GOLD_COLLECT = "gold";
	
	private SoundManager soundManager;
	private ItemInterface itemInterface;
	private Skin skin;
	private Color goldColor;

	/**
	 * Chest constructor.
	 * @param soundManager | {@link SoundManager} that handles sounds.
	 * @param knightSprite | {@link Sprite} of the {@link Knight}.
	 * @param itemInterface | Interface between {@link GameStage} and other Items. 
	 */
	public Chest(SoundManager soundManager,Sprite knightSprite, final ItemInterface itemInterface) {
		this.soundManager = soundManager;
		this.knightSprite = knightSprite;
		this.itemInterface = itemInterface;
		skin = GameScreen.SKIN;
		goldColor = Color.GOLDENROD;
	}

	/**
	 * Checks if the {@link Knight} is on a {@link Cell} with Chest {@link CellProperties}.
	 * @param tm | current {@link TiledMap}.
	 */
	public void check(TiledMap tm) {
		checkCell(tm);
		if (cell == null) {
			return;
		}
		if (cell.getTile().getProperties().containsKey(CellProperties.CHEST1.get())) {
			chest1();
		} else if (cell.getTile().getProperties().containsKey(CellProperties.CHEST2.get())) {
			chest2();
		} else if (cell.getTile().getProperties().containsKey(CellProperties.CHEST3.get())) {
			chest3();
		}
	}

	/**
	 * handles the events when a Chest1 was collected by the {@link Knight}.
	 */
	private void chest1() {
		soundManager.getSoundEffect().playGold();
		int gold = GameConstants.RANDOM.nextInt(MAX_GOLD_CHEST1)+1;
		Label label = new Label(POSITIVE_COLLECT + gold + GOLD_COLLECT,skin);
		label.setFontScale(FONT_SCALE);
		label.setColor(goldColor);
		itemInterface.addLabel(label);
		itemInterface.addGold(gold);
		destroyItem();
	}

	/**
	 * handles the events when a Chest2 was collected by the {@link Knight}.
	 */
	private void chest2() {
		soundManager.getSoundEffect().playGold();
		int gold = GameConstants.RANDOM.nextInt(MAX_GOLD_CHEST2)+1;
		Label label = new Label(POSITIVE_COLLECT + gold + GOLD_COLLECT,skin);
		label.setFontScale(FONT_SCALE);
		label.setColor(goldColor);
		itemInterface.addLabel(label);
		itemInterface.addGold(gold);
		destroyItem();
	}

	/**
	 * handles the events when a Chest3 was collected by the {@link Knight}.
	 */
	private void chest3() {
		soundManager.getSoundEffect().playGold();
		int gold = GameConstants.RANDOM.nextInt(MAX_GOLD_CHEST3)+1;
		Label label = new Label(POSITIVE_COLLECT + gold + GOLD_COLLECT,skin);
		label.setFontScale(FONT_SCALE);
		label.setColor(goldColor);
		itemInterface.addLabel(label);
		itemInterface.addGold(gold);
		destroyItem();
	}
}