package pl.edu.agh.po.service;

import pl.edu.agh.po.dao.DeviceDAO;
import pl.edu.agh.po.model.Device;

import java.util.ArrayList;

public class DeviceService implements DeviceManager{

    private final DeviceDAO deviceDAO = DeviceDAO.getInstance();
    private static DeviceService instance;

    public static DeviceService getInstance(){
        if (instance == null){
            instance = new DeviceService();
        }
        return instance;
    }
    public void addDevice(Device device) {
        deviceDAO.save(device);
    }

    public void deleteDevice(long id) {
        deviceDAO.deleteByID(id);
    }

    public ArrayList<Device> getAllDevices() {
        return deviceDAO.findAll();
    }

    public Device getDeviceById(long id) {
        return deviceDAO.findByID(id);
    }

    public void updateConfiguration(long id, String newConfig) {
        Device device = deviceDAO.findByID(id);
        if (device == null) {
            throw new IllegalArgumentException("Device not found");
        }
        device.setConfiguration(newConfig);
        deviceDAO.updateData(device);
    }
}