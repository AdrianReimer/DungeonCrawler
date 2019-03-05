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

import com.mygdx.model.DefaultModel;

/**
 * {@link StateMachine} Request.
 * @author Adrian Reimer
 *
 */
public class Request {
	private String	objectName;
	private State	state;

	/**
	 * Request constructor.
	 * @param objectName | name of the Model class that inherits {@link DefaultModel}.
	 * @param currentState | preferably IDLE {@link State}.
	 */
	public Request(String objectName, State currentState) {
		this.objectName = objectName;
		this.state = currentState; 
	}

	/**
	 * Object-Name getter.
	 * @return Model class name
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * Object-Name setter.
	 * @param objectName | Model class name
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * {@link State} getter.
	 * @return current {@link State}.
	 */
	public State getState() {
		return state;
	}

	/**
	 * {@link State} setter.
	 * @param state | {@link State}.
	 */
	public void setState(State state) {
		this.state = state;
	}
}
