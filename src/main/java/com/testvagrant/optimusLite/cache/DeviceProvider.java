package com.testvagrant.optimusLite.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.testvagrant.optimusLite.commons.entities.DeviceDetails;
import com.testvagrant.optimusLite.commons.entities.device.Status;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class DeviceProvider {

    protected LoadingCache<Status, List<DeviceDetails>> deviceCache;

    protected abstract void createDeviceCache();

    protected LoadingCache<Status, List<DeviceDetails>> getDeviceCache(List<DeviceDetails> deviceDetailsList) {
        return CacheBuilder.newBuilder()
                .maximumSize(50)
                .expireAfterWrite(4, TimeUnit.HOURS)
                .build(
                        new CacheLoader<Status, List<DeviceDetails>>() {
                            @Override
                            public List<DeviceDetails> load(Status status) throws Exception {
                                List<DeviceDetails> deviceDetailsByStatus = getDeviceDetailsByStatus(deviceDetailsList, status);
                                return deviceDetailsByStatus;
                            }
                        }
                );
    }


    private List<DeviceDetails> getDeviceDetailsByStatus(List<DeviceDetails> deviceDetailsList, Status status) {
        return deviceDetailsList.stream().filter(deviceDetails -> deviceDetails.getStatus().equals(status)).collect(Collectors.toList());
    }

    public DeviceDetails getAvailableDevice() throws ExecutionException {
        return deviceCache.get(Status.Available).stream().findAny().get();
    }
}


