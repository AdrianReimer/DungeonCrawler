package com.mygdx.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.constant.GameConstants;
import com.mygdx.music.SoundInterface;
import com.mygdx.screen.GameScreen;

public class HighscoreStage extends Stage {
	
    private boolean visible = false;
    private StageSwitchInterface stageSwitchInterface;
    private GameSaveInterface gameSaveInterface;
	
    /**
     * DeathStage constructor.
     * @param stageSwitchInterface | {@link StageSwitchInterface}.
     * @param soundInterface | {@link SoundInterface}.
     */
	public HighscoreStage (final StageSwitchInterface stageSwitchInterface, final SoundInterface soundInterface, final GameSaveInterface gameSaveInterface) {
		this.stageSwitchInterface = stageSwitchInterface;
		this.gameSaveInterface = gameSaveInterface;
		Skin skin = GameScreen.SKIN;
		Table rootTable = new Table(skin);
		rootTable.background(GameConstants.TABLE_BACKGROUND);
		rootTable.setFillParent(true);

		
		// add actors to the stage
		addActor(rootTable);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.ENTER ) {
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
	
	private void loadHighscores () {
		
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
