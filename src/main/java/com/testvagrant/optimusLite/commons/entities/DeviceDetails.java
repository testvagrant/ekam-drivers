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

package com.testvagrant.optimusLite.commons.entities;

import com.testvagrant.optimusLite.commons.entities.device.DeviceType;
import com.testvagrant.optimusLite.commons.entities.device.Platform;
import com.testvagrant.optimusLite.commons.entities.device.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.concurrent.Callable;

@Getter
@Setter
public class DeviceDetails implements Serializable, Callable {

  private String deviceName;
  private Platform platform;
  private String platformVersion;
  private DeviceType runsOn;
  private String udid;

  @Override
  public String toString() {
    return "DeviceDetails{"
        + "deviceName='"
        + deviceName
        + '\''
        + ", udid='"
        + udid
        + '\''
        + ", platform="
        + platform
        + ", platformVersion='"
        + platformVersion
        + '\''
        + ", runsOn="
        + runsOn.name()
        + '}';
  }

  @Override
  public DeviceDetails call() throws Exception {
    return this;
  }
}
