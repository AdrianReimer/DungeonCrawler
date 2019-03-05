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

package com.mygdx.music;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.enums.Musics;
import com.mygdx.enums.Sounds;
import com.mygdx.screen.GameScreen;

/**
 * Interface between Sounds and {@link GameScreen} where the related {@link ObjectMap}s are stored.
 * @author Adrian Reimer
 *
 */
public interface SoundInterface {
	
    Sound getSound(Sounds sound);
    Music getMusic(Musics music);
}
