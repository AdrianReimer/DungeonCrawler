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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.ObjectMap;

public enum Musics {
	// GameScreen Music
	VIDEO_DUNGEON_CRAWL("data/Music/Video Dungeon Crawl.mp3");

    private String fileName = null;

    Musics(String fileName) {
        this.fileName = fileName;
    }

    public Music get() {
        if (fileName != null) {
            return Gdx.audio.newMusic(Gdx.files.internal(fileName));
        } else {
            throw new IllegalArgumentException("No filename for this Sound!");
        }
    }
    
    public static ObjectMap<Musics, Music> loadAllMusics() {
        ObjectMap<Musics, Music> musicMap = new ObjectMap<>();
        for (Musics t : Musics.values()) {
            musicMap.put(t, t.get());
        }
        return musicMap;
    }
}
