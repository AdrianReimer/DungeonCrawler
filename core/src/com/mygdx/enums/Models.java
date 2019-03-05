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
import com.badlogic.gdx.utils.ObjectMap;

public enum Models {

    KNIGHT("data/Kickpixel - Dungeon Knight/spritesheets/knight_anims.png"),
    SLIME("data/Kickpixel - Dungeon Knight/spritesheets/slime.png");
    
    private String fileName;

    Models(String fileName) {
        this.fileName = fileName;
    }

    public Texture getModelTexture() {
        if (fileName != null) {
            return new Texture(fileName);
        } else {
            throw new IllegalArgumentException("No filename for this texture!");
        }
    }
    
    public static ObjectMap<Models, Texture> loadModelImages() {
        ObjectMap<Models, Texture> textureMap = new ObjectMap<>();
        for (Models t : Models.values()) {
        	textureMap.put(t, t.getModelTexture());
        }
        return textureMap;
    }
}
