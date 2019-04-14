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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.Manager.SoundManager;
import com.mygdx.constant.GameConstants;
import com.mygdx.enums.GameTexts;
import com.mygdx.screen.GameScreen;

/**
 * Option Stage for changing settings.
 * 
 * @author Adrian Reimer
 *
 */
public class OptionStage extends Stage {

	private static final float BOX_SPACE_BOTTOM = 20;
	private static final float WINDOW_SPACE_BOTTOM = 30;
	private static final float WINDOW_PAD = 50;

	private int screenWidth; // saved window width and height
	private int screenHeight; // --
	private boolean visible = false;
	private boolean lastStageWasMainMenu = false;
	private SoundManager soundManager;
	private StageSwitchInterface stageSwitchInterface;

	/**
	 * OptionStage constructor.
	 * 
	 * @param stageSwitchInterface
	 *            | {@link StageSwitchInterface}.
	 * @param soundManager
	 *            | {@link SoundManager}.
	 */
	public OptionStage(final StageSwitchInterface stageSwitchInterface,
			SoundManager soundManager) {
		this.stageSwitchInterface = stageSwitchInterface;
		this.soundManager = soundManager;
		Skin skin = GameScreen.SKIN;
		Table rootTable = new Table(skin);
		rootTable.background(GameConstants.TABLE_BACKGROUND);
		rootTable.setFillParent(true);
		// add Windows
		Window displaySettingsWindow = new Window(
				GameTexts.OPTION_STAGE_DISPLAY_SETTINGS_TEXT.get(), skin,
				GameConstants.DIALOG_WINDOW_STYLE);
		displaySettingsWindow.pad(WINDOW_PAD);
		displaySettingsWindow.setMovable(false);
		Window gameSettingsWindow = new Window(
				GameTexts.OPTION_STAGE_GAME_SETTINGS_TEXT.get(), skin,
				GameConstants.DIALOG_WINDOW_STYLE);
		gameSettingsWindow.pad(WINDOW_PAD);
		gameSettingsWindow.setMovable(false);
		Window soundSettingsWindow = new Window(
				GameTexts.OPTION_STAGE_SOUND_SETTINGS_TEXT.get(), skin,
				GameConstants.DIALOG_WINDOW_STYLE);
		soundSettingsWindow.pad(WINDOW_PAD);
		soundSettingsWindow.setMovable(false);
		Window musicSettingsWindow = new Window(
				GameTexts.OPTION_STAGE_MUSIC_SETTINGS_TEXT.get(), skin,
				GameConstants.DIALOG_WINDOW_STYLE);
		musicSettingsWindow.pad(WINDOW_PAD);
		musicSettingsWindow.setMovable(false);
		// add select boxes
		SelectBox<String> windowModeSelectBox = new SelectBox<>(skin);
		SelectBox<String> resolutionSelectBox = new SelectBox<>(skin);
		windowModeSelectBox.setItems("Windowed", "Fullscreen");
		windowModeSelectBox.setMaxListCount(2);
		windowModeSelectBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				switch (windowModeSelectBox.getSelected()) {
				case "Fullscreen":
					screenWidth = Gdx.graphics.getWidth(); // last window scales
					screenHeight = Gdx.graphics.getHeight(); // --
					resolutionSelectBox.setVisible(false);
					Gdx.graphics.setFullscreenMode(Gdx.graphics
							.getDisplayMode());
					break;
				case "Windowed":
					Gdx.graphics.setWindowedMode(screenWidth, screenHeight);
					resolutionSelectBox.setVisible(true);
					break;
				default:
					break;
				}
			}
		});
		resolutionSelectBox.setItems("1280 x 720", "800 x 600", "1024 x 768",
				"1280 x 800", "1280 x 1024", "1360 x 768", "1366 x 768",
				"1440 x 900", "1536 x 864", "1600 x 900", "1680 x 1050",
				"1920 x 1080", "1920 x 1200", "2048 x 1152", "2560 x 1080",
				"2560 x 1440", "3340 x 1440", "3840 x 2160");
		resolutionSelectBox.setMaxListCount(6);
		resolutionSelectBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				switch (resolutionSelectBox.getSelected()) {
				case "800 x 600":
					Gdx.graphics.setWindowedMode(800, 600);
					break;
				case "1024 x 768":
					Gdx.graphics.setWindowedMode(1024, 768);
					break;
				case "1280 x 720":
					Gdx.graphics.setWindowedMode(1280, 720);
					break;
				case "1280 x 800":
					Gdx.graphics.setWindowedMode(1280, 800);
					break;
				case "1280 x 1024":
					Gdx.graphics.setWindowedMode(1280, 1024);
					break;
				case "1360 x 768":
					Gdx.graphics.setWindowedMode(1360, 768);
					break;
				case "1366 x 768":
					Gdx.graphics.setWindowedMode(1366, 768);
					break;
				case "1440 x 900":
					Gdx.graphics.setWindowedMode(1440, 900);
					break;
				case "1536 x 864":
					Gdx.graphics.setWindowedMode(1536, 864);
					break;
				case "1600 x 900":
					Gdx.graphics.setWindowedMode(1600, 900);
					break;
				case "1680 x 1050":
					Gdx.graphics.setWindowedMode(1680, 1050);
					break;
				case "1920 x 1080":
					Gdx.graphics.setWindowedMode(1920, 1080);
					break;
				case "1920 x 1200":
					Gdx.graphics.setWindowedMode(1920, 1200);
					break;
				case "2048 x 1152":
					Gdx.graphics.setWindowedMode(2048, 1152);
					break;
				case "2560 x 1080":
					Gdx.graphics.setWindowedMode(2560, 1080);
					break;
				case "2560 x 1440":
					Gdx.graphics.setWindowedMode(2560, 1440);
					break;
				case "3340 x 1440":
					Gdx.graphics.setWindowedMode(3340, 1440);
					break;
				case "3840 x 2160":
					Gdx.graphics.setWindowedMode(3840, 2160);
					break;
				default:
					windowModeSelectBox.fire(event);
					break;
				}
			}
		});
		// add check boxes
		CheckBox vsyncBox = new CheckBox("VSync", skin);
		vsyncBox.setChecked(true);
		vsyncBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				Gdx.graphics.setVSync(vsyncBox.isPressed());
			}
		});
		// add Sliders
		final Slider soundEffectMasterSlider = new Slider(0, 10, 0.1f, false,
				skin);
		soundEffectMasterSlider.setValue(soundManager.getSoundEffect()
				.getSoundEffectMaster());
		soundEffectMasterSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				soundManager.getSoundEffect().setSoundEffectMaster(
						soundEffectMasterSlider.getValue());
			}
		});
		final Slider soundScapeMasterSlider = new Slider(0, 10, 0.1f, false,
				skin);
		soundScapeMasterSlider.setValue(soundManager.getSoundscape()
				.getSoundScapeMaster());
		soundScapeMasterSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				soundManager.getSoundscape().setSoundScapeMaster(
						soundScapeMasterSlider.getValue());
			}
		});
		// Buttons
		TextButton back = new TextButton(
				GameTexts.OPTION_STAGE_BACK_BUTTON.get(), skin);
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				escapeEvent();
			}
		});
		// fill windows
		displaySettingsWindow.add(windowModeSelectBox).spaceBottom(
				BOX_SPACE_BOTTOM);
		displaySettingsWindow.row();
		displaySettingsWindow.add(resolutionSelectBox).spaceBottom(
				BOX_SPACE_BOTTOM);
		gameSettingsWindow.add(vsyncBox).spaceBottom(BOX_SPACE_BOTTOM);
		soundSettingsWindow.add(soundEffectMasterSlider).spaceBottom(
				BOX_SPACE_BOTTOM);
		musicSettingsWindow.add(soundScapeMasterSlider).spaceBottom(
				BOX_SPACE_BOTTOM);
		// fill root Table
		rootTable.add(displaySettingsWindow).spaceBottom(WINDOW_SPACE_BOTTOM);
		rootTable.row();
		rootTable.add(gameSettingsWindow).spaceBottom(WINDOW_SPACE_BOTTOM);
		rootTable.row();
		rootTable.add(soundSettingsWindow).spaceBottom(WINDOW_SPACE_BOTTOM);
		rootTable.row();
		rootTable.add(musicSettingsWindow).spaceBottom(WINDOW_SPACE_BOTTOM);
		rootTable.row();
		rootTable.add(back);
		// add Actors to Stage
		addActor(rootTable);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.ESCAPE) {
			escapeEvent();
		}
		return true;
	}

	/**
	 * handles event when esc-key is pressed.
	 */
	private void escapeEvent() {
		visible = false;
		if (lastStageWasMainMenu)
			stageSwitchInterface.switchToMainMenu();
		else
			stageSwitchInterface.switchToGameStage(false);
	}

	@Override
	public void draw() {
		if (visible) {
			act(Gdx.graphics.getDeltaTime());
			super.draw();
		}
	}

	public void setLastStageWasMainMenu(boolean lastStageWasMainMenu) {
		this.lastStageWasMainMenu = lastStageWasMainMenu;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public void setSoundManager(SoundManager soundManager) {
		this.soundManager = soundManager;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
