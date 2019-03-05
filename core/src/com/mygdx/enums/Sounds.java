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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

public enum Sounds {
	// GameScreen Sounds
	SWORD_SWING("data/Music/swordSwing.mp3"),
    GOLD("data/Music/gold.mp3"),
    BOTTLE("data/Music/bottle.mp3");

    private String fileName = null;

    Sounds(String fileName) {
        this.fileName = fileName;
    }

    public Sound get() {
        if (fileName != null) {
            return Gdx.audio.newSound(Gdx.files.internal(fileName));
        } else {
            throw new IllegalArgumentException("No filename for this Sound!");
        }
    }
    
    public static ObjectMap<Sounds, Sound> loadAllSounds() {
        ObjectMap<Sounds, Sound> soundMap = new ObjectMap<>();
        for (Sounds t : Sounds.values()) {
            soundMap.put(t, t.get());
        }
        return soundMap;
    }

}
