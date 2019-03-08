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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.Manager.EventManager;
import com.mygdx.Manager.SoundManager;
import com.mygdx.Manager.SpriteManager;
import com.mygdx.Manager.WorldManager;
import com.mygdx.constant.GameConstants;
import com.mygdx.enums.Difficulty;
import com.mygdx.enums.GameStageImages;
import com.mygdx.enums.GameTexts;
import com.mygdx.model.Knight;
import com.mygdx.model.ModelInterface;
import com.mygdx.music.SoundInterface;
import com.mygdx.screen.GameScreen;

/**
 * Main {@link Stage} where the game logic is in.
 * @author Adrian Reimer
 *
 */
public class GameStage extends Stage implements Disposable,ItemInterface,LoadInterface{
	
	public static final float BAR_SPACE_BOTTOM = 25f;
	public static final float VALUE_SPACE_BOTTOM = 93.5f;
	public static final float BAR_PAD_TOP = 510f;
	public static final float SQUARES_MOVE = -14.65f;
	public static final float FONT_SCALE = 0.7f;
	private static final float BARS_UPDATE_DELAY = 0.2f;
	
	private int gold;
    private boolean visible;
	private boolean menuIsOpen;
	private WorldManager worldManager;
	private EventManager eventManager;
	private SoundManager soundManager;
	private SpriteManager spriteManager;
	private Table menuTable;
	private Table deathTable;
	private Table tableBarsHealthSquares;
	private Table tableBarsStaminaSquares;
	private Label healthValue;
	private Label staminaValue;
	private Label goldValue;
	private Label levelValue;
	private Queue<Integer> movement = new Queue<>(4);
	private Difficulty difficulty;
	private StageSwitchInterface stageSwitchInterface;
	private GameStageInterface gameStageInterface;
	private Timer timer;

	/**
	 * GameStage constructor.
	 * @param stageSwitchInterface | {@link StageSwitchInterface}.
	 * @param gameStageInterface | {@link GameStageInterface}.
	 * @param soundInterface | {@link SoundInterface}.
	 */
    public GameStage(final StageSwitchInterface stageSwitchInterface,final GameStageInterface gameStageInterface,final SoundInterface soundInterface, final ModelInterface modelInterface) {
    	this.stageSwitchInterface = stageSwitchInterface;
    	this.gameStageInterface = gameStageInterface;
    	timer = new Timer();
    	Skin skin = GameScreen.SKIN;
    	healthValue = new Label(GameTexts.GAME_STAGE_DEFAULT_VALUE.get(),skin);
    	healthValue.setFontScale(FONT_SCALE);
    	healthValue.setColor(Color.RED);
    	staminaValue = new Label(GameTexts.GAME_STAGE_DEFAULT_VALUE.get(),skin);
    	staminaValue.setFontScale(FONT_SCALE);
    	staminaValue.setColor(Color.FOREST);
    	goldValue = new Label("Gold " + gold,skin);
    	goldValue.setFontScale(FONT_SCALE*2);
    	goldValue.setColor(Color.GOLD);
    	levelValue = new Label("Level 1",skin);
    	levelValue.setFontScale(FONT_SCALE*2);
    	levelValue.setColor(Color.GRAY);
    	// create menu Table 
		menuTable = new Table(skin);
		menuTable.background(GameConstants.TABLE_BACKGROUND);
		menuTable.setWidth(250f);
		menuTable.setHeight(280f);
		menuTable.setPosition((float)Gdx.graphics.getWidth()/2, (float)Gdx.graphics.getHeight()/2, Align.center);
		menuTable.setVisible(false);
		Label menuLabel = new Label(GameTexts.GAME_STAGE_MENU_LABEL.get(),skin);
		menuTable.add(menuLabel);
		menuTable.row();
		// create Death Table
		deathTable = new Table(skin);
		deathTable.background(GameConstants.TABLE_BACKGROUND);
		deathTable.setWidth(800f);
		deathTable.setHeight(600f);
		deathTable.setPosition((float)Gdx.graphics.getWidth()/2, (float)Gdx.graphics.getHeight()/2, Align.center);
		deathTable.setVisible(false);
		Label deathLabel = new Label(GameTexts.GAME_STAGE_DEATH_LABEL.get(),skin);
		deathTable.add(deathLabel);
		deathTable.row();
		// menu Table Buttons
		TextButton saveButton = new TextButton(GameTexts.GAME_STAGE_SAVE_BUTTON.get(), skin);
		saveButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				visible = false;
				stageSwitchInterface.switchToSaveStage(spriteManager,worldManager);
			}
		});
		TextButton loadButton = new TextButton(GameTexts.GAME_STAGE_LOAD_BUTTON.get(), skin);
		loadButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				visible = false;
				stageSwitchInterface.switchToLoadStage(false);
			}
		});
		TextButton optionButton = new TextButton(GameTexts.GAME_STAGE_OPTION_BUTTON.get(), skin);
		optionButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				visible = false;
				stageSwitchInterface.switchToOptionStage(false);
			}
		});
		TextButton mainMenuButton = new TextButton(GameTexts.GAME_STAGE_MAINMENU_BUTTON.get(), skin);
		mainMenuButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				visible = false;
				stageSwitchInterface.switchToMainMenu();
			}
		});
		TextButton exitButton = new TextButton(GameTexts.GAME_STAGE_EXIT_BUTTON.get(), skin);
		exitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});
		// death Table Buttons
		TextButton yesButton = new TextButton(GameTexts.GAME_STAGE_YES_BUTTON.get(), skin);
		yesButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				visible = false;
				deathTable.setVisible(false);
				worldManager.setUpdatingLevel(false);
				stageSwitchInterface.switchToDeathStage();
			}
		});
		TextButton noButton = new TextButton(GameTexts.GAME_STAGE_NO_BUTTON.get(), skin);
		noButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				visible = false;
	    		deathTable.setVisible(false);
	    		worldManager.setUpdatingLevel(false);
				stageSwitchInterface.switchToMainMenu();
			}
		});
		// Filling menu Table
		menuTable.add(saveButton);
		menuTable.row();
		menuTable.add(loadButton);
		menuTable.row();
		menuTable.add(optionButton);
		menuTable.row();
		menuTable.add(mainMenuButton);
		menuTable.row();
		menuTable.add(exitButton);
		// Filling death Table
		deathTable.add(yesButton);
		deathTable.row();
		deathTable.add(noButton);
		// create other Tables
    	Table tableBarsFrame = new Table();
    	tableBarsHealthSquares = new Table();
    	tableBarsStaminaSquares = new Table();
    	Table tableBarsValues = new Table(skin);	
    	Table gameValues = new Table(skin);
    	Table background = new Table();
    	// let actor size to the stage
    	tableBarsFrame.setFillParent(true);
    	tableBarsHealthSquares.setFillParent(true);
    	tableBarsStaminaSquares.setFillParent(true);
    	tableBarsValues.setFillParent(true);
    	background.setFillParent(true);
    	gameValues.setFillParent(true);
    	// set position
    	tableBarsFrame.center().left().padTop(BAR_PAD_TOP);
    	tableBarsHealthSquares.center().left().padTop(BAR_PAD_TOP - 106);
    	tableBarsStaminaSquares.center().left().padTop(BAR_PAD_TOP + 106);
    	tableBarsValues.center().left().padTop(BAR_PAD_TOP - 30).padLeft(100f);
    	gameValues.padTop(BAR_PAD_TOP - 320).padRight(970);
    	background.left().bottom();
        // add buttons to tables
    	tableBarsFrame.add(gameStageInterface.getImage(GameStageImages.HEALTHBAR_FRAME)).spaceBottom(BAR_SPACE_BOTTOM).size(278, 81);
    	tableBarsFrame.row();
    	tableBarsFrame.add(gameStageInterface.getImage(GameStageImages.STAMINABAR_FRAME)).spaceBottom(BAR_SPACE_BOTTOM).size(278, 81);
    	tableBarsHealthSquares.add(gameStageInterface.getImage(GameStageImages.HEALTHBAR_SQUARES)).spaceBottom(BAR_SPACE_BOTTOM).size(278, 81);
    	tableBarsStaminaSquares.add(gameStageInterface.getImage(GameStageImages.STAMINABAR_SQUARES)).spaceBottom(BAR_SPACE_BOTTOM).size(278, 81);
    	background.add(gameStageInterface.getImage(GameStageImages.BACKGROUND_FRAME)).size(300, 320);
    	gameValues.add(levelValue).spaceBottom(VALUE_SPACE_BOTTOM/3);
    	gameValues.row();
    	gameValues.add(goldValue).spaceBottom(VALUE_SPACE_BOTTOM/3);
    	tableBarsValues.add(healthValue).spaceBottom(VALUE_SPACE_BOTTOM);
    	tableBarsValues.row();
    	tableBarsValues.add(staminaValue).spaceBottom(VALUE_SPACE_BOTTOM);
        // add tables to stage
    	addActor(background);
        addActor(tableBarsHealthSquares);
        addActor(tableBarsStaminaSquares);
        addActor(tableBarsFrame);
        addActor(tableBarsValues);
        addActor(gameValues);
        addActor(menuTable);
        addActor(deathTable);
    	// create managers
		soundManager = new SoundManager(soundInterface);
		worldManager = new WorldManager();
		spriteManager = new SpriteManager(worldManager.getCamera());
		eventManager = new EventManager(soundManager,worldManager,spriteManager,this,modelInterface,deathTable);
		// start updateBars task
		updateBars();		
    }

    @Override
    public void draw() {
        if (visible) {
        	if(eventManager.isOnEndTile()) {
        		if(difficulty != null)
        			gameStageInterface.removeDifficulty();
        		difficulty = gameStageInterface.getDifficulty();
        		levelValue.setText(difficulty.getLevelName());
        		eventManager.killMonsters();
        		setVisible(false);
        		stageSwitchInterface.switchToLoadingStage(difficulty.getMonsterSpawnChance(),difficulty.getRoomSpawnChance());
        	}else {
	            act(Gdx.graphics.getDeltaTime());
	            eventManager.checkEvents();
	    		worldManager.updateWorld(spriteManager.getKnight().getSprite());
	    		spriteManager.updateSpriteList();
	            super.draw();
        	}
        }
    }
    
    private void updateBars () {
		timer.scheduleTask(new Task() {
			@Override
			public void run() {
        		setHealthBar();
        		setStaminaBar();
			}
		}, 0,BARS_UPDATE_DELAY);
    }

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Input.Keys.W:
			movement.addFirst(keycode);
			spriteManager.getKnight().setMovementX(0);
			spriteManager.getKnight().setMovementY(spriteManager.getKnight().getMovementSpeed());
			break;
		case Input.Keys.S:
			movement.addFirst(keycode);
			spriteManager.getKnight().setMovementX(0);
			spriteManager.getKnight().setMovementY(-spriteManager.getKnight().getMovementSpeed());
			break;
		case Input.Keys.A:
			movement.addFirst(keycode);
			spriteManager.getKnight().setMovementY(0);
			spriteManager.getKnight().setMovementX(-spriteManager.getKnight().getMovementSpeed());
			break;
		case Input.Keys.D:
			movement.addFirst(keycode);
			spriteManager.getKnight().setMovementY(0);
			spriteManager.getKnight().setMovementX(spriteManager.getKnight().getMovementSpeed());
			break;
		case Input.Keys.ESCAPE:
			if(menuIsOpen){
				menuIsOpen = false;
				spriteManager.getKnight().setMovementX(0);
				spriteManager.getKnight().setMovementY(0);
				spriteManager.getKnight().setMovementSpeed(Knight.DEFAULT_MOVEMENT_SPEED);
				menuTable.setVisible(false);
			}else {
				menuIsOpen = true;
				spriteManager.getKnight().setMovementX(0);
				spriteManager.getKnight().setMovementY(0);
				spriteManager.getKnight().setMovementSpeed(0);
				menuTable.setVisible(true);
			}
			break;
		case Input.Keys.SPACE:
			if(!spriteManager.getKnight().isAttacking() && spriteManager.getKnight().getStamina() >= spriteManager.getKnight().getAttackStaminaCost()) {
				spriteManager.getKnight().attackAnimation();
			}
			break;
		default: break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Input.Keys.W:
		case Input.Keys.S:
			if(movement.first() == keycode) {
				movement.removeFirst();
				spriteManager.getKnight().setMovementY(0);
				checkIfButtonsPressed();
			}else {
				movement.removeValue(keycode,true);
			}
			break;
		case Input.Keys.A:
		case Input.Keys.D:
			if(movement.first() == keycode) {
				movement.removeFirst();
				spriteManager.getKnight().setMovementX(0);
				checkIfButtonsPressed();
			}else {
				movement.removeValue(keycode,true);
			}
			break;
		default:
		}
		return true;
	}

	/**
	 * additional {@link Knight} movement logic.
	 */
	private void checkIfButtonsPressed() {
		if(movement.size >= 1) {
			switch(movement.first()) {
			case Input.Keys.W:
				spriteManager.getKnight().setMovementX(0);
				spriteManager.getKnight().setMovementY(spriteManager.getKnight().getMovementSpeed());
				break;
			case Input.Keys.S:
				spriteManager.getKnight().setMovementX(0);
				spriteManager.getKnight().setMovementY(-spriteManager.getKnight().getMovementSpeed());
				break;
			case Input.Keys.A:
				spriteManager.getKnight().setMovementY(0);
				spriteManager.getKnight().setMovementX(-spriteManager.getKnight().getMovementSpeed());
				break;
			case Input.Keys.D:
				spriteManager.getKnight().setMovementY(0);
				spriteManager.getKnight().setMovementX(spriteManager.getKnight().getMovementSpeed());
				break;
			default:
			}
		}
	}

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

	public WorldManager getWorldManager() {
		return worldManager;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}
	
	public SpriteManager getSpriteManager() {
		return spriteManager;
	}

	public void setHealthBar() {
		tableBarsHealthSquares.setX(-160);
		healthValue.setText(spriteManager.getKnight().getHealth()+"/"+spriteManager.getKnight().getMaxHealth());
		tableBarsHealthSquares.moveBy((spriteManager.getKnight().getHealth() / (spriteManager.getKnight().getMaxHealth()/10))*16f, 0);
	}
	
	public void setStaminaBar() {
		tableBarsStaminaSquares.setX(-160);
		staminaValue.setText(spriteManager.getKnight().getStamina()+"/"+spriteManager.getKnight().getMaxStamina());
		tableBarsStaminaSquares.moveBy((spriteManager.getKnight().getStamina() / (spriteManager.getKnight().getMaxStamina()/10))*16f, 0);
	}

	public void setGoldValue(Label goldValue) {
		this.goldValue = goldValue;
	}

	public void setLevelValue(Label levelValue) {
		this.levelValue = levelValue;
	}

	public void addGold(int goldAmount) {
		gold += goldAmount;
		goldValue.setText("Gold " + gold);
	}

	@Override
	public void addHealth(int health) {
		spriteManager.getKnight().setMaxHealth(spriteManager.getKnight().getMaxHealth()+1);
		spriteManager.getKnight().setHealth(spriteManager.getKnight().getHealth()+health);
	}

	@Override
	public void addStamina(int stamina) {
		spriteManager.getKnight().setMaxStamina(spriteManager.getKnight().getMaxStamina()+1);
		spriteManager.getKnight().setStamina(spriteManager.getKnight().getStamina()+stamina);
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
		goldValue.setText("Gold " + gold);
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
		levelValue.setText(difficulty.getLevelName());
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}
	
	public Table getDeathTable() {
		return deathTable;
	}

	public Table getMenuTable() {
		return menuTable;
	}

	@Override
	public void dispose() {
		super.dispose();
		worldManager.dispose();
		eventManager.dispose();
		spriteManager.dispose();
		movement.clear();
		timer.clear();
	}
	
}
