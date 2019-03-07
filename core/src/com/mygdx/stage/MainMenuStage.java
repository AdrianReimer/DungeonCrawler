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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.Manager.SpriteManager;
import com.mygdx.Manager.WorldManager;
import com.mygdx.constant.GameConstants;
import com.mygdx.enums.GameTexts;
import com.mygdx.music.SoundInterface;
import com.mygdx.screen.GameScreen;

/**
 * Main Menu Stage (stage after starting the .jar)
 * @author Adrian Reimer
 *
 */
public class MainMenuStage extends Stage {

	private boolean visible = true;
	private TextButton resumeButton;

	/**
	 * MainMenuStage constructor.
	 * @param stageSwitchInterface | {@link StageSwitchInterface}.
	 * @param soundInterface | {@link SoundInterface}.
	 * @param worldManager | {@link WorldManager}.
	 * @param spriteManager | {@link SpriteManager}.
	 */
	public MainMenuStage(final StageSwitchInterface stageSwitchInterface,final SoundInterface soundInterface,WorldManager worldManager,SpriteManager spriteManager) {
		Skin skin = GameScreen.SKIN;
		Table rootTable = new Table(skin);
		rootTable.background(GameConstants.TABLE_BACKGROUND);
		rootTable.setFillParent(true);
		rootTable.align(Align.topLeft);

		Label label = new Label(GameTexts.MAIN_MENU_STAGE_LABEL.get(),skin);
		rootTable.add(label);
		rootTable.row();

		resumeButton = new TextButton(GameTexts.MAIN_MENU_STAGE_RESUME_BUTTON.get(), skin);
		resumeButton.setVisible(false);
		resumeButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				visible = false;
				stageSwitchInterface.switchToGameStage(false);
			}
		});
		TextButton newgameButton = new TextButton(GameTexts.MAIN_MENU_STAGE_NEWGAME_BUTTON.get(), skin);
		newgameButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				visible = false;
				stageSwitchInterface.switchToGameStage(true);
			}
		});
		TextButton loadButton = new TextButton(GameTexts.MAIN_MENU_STAGE_LOAD_BUTTON.get(), skin);
		loadButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				visible = false;
				stageSwitchInterface.switchToLoadStage(spriteManager, worldManager, true);
			}
		});
		TextButton optionButton = new TextButton(GameTexts.MAIN_MENU_STAGE_OPTION_BUTTON.get(), skin);
		optionButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				visible = false;
				stageSwitchInterface.switchToOptionStage(true);
			}
		});
		TextButton highscoreButton = new TextButton(GameTexts.MAIN_MENU_STAGE_HIGHSCORE_BUTTON.get(), skin);
		highscoreButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				visible = false;
				stageSwitchInterface.switchToHighscoreStage();
			}
		});
		TextButton exitButton = new TextButton(GameTexts.MAIN_MENU_STAGE_EXIT_BUTTON.get(), skin);
		exitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				// expose
				// maybe want to save
				Gdx.app.exit();
			}
		});
		rootTable.add(resumeButton).align(Align.bottomLeft);
		rootTable.row();
		rootTable.add(newgameButton).align(Align.bottomLeft);
		rootTable.row();
		rootTable.add(loadButton).align(Align.bottomLeft);
		rootTable.row();
		rootTable.add(optionButton).align(Align.bottomLeft);
		rootTable.row();
		rootTable.add(highscoreButton).align(Align.bottomLeft);
		rootTable.row();
		rootTable.add(exitButton).align(Align.bottomLeft);
		addActor(rootTable);

	}

	@Override
	public void draw() {
		if (visible) {
			act(Gdx.graphics.getDeltaTime());
			super.draw();
		}
	}

	public TextButton getResumeButton() {
		return resumeButton;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
