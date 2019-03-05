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

import com.badlogic.gdx.audio.Sound;
import com.mygdx.enums.Sounds;

/**
 * Handles the Sound Effects (short sounds/non music related)
 * @author Adrian Reimer
 *
 */
public class SoundEffect{
	
	private static final float SWORD_SWING_BASE_VOLUME = 0.2f;
	private static final float GOLD_BASE_VOLUME = 0.15f;
	private static final float BOTTLE_BASE_VOLUME = 0.1f;
	
	private float soundEffectMaster;
	private Sound swordSwing;
	private Sound gold;
	private Sound bottle;
	
	/**
	 * SoundEffect constructor.
	 * @param soundInterface | {@link SoundInterface}.
	 */
	public SoundEffect(final SoundInterface soundInterface) {
		soundEffectMaster = 1f;
		swordSwing = soundInterface.getSound(Sounds.SWORD_SWING);
		gold = soundInterface.getSound(Sounds.GOLD);
		bottle = soundInterface.getSound(Sounds.BOTTLE);
	}
	
	public void setSoundEffectMaster(float soundEffectMaster) {
		this.soundEffectMaster = soundEffectMaster;
	}

	public float getSoundEffectMaster() {
		return soundEffectMaster;
	}

	/**
	 * plays the sword swing sound.
	 */
	public void playSwordSwing () {
		swordSwing.play(SWORD_SWING_BASE_VOLUME * soundEffectMaster);
	}
	
	/**
	 * plays the gold collect sound.
	 */
	public void playGold () {
		gold.play(GOLD_BASE_VOLUME * soundEffectMaster);
	}
	
	/**
	 * plays the bottle drink sound.
	 */
	public void playBottle() {
		bottle.play(BOTTLE_BASE_VOLUME * soundEffectMaster);
	}

}
