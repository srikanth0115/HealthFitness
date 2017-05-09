package com.example.srikanthbandi.healthfit.bluetooth;

import java.util.HashMap;


public class BleSensors {

    private static HashMap<String, BleSensor<?>> SENSORS = new HashMap<String, BleSensor<?>>();

    static {
        final BleTestSensor testSensor = new BleTestSensor();
        final BleHeartRateSensor heartRateSensor = new BleHeartRateSensor();

        SENSORS.put(testSensor.getServiceUUID(), testSensor);
        SENSORS.put(heartRateSensor.getServiceUUID(), heartRateSensor);
    }

    public static BleSensor<?> getSensor(String uuid) {
        return SENSORS.get(uuid);
    }
}
