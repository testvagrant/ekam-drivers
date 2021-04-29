package com.testvagrant.optimus.mdb;

import com.testvagrant.optimus.commons.entities.TargetDetails;

import java.util.List;

public abstract class Mobile {

  protected List<TargetDetails> targetDetails;

  public abstract List<TargetDetails> getDevices();
}
