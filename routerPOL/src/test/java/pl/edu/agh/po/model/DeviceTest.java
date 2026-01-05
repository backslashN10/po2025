package pl.edu.agh.po.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeviceTest {

    @Test
    public void testConstructorWithId() {
        Device device = new Device(1L, DeviceType.ROUTER, DeviceStatus.AVAILABLE,
                "ModelX", "host1", 4, "config");

        assertEquals(1L, device.getId());
        assertEquals(DeviceType.ROUTER, device.getType());
        assertEquals(DeviceStatus.AVAILABLE, device.getStatus());
        assertEquals("ModelX", device.getModel());
        assertEquals("host1", device.getHostName());
        assertEquals(4, device.getNumberOfEthernetInterfaces());
        assertEquals("config", device.getConfiguration());
    }

    @Test
    public void testConstructorWithoutId() {
        Device device = new Device(DeviceType.SWITCH, DeviceStatus.IN_USE,
                "ModelY", "host2", 8, "cfg2");

        // id domyślnie 0, bo nie ustawiony
        assertEquals(0L, device.getId());
        assertEquals(DeviceType.SWITCH, device.getType());
        assertEquals(DeviceStatus.IN_USE, device.getStatus());
        assertEquals("ModelY", device.getModel());
        assertEquals("host2", device.getHostName());
        assertEquals(8, device.getNumberOfEthernetInterfaces());
        assertEquals("cfg2", device.getConfiguration());
    }

    @Test
    public void testSetConfiguration() {
        Device device = new Device(DeviceType.ACCESS_POINT, DeviceStatus.BROKEN,
                "AP-123", "ap1", 2, "oldConfig");

        device.setConfiguration("newConfig");
        assertEquals("newConfig", device.getConfiguration());
    }
}