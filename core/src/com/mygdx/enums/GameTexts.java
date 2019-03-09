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

public enum GameTexts {
	// ### DeathStage
	// Label
	DEATH_STAGE_LABEL("*** You Died ***\n\nEnter your Name for the Highscore-List\n"),
	// ### SaveStage
	// Label
	SAVE_STAGE_LABEL("**** Save your Game by Selecting a Slot ****\n\nSlot - Date\n\n\n\n"),
	// Button
	SAVE_STAGE_BUTTON1("SaveSpace1"),
	SAVE_STAGE_BUTTON2("SaveSpace2"),
	SAVE_STAGE_BUTTON3("SaveSpace3"),
	SAVE_STAGE_BUTTON4("SaveSpace4"),
	SAVE_STAGE_BUTTON5("SaveSpace5"),
	SAVE_STAGE_BACK_BUTTON("Back"),
	// ### MainMenuStage
	// Label
	MAIN_MENU_STAGE_LABEL("**** Game made by Adrian Reimer ****\n\nCredits\n_______________\nUI "
			+ "Skin : Commodore 64 UI Ver. 1\nCreated by Raymond Raeleus Buckley\nVisit ray3k.com "
			+ "for games, tutorials, and much more!\n\nSoundEffects obtained from https://www.zapsplat.com\n"
			+ "SoundEffects by Morten Barfod Soegaard, Little Robot Sound Factory   \nwww.littlerobotsoundfactory.com\n\n"
			+ "Video Dungeon Crawl Kevin MacLeod (incompetech.com)\nLicensed under Creative Commons: By Attribution 3.0 License\n"
			+ "http://creativecommons.org/licenses/by/3.0/\n_______________\n\nControls\n_______________\nW - Forwards\nA - Left\n"
			+ "S - Backwards\nD - Right\nSpace - Attack\n_______________\n"),
	// Button
	MAIN_MENU_STAGE_RESUME_BUTTON("Resume"),
	MAIN_MENU_STAGE_NEWGAME_BUTTON("New Game"),
	MAIN_MENU_STAGE_PLAY_BUTTON("Play"),
	MAIN_MENU_STAGE_LOAD_BUTTON("Load"),
	MAIN_MENU_STAGE_OPTION_BUTTON("Options"),
	MAIN_MENU_STAGE_HIGHSCORE_BUTTON("Highscores"),
	MAIN_MENU_STAGE_EXIT_BUTTON("Exit"),
	// ### LoadStage
	// Label
	LOAD_STAGE_LABEL("**** Load your Game by Selecting a Slot ****\n\nSlot - Date\n\n\n\n"),
	// Button
	LOAD_STAGE_BUTTON1("LoadSpace1"),
	LOAD_STAGE_BUTTON2("LoadSpace2"),
	LOAD_STAGE_BUTTON3("LoadSpace3"),
	LOAD_STAGE_BUTTON4("LoadSpace4"),
	LOAD_STAGE_BUTTON5("LoadSpace5"),
	LOAD_STAGE_BACK_BUTTON("Back"),
	// ### LoadingStage
	// Label
	LOADING_STAGE_LABEL("Loading..."),
	// ### OptionStage
	// Label
	OPTION_STAGE_DISPLAY_SETTINGS_TEXT("\nDisplay Settings\n"),
	OPTION_STAGE_GAME_SETTINGS_TEXT("\nGame Settings\n"),
	OPTION_STAGE_SOUND_SETTINGS_TEXT("\nSound Volume\n"),
	OPTION_STAGE_MUSIC_SETTINGS_TEXT("\nMusic Volume\n"),
	// Button
	OPTION_STAGE_BACK_BUTTON("Back"),
	// ### GameStage
	// Label
	GAME_STAGE_DEFAULT_VALUE("?/?"),
	GAME_STAGE_DEATH_LABEL("You Died.\n\nDo you want to be on the Highscore List?\n\n\n"),
	GAME_STAGE_MENU_LABEL("Main Menu\n"),
	// Button
	GAME_STAGE_SAVE_BUTTON("Save"),
	GAME_STAGE_LOAD_BUTTON("Load"),
	GAME_STAGE_OPTION_BUTTON("Options"),
	GAME_STAGE_MAINMENU_BUTTON("MainMenu"),
	GAME_STAGE_EXIT_BUTTON("Exit"),
	GAME_STAGE_YES_BUTTON("Yes"),
	GAME_STAGE_NO_BUTTON("No"),
	// ### HighscoreStage
	// Label
	HIGHSCORE_STAGE_LABEL("Top 10 Players\n\n"),
	// Button
	HIGHSCORE_STAGE_BACK_BUTTON("Back");
	
	
    private String text;

    GameTexts(String text) {
        this.text = text;
    }

    public String get() {
        return text;
    }
}
