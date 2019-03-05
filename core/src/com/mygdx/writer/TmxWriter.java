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

package com.mygdx.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.badlogic.gdx.Gdx;

/**
 * Very basic hard-coded Tmx Writer.
 * --> fix and improve robins implementation, when time ?
 * @author Adrian Reimer
 *
 */
public class TmxWriter {
	
	public void writeTmxFile(String floorLayer, String objLayer, String filepath) {
		try (FileWriter fw = new FileWriter(filepath);BufferedWriter bw = new BufferedWriter(fw)) {
			bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			bw.newLine();
			bw.write("<map version=\"1.0\" tiledversion=\"1.0.2\" orientation=\"orthogonal\" renderorder=\"right-down\" width=\"500\" height=\"500\" tilewidth=\"32\" tileheight=\"32\" nextobjectid=\"1\">");
			bw.newLine();
			bw.write(" <tileset firstgid=\"1\" source=\"tiles.tsx\"/>");
			bw.newLine();
			bw.write(" <tileset firstgid=\"141\" source=\"dungeon_objects.tsx\"/>");
			bw.newLine();
			bw.write(" <tileset firstgid=\"617\" source=\"grey.tsx\"/>");
			bw.newLine();
			bw.write(" <tileset firstgid=\"757\" source=\"lava.tsx\"/>");
			bw.newLine();
			bw.write(" <layer name=\"floor\" width=\"500\" height=\"500\">");
			bw.newLine();
			bw.write("  <data encoding=\"csv\">");
			bw.newLine();
			bw.write(floorLayer);
			bw.newLine();
			bw.write("  </data>");
			bw.newLine();
			bw.write(" </layer>");
			bw.newLine();
			bw.write(" <layer name=\"obj\" width=\"500\" height=\"500\">");
			bw.newLine();
			bw.write("  <data encoding=\"csv\">");
			bw.newLine();
			bw.write(objLayer);
			bw.newLine();
			bw.write("  </data>");
			bw.newLine();
			bw.write(" </layer>");
			bw.newLine();
			bw.write("</map>");
			bw.newLine();
		} catch (IOException e1) {
			Gdx.app.log("SaveMap", "Error saving TiledMap",e1);
		}
	}
}
