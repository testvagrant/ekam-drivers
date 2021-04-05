package com.testvagrant.optimusLite.mdb.android;

import com.testvagrant.optimusLite.commons.entities.SmartBOT;
import com.testvagrant.optimusLite.commons.entities.performance.Activity;
import com.testvagrant.optimusLite.commons.entities.performance.CpuStatistics;
import com.testvagrant.optimusLite.commons.entities.performance.MemoryStatistics;
import com.testvagrant.optimusLite.mdb.core.MDB;

public interface ADB extends MDB {

    /**
     * Captures the Memory at a particular interval. Usage is captured in Total and Actual MegaBytes
     * @param smartBOT
     * @return
     */
    MemoryStatistics getMemoryInfo(SmartBOT smartBOT); //TODO: Remove SmartBot

    /**
     * Captures the CPU at a particular interval. Usage is captured in User and Kernel percentage
     * @param smartBOT
     * @return
     */
    CpuStatistics getCpuInfo(SmartBOT smartBOT);

    /**
     * Captures the current focused activity.
     * @param smartBOT
     * @return
     */
    Activity getActivity(SmartBOT smartBOT);

}
