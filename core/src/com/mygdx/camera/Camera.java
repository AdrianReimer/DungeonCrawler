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

package com.mygdx.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.model.Knight;

/**
 * Orthographic Camera that is focusing on the {@link Knight}.
 * @author Adrian Reimer
 *
 */
public class Camera extends OrthographicCamera{
	
	private OrthographicCamera orthographicCamera;
	
	/**
	 * Camera constructor.
	 */
	public Camera() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		orthographicCamera = new OrthographicCamera(w,h);
	}
	
	/**
	 * Updates the camera and focuses on the {@link Knight}.
	 * @param knightSprite | {@link Sprite} of  the {@link Knight}.
	 * @return {@link OrthographicCamera}.
	 */
	public OrthographicCamera updateCamera (Sprite knightSprite) {
		orthographicCamera.update();
		orthographicCamera.position.x = knightSprite.getOriginX() + knightSprite.getX() ;
		orthographicCamera.position.y = knightSprite.getOriginY() + knightSprite.getY();
		return orthographicCamera;
	}
	
	/**
	 * resizes the camera.
	 * @param width | width of the Screen.
	 * @param height | height of the Screen.
	 */
	public void resize(int width, int height) {
		orthographicCamera = new OrthographicCamera(width,height);
	}

	/**
	 * camera getter.
	 * @return {@link OrthographicCamera}.
	 */
	public OrthographicCamera getCamera() {
		return orthographicCamera;
	}
	
}
