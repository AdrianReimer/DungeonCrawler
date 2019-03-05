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

public enum CellProperties {
	// Collision
	COLLISION("block"),
	// Connection
	CONNECTION("connection"),
	NORTH("north"),
	EAST("east"),
	SOUTH("south"),
	WEST("west"),
	// Spawn
	SPAWN("spawn"),
	// End
	END("end"),
	// Potions
	STRENGTH_POTION("StrengthPotion"),
	HEALTH_POTION("HealthPotion"),
	AGILITY_POTION("AgilityPotion"),
	SPEED_POTION("SpeedPotion"),
	// Chests
	CHEST1("Chest1"),
	CHEST2("Chest2"),
	CHEST3("Chest3");
	

    private String propertyName;

	CellProperties(String propertyName) {
        this.propertyName = propertyName;
    }

    public String get() {
        return propertyName;
    }

}
