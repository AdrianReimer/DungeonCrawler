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

import java.util.List;

import com.mygdx.model.Slime;

/**
 * State Machine for the {@link Slime}.
 * @author Adrian Reimer
 *
 */
public class StateMachine {
	private List<Transition>	transitions;
	private Request				request;

	/**
	 * StateMachine constructor.
	 * @param config | {@link TransitionConfiguration}.
	 * @param request | {@link Request}.
	 */
	public StateMachine(TransitionConfiguration config, Request request) {
		this.request = request;
		this.transitions = config.getTransitions();
	}

	/**
	 * Applies an action to the StateMachine.
	 * @param action | {@link Action}.
	 * @return this {@link StateMachine}.
	 */
	public StateMachine apply(Action action) {
		for (Transition transition : transitions) {
			boolean currentStateMatches = transition.from.equals(request.getState());
			boolean conditionsMatch = transition.action.equals(action);

			if (currentStateMatches && conditionsMatch) {
				request.setState(transition.to);
				break;
			}
		}

		return this;
	}

	/**
	 * {@link Request} getter.
	 * @return {@link Request} of the StateMachine.
	 */
	public Request getRequest() {
		return request;
	}
	
}
