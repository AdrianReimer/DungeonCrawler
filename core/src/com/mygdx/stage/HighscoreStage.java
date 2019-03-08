package com.mygdx.stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.constant.GameConstants;
import com.mygdx.enums.GameSaves;
import com.mygdx.enums.GameTexts;
import com.mygdx.music.SoundInterface;
import com.mygdx.screen.GameScreen;
import com.mygdx.writer.Highscore;

/**
 * Displays 
 * @author adria
 *
 */
public class HighscoreStage extends Stage {
	
	private static final int HIGHSCORE_DISPLAY_AMOUNT = 10;
	
    private boolean visible = false;
    private StageSwitchInterface stageSwitchInterface;
    private GameSaveInterface gameSaveInterface;
    private List<Highscore> highscoreList;
    private Label highscoreLabel;
	
    /**
     * DeathStage constructor.
     * @param stageSwitchInterface | {@link StageSwitchInterface}.
     * @param soundInterface | {@link SoundInterface}.
     */
	public HighscoreStage (final StageSwitchInterface stageSwitchInterface, final SoundInterface soundInterface, final GameSaveInterface gameSaveInterface) {
		this.stageSwitchInterface = stageSwitchInterface;
		this.gameSaveInterface = gameSaveInterface;
		highscoreList = new ArrayList<>();
		loadHighscores(gameSaveInterface.getPath(GameSaves.HIGHSCORE_FILEPATH));
		Skin skin = GameScreen.SKIN;
		Table rootTable = new Table(skin);
		rootTable.background(GameConstants.TABLE_BACKGROUND);
		rootTable.setFillParent(true);
		// Labels
		Label label = new Label(GameTexts.HIGHSCORE_STAGE_LABEL.get(),skin);
		highscoreLabel = new Label("",skin);
		updateHighscores();
		// Buttons
		TextButton back = new TextButton(GameTexts.HIGHSCORE_STAGE_BACK_BUTTON.get(), skin);
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				visible = false;
				stageSwitchInterface.switchToMainMenu();
			}
		});
		rootTable.add(label);
		rootTable.row();
		rootTable.add(highscoreLabel);
		rootTable.row();
		rootTable.add(back);
		// add actors to the stage
		addActor(rootTable);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.ESCAPE ) {
			visible = false;
			stageSwitchInterface.switchToMainMenu();
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
	
	private void loadHighscores (String filepath) {
		highscoreList.clear();
		int highscoreNumber = 1;
		JSONParser parser = new JSONParser();
		try {     
            Object obj = parser.parse(new FileReader(filepath));
            JSONObject jsonObject =  (JSONObject) obj;
            // load all player highscores
            for(JSONObject player = (JSONObject)jsonObject.get("Player"+highscoreNumber);player != null;player = (JSONObject)jsonObject.get("Player"+highscoreNumber) ) {
            	Highscore highscore = new Highscore((String)player.get("name"),(int)(long)player.get("gold"));
            	highscoreList.add(highscore);
            	highscoreNumber++;
            }
        } catch (IOException | ParseException e) {
        	Gdx.app.log("LoadSaveDataDate", "Error loading SaveDataDate",e);
        }
	}
	
	public void updateHighscores() {
		loadHighscores(gameSaveInterface.getPath(GameSaves.HIGHSCORE_FILEPATH));
		Collections.sort(highscoreList); // sort List by gold value
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < HIGHSCORE_DISPLAY_AMOUNT; i++) {
			if(i == highscoreList.size()) {
				break;
			}
			// build highscoreList string
			stringBuilder.append(highscoreList.get(i).getName());
			stringBuilder.append(" - ");
			stringBuilder.append(highscoreList.get(i).getGold() + "g\n");
		}
		stringBuilder.append("\n\n\n");
		highscoreLabel.setText(stringBuilder.toString());
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public List<Highscore> getHighscoreList() {
		return highscoreList;
	}

}
