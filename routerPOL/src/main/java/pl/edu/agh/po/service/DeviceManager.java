package pl.edu.agh.po.service;

import pl.edu.agh.po.model.Device;

import java.util.ArrayList;

public interface DeviceManager {
    void addDevice(Device device);
    void deleteDevice(long id);
    void updateConfiguration(long id, String config);
    Device getDeviceById(long id);
    ArrayList<Device> getAllDevices();
}
