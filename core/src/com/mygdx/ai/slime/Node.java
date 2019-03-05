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

package com.mygdx.ai.slime;

/**
 * Simple Node that holds x and y Position.
 * @author Adrian Reimer
 */
public class Node <T extends Number> {
	
	private T x;
	private T y;
	
	/**
	 * Node Constructor.	
	 * @param x | x position of the node.
	 * @param y | y position of the node.
	 */
	public Node(T x, T y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * x position getter.
	 * @return x position of the node.
	 */
	public T getX() {
		return x;
	}
	
	/**
	 * y position getter.
	 * @return y position of the node.
	 */
	public T getY() {
		return y;
	}

}
