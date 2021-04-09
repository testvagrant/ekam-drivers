package com.testvagrant.optimus.mdb;

import com.testvagrant.optimus.commons.entities.DeviceDetails;

import java.util.List;

public abstract class Mobile {

  protected List<DeviceDetails> deviceDetails;

  public abstract List<DeviceDetails> getDevices();
}
