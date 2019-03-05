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

package com.mygdx.enums;

import com.badlogic.gdx.utils.Queue;
import com.mygdx.constant.GameConstants;

public enum Difficulty {
	LEVEL1(20,10,"Level 1"),
	LEVEL2(19,12,"Level 2"),
	LEVEL3(18,14,"Level 3"),
	LEVEL4(17,16,"Level 4"),
	LEVEL5(16,18,"Level 5"),
	LEVEL6(15,20,"Level 6"),
	LEVEL7(14,22,"Level 7"),
	LEVEL8(13,24,"Level 8"),
	LEVEL9(12,26,"Level 9"),
	LEVEL10(11,28,"Level 10"),
	LEVEL11(10,30,"Level 11"),
	LEVEL12(9,32,"Level 12"),
	LEVEL13(8,34,"Level 13"),
	LEVEL14(7,36,"Level 14"),
	LEVEL15(6,38,"Level 15"),
	LEVEL16(5,40,"Level 16"),
	LEVEL17(4,42,"Level 17"),
	LEVEL18(3,44,"Level 18"),
	LEVEL19(2,46,"Level 19"),
	LEVEL20(1,48,"Level 20");
	
    private int monsterSpawnChance;
    private int roomSpawnChance;
    private String levelName;
    

    Difficulty(int monsterSpawnChance, int roomSpawnChance, String levelName) {
        this.monsterSpawnChance = monsterSpawnChance;
        this.roomSpawnChance = roomSpawnChance;
        this.levelName = levelName;
    }

    public int getMonsterSpawnChance() {
        return GameConstants.RANDOM.nextInt(monsterSpawnChance)+1;
    }
    
    public int getRoomSpawnChance() {
        return roomSpawnChance;
    }
    
    public String getLevelName() {
        return levelName;
    }
    
    public static Queue<Difficulty> loadDifficulty() {
    	Queue<Difficulty> difficultyQueue = new Queue<>();
        for (Difficulty t : Difficulty.values()) {
        	difficultyQueue.addFirst(t);
        }
        return difficultyQueue;
    }
    
}
