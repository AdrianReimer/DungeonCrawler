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
import com.google.common.collect.Lists;

/**
 * Transition Table for the {@link StateMachine}.
 * @author Adrian Reimer
 *
 */
public enum TransitionConfiguration {
	DEFAULT(Lists.newArrayList(
			new Transition(State.IDLE, 		Action.IN_AREA_REQUEST, 			State.LOOKING),	
            new Transition(State.LOOKING, 	Action.IN_AGGRORANGE_REQUEST, 		State.HUNTING),	
            new Transition(State.HUNTING, 	Action.NOT_IN_AGGRORANGE_REQUEST, 		State.LOOKING), 	
            new Transition(State.LOOKING, 	Action.NOT_IN_AREA_REQUEST, 			State.IDLE)
    ));

    private List<Transition> transitions;

    /**
     * TransitionConfiguration constructor.
     * @param transitions | {@link List} of {@link Transition}s
     */
    TransitionConfiguration(List<Transition> transitions) {
        this.transitions = transitions;
    }

    /**
     * Transition List getter.
     * @return {@link List} of {@link Transition}s
     */
    public List<Transition> getTransitions() {
        return transitions;
    }
}
