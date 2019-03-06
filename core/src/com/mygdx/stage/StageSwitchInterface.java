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

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.Manager.SpriteManager;
import com.mygdx.Manager.WorldManager;

/**
 * Handles the {@link Stage} switches.
 * Only one {@link Stage} should be visible.
 * @author Adrian Reimer
 *
 */
public interface StageSwitchInterface {
    void switchToGameStage(boolean makeNewLevel);
    void switchToMainMenu(boolean hasCurrentLevel);
    void switchToLoadStage(SpriteManager spriteManager, WorldManager worldManager, boolean lastStageWasMainMenu);
    void switchToSaveStage(SpriteManager spriteManager, WorldManager worldManager);
    void switchToOptionStage(boolean lastStageWasMainMenu);
    void switchToLoadingStage(int monsterSpawnChance, int roomSpawnChance);
    void switchToHighscoreStage();
    void switchToDeathStage();
}
