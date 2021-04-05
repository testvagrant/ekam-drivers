/*
 * Copyright (c) 2017.  TestVagrant Technologies
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.testvagrant.optimusLite.commons.utils;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.URL;


public class JsonParser {
    public <T> T getAppJson(String name, Class<T> tClass)  {
        String testFeedStream = this.getClass().getClassLoader().getResource(String.format("%s.json",name)).getPath();
        try {
            return new Gson().fromJson(new FileReader(testFeedStream), tClass);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public JsonObject getAppJson(String name) {
        return  getAppJson(name, JsonObject.class);
    }
}
