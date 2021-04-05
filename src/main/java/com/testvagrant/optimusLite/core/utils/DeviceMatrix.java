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

package com.testvagrant.optimusLite.core.utils;

//import com.testvagrant.commons.entities.DeviceDetails;

import com.testvagrant.optimusLite.commons.entities.DeviceDetails;
import com.testvagrant.optimusLite.commons.utils.JsonParser;
import com.testvagrant.optimusLite.core.parser.OptimusConfigParser;
import com.testvagrant.optimusLite.mdb.android.Android;
import com.testvagrant.optimusLite.mdb.ios.IOS;
import org.apache.commons.exec.OS;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps all devices to a platform by name and id
 */
public class DeviceMatrix {


    private List<DeviceDetails> deviceDetailsList = new ArrayList<>();

    public DeviceMatrix(String testFeed) throws FileNotFoundException {
        init(testFeed);
    }

    public List<DeviceDetails> getDeviceDetailsList() {
        return deviceDetailsList;
    }

    private void init(String testFeed) throws FileNotFoundException {
        OptimusConfigParser configParser = new OptimusConfigParser(testFeed);

        if (configParser.isForAndroid())
            deviceDetailsList.addAll(new Android().getDevices());

        if (OS.isFamilyMac()) {
            if (configParser.isForIos())
                deviceDetailsList.addAll(new IOS().getDevices());
        }

    }


}
