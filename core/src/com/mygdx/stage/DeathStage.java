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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.mygdx.constant.GameConstants;
import com.mygdx.enums.GameTexts;
import com.mygdx.model.Knight;
import com.mygdx.music.SoundInterface;
import com.mygdx.screen.GameScreen;

/**
 * Stage after the {@link Knight} died. 
 * @author Adrian Reimer
 *
 */
public class DeathStage extends Stage {
	
    private boolean visible = false;
    private TextField textField;
    private StageSwitchInterface stageSwitchInterface;
	
    /**
     * DeathStage constructor.
     * @param stageSwitchInterface | {@link StageSwitchInterface}.
     * @param soundInterface | {@link SoundInterface}.
     */
	public DeathStage (final StageSwitchInterface stageSwitchInterface, final SoundInterface soundInterface) {
		this.stageSwitchInterface = stageSwitchInterface;
		Skin skin = GameScreen.SKIN;
		Table rootTable = new Table(skin);
		rootTable.background(GameConstants.TABLE_BACKGROUND);
		rootTable.setFillParent(true);
		// add textfield for Highscore-list
		textField = new TextField("", skin);
		textField.scaleBy(2);
		// add label
		Label label = new Label(GameTexts.DEATH_STAGE_LABEL.get(),skin);
		rootTable.add(label);
		rootTable.row();
		rootTable.add(textField);
		// add actors to the stage
		addActor(rootTable);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.ENTER && textField.getText().length() > 0) {
			visible = false;
			stageSwitchInterface.switchToMainMenu(false);
		}
		return true;
	}
	
	@Override
	public void draw() {
		if (visible) {
			act(Gdx.graphics.getDeltaTime());
			super.draw();
		}
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
