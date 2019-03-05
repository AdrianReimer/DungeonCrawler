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

package com.mygdx.ai.slime.stateMachine;

/**
 * Transition for the {@link StateMachine}.
 * @author Adrian Reimer
 *
 */
public class Transition {
	State	from;
	Action	action;
	State	to;

	/**
	 * Transition constructor.
	 * @param from | current {@link State} of the {@link StateMachine}.
	 * @param action | action that applies to the {@link StateMachine}.
	 * @param to | destination {@link State}.
	 */
	public Transition(State from, Action action, State to) {
		this.from = from;
		this.action = action;
		this.to = to;
	}
}
