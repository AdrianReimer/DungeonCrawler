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

package com.mygdx.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.constant.GameConstants;
import com.mygdx.enums.GameTexts;
import com.mygdx.screen.GameScreen;

/**
 * Loading Stage (when loading next DungeonLevel)
 * @author Adrian Reimer
 *
 */
public class LoadingStage extends Stage {
	
	private static final float DELAY = 1f;
	
	private boolean visible = false;
	private boolean readyLoading;
	private Timer timer;
	private Label label;
	private StageSwitchInterface stageSwitchInterface;
	private GameStage gameStage;
    private int monsterSpawnChance;
    private int roomSpawnChance;
	
    /**
     * LoadingStage constructor.
     * @param stageSwitchInterface | {@link StageSwitchInterface}.
     * @param gameStage | {@link GameStage}.
     */
    public LoadingStage(final StageSwitchInterface stageSwitchInterface, GameStage gameStage) {
    	this.stageSwitchInterface = stageSwitchInterface;
    	this.gameStage = gameStage;
		Skin skin = GameScreen.SKIN;
		timer = new Timer();
		// create root Table 
		Table rootTable = new Table(skin);
		rootTable.background(GameConstants.TABLE_BACKGROUND);
		rootTable.setFillParent(true);
		rootTable.align(Align.bottomRight);
		// create label
		label = new Label(GameTexts.LOADING_STAGE_LABEL.get(),skin);
		rootTable.add(label);
		// add Actors to Stage
		addActor(rootTable);
		// start Timer Methods
		loadDungeon();
    }
    
    /**
     * loades the new Dungeon Level.
     */
    private void loadDungeon() {
		timer.scheduleTask(new Task() {
			@Override
			public void run() {
				if(visible) {
					gameStage.getEventManager().createNewDungeonLevel(monsterSpawnChance, roomSpawnChance);
					readyLoading = true;
				}
				timer.stop();
			}
		},0,DELAY);
    }
    

    @Override
    public void draw() {
        if (visible) {
            act(Gdx.graphics.getDeltaTime());
            super.draw();
            if(readyLoading) {
            	readyLoading = false;
            	visible = false;
            	stageSwitchInterface.switchToGameStage(false);
            }
        }
    }
    
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setMonsterSpawnChance(int monsterSpawnChance) {
		this.monsterSpawnChance = monsterSpawnChance;
	}

	public void setRoomSpawnChance(int roomSpawnChance) {
		this.roomSpawnChance = roomSpawnChance;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		timer.clear();
	}
	
}
