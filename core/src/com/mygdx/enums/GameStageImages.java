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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ObjectMap;

public enum GameStageImages {
	// GameStage
    HEALTHBAR_FRAME("data/GUI/GameStage/healthbar_frame.png"),
    HEALTHBAR_SQUARES("data/GUI/GameStage/healthbar_squares.png"),
    MANABAR_FRAME("data/GUI/GameStage/manabar_frame.png"),
    MANABAR_SQUARES("data/GUI/GameStage/manabar_squares.png"),
    STAMINABAR_FRAME("data/GUI/GameStage/staminabar_frame.png"),
    STAMINABAR_SQUARES("data/GUI/GameStage/staminabar_squares.png"),
    BACKGROUND_FRAME("data/GUI/GameStage/background_frame.png"),
    TREASURE_CRATE("data/GUI/GameStage/treasure_crate.png");
    
    private String fileName = null;

    GameStageImages(String fileName) {
        this.fileName = fileName;
    }

    public Image get() {
        if (fileName != null) {
            return new Image(new Texture(fileName));
        } else {
            throw new IllegalArgumentException("No filename for this texture!");
        }
    }
    
    public static ObjectMap<GameStageImages, Image> loadGameStageImages() {
        ObjectMap<GameStageImages, Image> imageMap = new ObjectMap<>();
        for (GameStageImages t : GameStageImages.values()) {
        	imageMap.put(t, t.get());
        }
        return imageMap;
    }
}
