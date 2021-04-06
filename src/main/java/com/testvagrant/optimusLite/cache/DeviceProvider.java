package com.testvagrant.optimusLite.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.testvagrant.optimusLite.commons.entities.DeviceDetails;
import org.apache.commons.lang3.SerializationUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public abstract class DeviceProvider {

    protected LoadingCache<String, DeviceDetails> deviceCache;

    protected synchronized LoadingCache<String, DeviceDetails> getDeviceCache(
            List<DeviceDetails> deviceDetailsList) {
        return CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(4, TimeUnit.HOURS)
                .build(
                        new CacheLoader<String, DeviceDetails>() {
                            @Override
                            public DeviceDetails load(String udid) {
                                return getDeviceDetails(deviceDetailsList, udid);
                            }
                        });
    }

    private synchronized DeviceDetails getDeviceDetails(
            List<DeviceDetails> deviceDetailsList, String udid) {
        return deviceDetailsList.stream()
                .filter(deviceDetails -> deviceDetails.getUdid().equals(udid))
                .findAny().orElseThrow(() -> new RuntimeException("No Devices Available"));
    }

    public synchronized DeviceDetails getAvailableDevice() throws ExecutionException, CloneNotSupportedException {
        Optional<DeviceDetails> deviceDetails = deviceCache.asMap().values().stream().findAny();
        if (deviceDetails.isPresent()) {
            DeviceDetails originalDeviceDetails = deviceDetails.get();
            engageDevice(originalDeviceDetails);
            DeviceDetails deviceDetailsCopy = SerializationUtils.clone(originalDeviceDetails);
            return deviceDetailsCopy;
        }
        throw new RuntimeException("No available devices");
    }

    private synchronized void engageDevice(DeviceDetails deviceDetails) {
        deviceCache.invalidate(deviceDetails.getUdid());
    }

    public synchronized void releaseDevice(DeviceDetails deviceDetails) {
        deviceCache.put(deviceDetails.getUdid(), deviceDetails);
    }

    protected synchronized void addDeviceDetailsToCache(List<DeviceDetails> devices) {
      deviceCache = getDeviceCache(devices);
      devices.parallelStream().forEach(deviceDetails -> deviceCache.put(deviceDetails.getUdid(), deviceDetails));
    }

    public int getTotalAvailableDevices() {
        return deviceCache.asMap().size();
    }
}
