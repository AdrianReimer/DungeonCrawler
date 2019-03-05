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

package com.mygdx.constant;

import java.util.Random;

/**
 * Constants not really related to a single specific class and not worth to enumerable.
 * @author Adrian Reimer
 *
 */
public final class GameConstants {
	public static final Random RANDOM = new Random();
	public static final String DIALOG_WINDOW_STYLE = "dialog";
	public static final String TABLE_BACKGROUND = "window";
	
	/**
	 * hides Default constructor.
	 */
	private GameConstants(){};

}
