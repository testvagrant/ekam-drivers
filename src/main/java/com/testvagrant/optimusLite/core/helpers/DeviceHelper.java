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
 *
 */

package com.testvagrant.optimusLite.core.helpers;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.testvagrant.optimusLite.commons.entities.DeviceDetails;
import com.testvagrant.optimusLite.commons.exceptions.OptimusException;
import com.testvagrant.optimusLite.commons.utils.JsonParser;
import com.testvagrant.optimusLite.core.updater.DeviceMiner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeviceHelper {

    private JsonObject testFeedJson;

    public DeviceHelper(String appJson) {
        JsonArray jsonArray = (JsonArray) new JsonParser().getAppJson(appJson).get("testFeed");
        this.testFeedJson = (JsonObject) jsonArray.get(0);
    }


    public List<String> getConnectedDevicesMatchingRunCriteria() throws OptimusException {
        List<String> connectedDevices = new DeviceMiner(new ArrayList<>(), testFeedJson) //TODO: Fetch devices from inmemory
                .getAllDevicesThatMatchTheCriteria().stream()
                .map(DeviceDetails::getUdid)
                .collect(Collectors.toList());
        return connectedDevices;

    }


}
