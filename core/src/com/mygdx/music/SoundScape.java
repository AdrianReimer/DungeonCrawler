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
import com.mygdx.enums.Musics;

/**
 * Handles the Sound Scapes (long sounds/background sounds/music)
 * @author Adrian Reimer
 *
 */
public class SoundScape{
	
	private static final float BACKGROUND_MUSIC_BASE_VOLUME = 0.05f;
	
	private float soundScapeMaster;
	private Music backgroundMusic;
	
	/**
	 * SoundScape constructor.
	 * @param soundInterface | {@link SoundInterface}.
	 */
	public SoundScape(final SoundInterface soundInterface) {
		soundScapeMaster = 1;
		backgroundMusic = soundInterface.getMusic(Musics.VIDEO_DUNGEON_CRAWL);
		backgroundMusic.setVolume(BACKGROUND_MUSIC_BASE_VOLUME);
		backgroundMusic.setLooping(true);
	}
	
	private void setBackgroundMusicVolume() {
		backgroundMusic.setVolume(BACKGROUND_MUSIC_BASE_VOLUME * soundScapeMaster);
	}
	
	public float getSoundScapeMaster() {
		return soundScapeMaster;
	}

	public void setSoundScapeMaster(float soundScapeMaster) {
		this.soundScapeMaster = soundScapeMaster;
		setBackgroundMusicVolume();
	}

	/**
	 * plays the background music.
	 */
	public void playBackgroundMusic () {
		backgroundMusic.play();
	}
	
	/**
	 * stops the background music.
	 */
	public void stopBackgroundMusic () {
		backgroundMusic.stop();
	}
}
